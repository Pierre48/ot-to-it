package com.cgi.connect.config;

import static com.cgi.connect.PLCSubscriptionSourceConfig.SUBSCRIPTIONS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigExtractor {

  /**
   * Extract mapping rules from configuration
   *
   * @param props the full configuration
   * @return Fields mapping
   */
  public static Map<String, List<Pair<String, String>>> mappings(Map<String, String> props) {
    var result = new HashMap<String, List<Pair<String, String>>>();

    // extract mapping from config
    props.entrySet().stream()
        .filter(prop -> prop.getKey().contains("mapping"))
        .forEach(
            mappingPropertyEntry -> {
              var key = mappingPropertyEntry.getKey();
              var value = mappingPropertyEntry.getValue();

              var subscriptionName = key.split("\\.")[2];
              var fieldName = key.split("\\.")[3];

              var mappingForSubscription = result.get(subscriptionName);

              if (mappingForSubscription == null) {
                mappingForSubscription = new ArrayList<>();
              }

              mappingForSubscription.add(Pair.of(fieldName, value));

              result.put(subscriptionName, mappingForSubscription);
            });

    return result;
  }

  /**
   * Extract subscriptions
   *
   * @param props the full configuration
   * @return Fields mapping
   */
  public static Map<String, String> subscriptionsPath(
      Map<String, String> props, List<String> tags) {

    var result = new HashMap<String, String>();

    tags.forEach(
        tag -> {
          var path = props.get(SUBSCRIPTIONS + "." + tag + ".path");
          result.put(tag, path);
        });

    return result;
  }
}
