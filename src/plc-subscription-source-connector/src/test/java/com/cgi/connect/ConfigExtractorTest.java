package com.cgi.connect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.cgi.connect.config.ConfigExtractor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ConfigExtractorTest {
  @Test
  public void shouldExtractMappings() {

    final Map<String, String> sourceProperties = new HashMap<>();
    sourceProperties.put(PLCSubscriptionSourceConfig.SUBSCRIPTIONS, "machine1,machine2");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS, "id,date,fabricationId,weight");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_KEY, "id,fabricationId");
    sourceProperties.put(PLCSubscriptionSourceConfig.PLC_CONNECTION_STRING, "myConnectionString");

    sourceProperties.put("plc.mappings.machine1.id.path", "ns=2;s=Id_1:STRING");
    sourceProperties.put("plc.mappings.machine1.date.path", "ns=2;s=Fabrication_Date_1:STRING");
    sourceProperties.put(
        "plc.mappings.machine1.fabricationId.path", "ns=2;s=Fabrication_Id_1:STRING");
    sourceProperties.put("plc.mappings.machine1.weight.path", "ns=2;s=Fabrication_Weight_1:STRING");

    sourceProperties.put("plc.mappings.machine2.id.path", "ns=2;s=Id_2:STRING");
    sourceProperties.put("plc.mappings.machine2.date.path", "ns=2;s=Fabrication_Date_2:STRING");
    sourceProperties.put(
        "plc.mappings.machine2.fabricationId.path", "ns=2;s=Fabrication_Id_2:STRING");
    sourceProperties.put("plc.mappings.machine2.weight.path", "ns=2;s=Fabrication_Weight_2:STRING");

    var result = ConfigExtractor.mappings(sourceProperties);

    assertEquals(result.keySet().size(), 2);
    assertNotNull(result.get("machine1"));
    assertNotNull(result.get("machine2"));

    assertEquals(result.get("machine1").size(), 4);
    assertEquals(result.get("machine2").size(), 4);

    assertNotNull(
        result.get("machine1").stream().filter(pair -> pair.getKey().equals("id")).findAny());
    assertNotNull(
        result.get("machine1").stream().filter(pair -> pair.getKey().equals("date")).findAny());
    assertNotNull(
        result.get("machine1").stream()
            .filter(pair -> pair.getKey().equals("fabricationId"))
            .findAny());
    assertNotNull(
        result.get("machine1").stream().filter(pair -> pair.getKey().equals("weight")).findAny());

    assertNotNull(
        result.get("machine2").stream().filter(pair -> pair.getKey().equals("id")).findAny());
    assertNotNull(
        result.get("machine2").stream().filter(pair -> pair.getKey().equals("date")).findAny());
    assertNotNull(
        result.get("machine2").stream()
            .filter(pair -> pair.getKey().equals("fabricationId"))
            .findAny());
    assertNotNull(
        result.get("machine2").stream().filter(pair -> pair.getKey().equals("weight")).findAny());
  }

  @Test
  public void shouldExtractSubscriptionPath() {

    final Map<String, String> sourceProperties = new HashMap<>();
    sourceProperties.put(PLCSubscriptionSourceConfig.SUBSCRIPTIONS, "machine1,machine2");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS, "id,date,fabricationId,weight");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_KEY, "id,fabricationId");
    sourceProperties.put(PLCSubscriptionSourceConfig.PLC_CONNECTION_STRING, "myConnectionString");

    sourceProperties.put("plc.subscriptions.machine1.path", "ns=2;s=Fabrication_Id_1:STRING");
    sourceProperties.put("plc.subscriptions.machine2.path", "ns=2;s=Fabrication_Id_2:STRING");

    var result =
        ConfigExtractor.subscriptionsPath(sourceProperties, List.of("machine1", "machine2"));

    assertEquals(result.keySet().size(), 2);
    assertEquals(result.get("machine1"), "ns=2;s=Fabrication_Id_1:STRING");
    assertEquals(result.get("machine2"), "ns=2;s=Fabrication_Id_2:STRING");
  }
}
