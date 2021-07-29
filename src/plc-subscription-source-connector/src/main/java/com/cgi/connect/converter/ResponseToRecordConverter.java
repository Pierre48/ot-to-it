package com.cgi.connect.converter;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.plc4x.java.api.messages.PlcReadResponse;

public class ResponseToRecordConverter {

  /**
   * Return a kafka SourceRecord from a PlcReadResponse
   *
   * @param response a PLC read response
   * @param fieldTreeElements the tree structure of configured fields
   * @return a SourceRecord
   */
  public static SourceRecord convert(
      PlcReadResponse response,
      String kafkaTopic,
      Schema outputSchema,
      List<Pair<String, String>> mapping,
      List<String> keyComposition,
      Map<String, TreeElement> fieldTreeElements) {
    Struct outputValue = new Struct(outputSchema);

    mapping.forEach(
        mappingPair ->
            fillSchema(outputValue, fieldTreeElements, mappingPair.getKey(), response, 0));

    // Build output record key
    var key =
        keyComposition.stream()
            .map(outputValue::getString)
            .reduce(
                "",
                (a, b) -> {
                  if (a.isEmpty()) {
                    return b;
                  }

                  return a + "#" + b;
                });

    return new SourceRecord(
        null, // Collections.singletonMap(filename, checksumValue),
        null, // OFFSET_CHECKMARK,
        kafkaTopic,
        Schema.STRING_SCHEMA,
        key,
        outputSchema,
        outputValue);
  }

  /**
   * Fill output struct recursively
   *
   * @param outputValue the current level struct to fill
   * @param fieldTreeElements the catalog of configured fields
   * @param fieldKey the current field to fill
   * @param response the response from PLC
   * @param depth the current depth in the global struct
   */
  private static void fillSchema(
      Struct outputValue,
      Map<String, TreeElement> fieldTreeElements,
      String fieldKey,
      PlcReadResponse response,
      int depth) {

    var fieldDescription = fieldTreeElements.get(fieldKey);

    if (outputValue.schema().field(fieldKey) != null) {

      var fieldType = fieldDescription.getType();

      switch (fieldType) {
        case "FLOAT":
          outputValue.put(fieldKey, Double.valueOf(response.getFloat(fieldKey)));
          return;
        case "STRING":
        default:
          outputValue.put(fieldKey, response.getString(fieldKey));
      }
      return;
    }

    var parentElementId = TreeElement.getAncestor(fieldDescription, depth).getId();
    var subLevelStruct = outputValue.getStruct(parentElementId);

    if (subLevelStruct == null) {
      // Initialize if not exists
      subLevelStruct = new Struct(outputValue.schema().field(parentElementId).schema());
      outputValue.put(parentElementId, subLevelStruct);
    }
    fillSchema(subLevelStruct, fieldTreeElements, fieldKey, response, ++depth);
  }
}
