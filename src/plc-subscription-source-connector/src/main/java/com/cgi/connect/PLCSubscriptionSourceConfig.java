package com.cgi.connect;

import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class PLCSubscriptionSourceConfig extends AbstractConfig {

  public static final String OUTPUT_FIELDS = "plc.output.fields";

  public static final String KAFKA_TOPIC = "topic";
  private static final String KAFKA_TOPIC_DOC = "The Kafka topic to write the data to.";

  public static final String SUBSCRIPTIONS = "plc.subscriptions";
  private static final String SUBSCRIPTIONS_DOC = "Comma separated list of field/tags to subscribe";

  public static final String OUTPUT_KEY = "plc.output.key";
  private static final String OUTPUT_KEY_DOC =
      "Comma separated list of output model fields used for key construction";

  public static final String PLC_CONNECTION_STRING = "plc.connection.string";
  private static final String PLC_CONNECTION_STRING_DOC =
      "The connection string to contact the PLC server.";

  public static final String PLC_POLL_INTERVAL = "plc.poll.interval";
  private static final String PLC_POLL_INTERVAL_DOC =
      "Duration in millisecond between each PLC data polling.";

  public static final ConfigDef CONFIG_DEF =
      new ConfigDef()
          .define(KAFKA_TOPIC, Type.STRING, Importance.HIGH, KAFKA_TOPIC_DOC)
          .define(SUBSCRIPTIONS, Type.STRING, Importance.HIGH, SUBSCRIPTIONS_DOC)
          .define(OUTPUT_KEY, Type.STRING, Importance.HIGH, OUTPUT_KEY_DOC)
          .define(PLC_POLL_INTERVAL, Type.STRING, Importance.HIGH, PLC_POLL_INTERVAL_DOC)
          .define(PLC_CONNECTION_STRING, Type.STRING, Importance.HIGH, PLC_CONNECTION_STRING_DOC);

  public PLCSubscriptionSourceConfig(Map<String, String> parsedConfig) {
    super(CONFIG_DEF, parsedConfig);
  }
}
