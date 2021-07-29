package com.cgi.connect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.cgi.connect.converter.SchemaGenerator;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.connect.data.Schema;
import org.junit.jupiter.api.Test;

public class SchemaGeneratorTest {
  @Test
  public void shouldBuildSchema() {

    final Map<String, String> sourceProperties = new HashMap<>();
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".id", "STRING");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".date", "STRING");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".fabricationId", "STRING");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".weight", "STRING");
    sourceProperties.put(
        PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".operation.subField1", "STRING");
    sourceProperties.put(
        PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".operation.subField2", "STRING");
    sourceProperties.put(
        PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".operation.subField3", "STRING");
    sourceProperties.put(
        PLCSubscriptionSourceConfig.OUTPUT_FIELDS + ".operation.details.subSubField1", "STRING");

    var fieldTree = SchemaGenerator.buildFieldTree(sourceProperties);
    var result = SchemaGenerator.generate(fieldTree);

    assertEquals(5, result.fields().size());
    assertEquals(4, result.field("operation").schema().fields().size());
    assertNotNull(result.field("operation").schema().field("subField1"));
    assertEquals(
        result.field("operation").schema().field("subField2").schema(),
        Schema.OPTIONAL_STRING_SCHEMA);
    assertNotNull(result.field("operation").schema().field("details"));
    assertNotNull(
        result.field("operation").schema().field("details").schema().field("subSubField1"));
  }
}
