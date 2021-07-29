package com.cgi.connect;

import static com.cgi.connect.PLCSubscriptionSourceConfig.*;
import static com.cgi.connect.PLCSubscriptionSourceConfig.PLC_CONNECTION_STRING;

import com.cgi.connect.config.ConfigExtractor;
import com.cgi.connect.config.ConnectionManager;
import com.cgi.connect.converter.ResponseToRecordConverter;
import com.cgi.connect.converter.SchemaGenerator;
import com.cgi.connect.converter.TreeElement;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Scheduled runner holding the business logic */
public class PollRunner implements Runnable {
  private static final Logger log = LoggerFactory.getLogger(PollRunner.class);

  private String kafkaTopic;
  private List<String> keyComposition;
  private LinkedBlockingQueue<SourceRecord> eventQueue;
  private Map<String, Object> valueBuffer;
  private List<String> subscriptions;
  private Map<String, List<Pair<String, String>>> mappings;
  private Map<String, String> subscriptionPath;
  private PlcConnection plcConnection;
  private Schema outputSchema;
  private Map<String, TreeElement> fieldTreeElements;

  public void configure(Map<String, String> config) throws PlcConnectionException {
    // retrieve and init configuration values
    this.kafkaTopic = config.get(KAFKA_TOPIC);
    this.subscriptions =
        Arrays.stream(config.get(SUBSCRIPTIONS).split(",")).collect(Collectors.toList());
    this.keyComposition =
        Arrays.stream(config.get(OUTPUT_KEY).split(",")).collect(Collectors.toList());
    var plcConnectionString = config.get(PLC_CONNECTION_STRING);
    this.eventQueue = new LinkedBlockingQueue<>();
    // Contains path for each configured subscription
    this.valueBuffer = new HashMap<>();
    mappings = ConfigExtractor.mappings(config);
    subscriptionPath = ConfigExtractor.subscriptionsPath(config, subscriptions);

    fieldTreeElements = SchemaGenerator.buildFieldTree(config);
    outputSchema = SchemaGenerator.generate(fieldTreeElements);

    this.plcConnection = ConnectionManager.getConnection(plcConnectionString);

    // Check if this connection support subscribing to data.
    if (!plcConnection.getMetadata().canSubscribe() || !plcConnection.getMetadata().canRead()) {
      log.error("This plc driver does not have read & subscribe capabilities");
      throw new IllegalStateException(
          "This plc driver does not have read & subscribe capabilities");
    }
  }

  public List<SourceRecord> getNewEvents() throws InterruptedException {
    var result = new ArrayList<SourceRecord>();

    while (!this.eventQueue.isEmpty()) {
      result.add(eventQueue.take());
    }

    return result;
  }

  public void close() {
    try {
      plcConnection.close();
    } catch (Exception e) {
      log.error("Fail to close PlcConnection", e);
    }
  }

  @Override
  public void run() {
    // Pour chaque tag
    for (String subscription : subscriptions) {

      var subscriptionMapping = mappings.get(subscription);
      var subPath = subscriptionPath.get(subscription);

      var subField =
          subscriptionMapping.stream()
              .filter(pair -> pair.getValue().equals(subPath))
              .map(Pair::getKey)
              .findFirst()
              .get();

      // Lecture synchrone des champs associés
      var readBuilder = plcConnection.readRequestBuilder();
      subscriptionMapping.forEach(
          mappingPair -> readBuilder.addItem(mappingPair.getKey(), mappingPair.getValue()));

      var asyncResponse = readBuilder.build().execute();

      asyncResponse.whenComplete(
          (response, throwable) -> {
            if (response != null) {

              var newValue = extractFieldFromResponse(response, subField);
              var currentValue = valueBuffer.get(subPath);

              // First Value is kept as memory an will be the first reference value
              if (currentValue == null) {
                valueBuffer.put(subPath, newValue);
                return;
              }

              // Only send an event when "subscribed" field change
              if (currentValue.equals(newValue)) {
                return;
              }

              valueBuffer.put(subPath, newValue);
              var sourceRecord =
                  ResponseToRecordConverter.convert(
                      response,
                      kafkaTopic,
                      outputSchema,
                      subscriptionMapping,
                      keyComposition,
                      fieldTreeElements);
              try {
                eventQueue.put(sourceRecord);
              } catch (InterruptedException e) {
                log.error("Interrupted during event queue enrishment", e);
              }
            } else {
              log.error("Error during read request", throwable);
            }
          });
    }
  }

  private Object extractFieldFromResponse(PlcReadResponse response, String key) {

    var fieldType = fieldTreeElements.get(key).getType();

    switch (fieldType) {
      case "FLOAT":
        return response.getFloat(key);
      case "STRING":
      default:
        return response.getString(key);
    }
  }
}
