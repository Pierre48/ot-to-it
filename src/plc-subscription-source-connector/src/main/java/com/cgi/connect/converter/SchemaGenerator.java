package com.cgi.connect.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class SchemaGenerator {

  private static final String OUTPUT_FIELDS = "plc.output.fields";

  /**
   * @param treeElementMap a tree structure of all configured user field from the configuration
   * @return the output Struct schema
   */
  public static Schema generate(Map<String, TreeElement> treeElementMap) {

    var schemaBuilder = SchemaBuilder.struct().name("com.cgi.avro.DefaultPLCEvent");

    var firstLevelElements =
        treeElementMap.values().stream().filter(TreeElement::isRoot).collect(Collectors.toList());

    firstLevelElements.forEach(el -> buildSchemaField(el, schemaBuilder));

    return schemaBuilder.build();
  }

  /**
   * @param config the configuration properties map
   * @return a tree structure of all configured user field from the configuration
   */
  public static Map<String, TreeElement> buildFieldTree(Map<String, String> config) {

    var fieldDefinitions =
        config.entrySet().stream()
            .filter(e -> e.getKey().contains(OUTPUT_FIELDS))
            .map(e -> Pair.of(e.getKey().replaceFirst(OUTPUT_FIELDS + ".", ""), e.getValue()))
            .collect(Collectors.toList());

    var treeElements = new HashMap<String, TreeElement>();

    // Create all tree elements
    fieldDefinitions.stream()
        .flatMap(fieldEntry -> Arrays.stream(fieldEntry.getKey().split("\\.")))
        .forEach(subField -> treeElements.computeIfAbsent(subField, TreeElement::of));

    // Hierarchy Building
    fieldDefinitions.forEach(
        fieldDefinition -> {
          var fieldSplit = fieldDefinition.getKey().split("\\.");

          for (int i = 0; i < fieldSplit.length; i++) {

            var currentField = treeElements.get(fieldSplit[i]);
            currentField.setDepth(i);

            if (i == fieldSplit.length - 1) {
              // if it's the last
              currentField.setType(fieldDefinition.getValue());
            } else {
              currentField.setType("STRUCT");
            }

            if (i > 0) {

              var previousField = treeElements.get(fieldSplit[i - 1]);

              // if not the first
              currentField.setParent(previousField);
              previousField.addChild(currentField);
            }
          }
        });

    return treeElements;
  }

  private static void buildSchemaField(TreeElement el, SchemaBuilder builder) {

    if (el.getChildren().size() > 0) {
      // If it's a sub struct
      var subBuilder = SchemaBuilder.struct().optional().name(el.getId() + "_sub_Struct");
      el.getChildren().forEach(child -> buildSchemaField(child, subBuilder));
      builder.field(el.getId(), subBuilder.build());
      return;
    }

    switch (el.getType()) {
      case "FLOAT":
        builder.field(el.getId(), SchemaBuilder.OPTIONAL_FLOAT64_SCHEMA);
        return;
      case "STRING":
      default:
        builder.field(el.getId(), Schema.OPTIONAL_STRING_SCHEMA);
        // TODO Handle some more data types
    }
  }
}
