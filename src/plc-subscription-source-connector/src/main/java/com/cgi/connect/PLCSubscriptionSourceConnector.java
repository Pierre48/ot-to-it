package com.cgi.connect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLCSubscriptionSourceConnector extends SourceConnector {

  private static final Logger log = LoggerFactory.getLogger(PLCSubscriptionSourceConnector.class);
  private static final String CONNECTOR_NAME = PLCSubscriptionSourceConnector.class.getSimpleName();
  private Map<String, String> extendedConfig;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  /** This will be executed once per connector. This can be used to handle connector level setup. */
  @Override
  public void start(Map<String, String> map) {
    log.info("The {} has been started.", CONNECTOR_NAME);
    new PLCSubscriptionSourceConfig(map);
    this.extendedConfig = map;
  }

  /** Returns your task implementation. */
  @Override
  public Class<? extends Task> taskClass() {
    return PLCSubscriptionSourceTask.class;
  }

  /**
   * Defines the individual task configurations that will be executed. The connector doesn't support
   * multi-tasking. One task only.
   */
  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    final List<Map<String, String>> configs = new ArrayList<>();

    configs.add(extendedConfig);

    return configs;
  }

  @Override
  public void stop() {
    log.info("The {} has been stopped.", CONNECTOR_NAME);
  }

  @Override
  public ConfigDef config() {
    return PLCSubscriptionSourceConfig.CONFIG_DEF;
  }
}
