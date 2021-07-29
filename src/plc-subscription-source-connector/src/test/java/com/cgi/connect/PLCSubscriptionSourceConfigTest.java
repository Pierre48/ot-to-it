package com.cgi.connect;

import static com.cgi.connect.PLCSubscriptionSourceConfig.*;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.ConfigValue;
import org.junit.jupiter.api.Test;

public class PLCSubscriptionSourceConfigTest {

  @Test
  public void testConfigValidationWithAllParameters() {

    final Map<String, String> sourceProperties = new HashMap<>();
    sourceProperties.put(PLCSubscriptionSourceConfig.SUBSCRIPTIONS, "tag1,tag2");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS, "field1,field2,field3");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_KEY, "field1,field2");
    sourceProperties.put(PLCSubscriptionSourceConfig.PLC_CONNECTION_STRING, "myConnectionString");
    sourceProperties.put(PLCSubscriptionSourceConfig.KAFKA_TOPIC, "output_topic");
    sourceProperties.put(PLC_POLL_INTERVAL, "10000");

    final List<ConfigValue> configValues = CONFIG_DEF.validate(sourceProperties);

    for (ConfigValue val : configValues) {
      assertEquals("Config property errors: " + val.errorMessages(), 0, val.errorMessages().size());
    }
  }
}
