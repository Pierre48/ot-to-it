package com.cgi.connect;

import static com.cgi.connect.PLCSubscriptionSourceConfig.PLC_POLL_INTERVAL;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLCSubscriptionSourceTask extends SourceTask {
  private static final Logger log = LoggerFactory.getLogger(PLCSubscriptionSourceTask.class);

  private PollRunner runner;
  private ScheduledFuture<?> scheduledTask;

  public PLCSubscriptionSourceTask() {}

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  /**
   * Task initialisation from configuration
   *
   * @param config the map of properties
   */
  @Override
  public void start(Map<String, String> config) {
    var pollInterval = Integer.parseInt(config.get(PLC_POLL_INTERVAL));

    runner = new PollRunner();

    try {
      runner.configure(config);
    } catch (PlcConnectionException e) {
      log.error("The Plc Server could not be reached, review your configuration.", e);
      throw new IllegalStateException(
          "The Plc Server could not be reached, review your configuration.");
    }

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduledTask = scheduler.scheduleAtFixedRate(runner, 0, pollInterval, TimeUnit.MILLISECONDS);
  }

  /**
   * Kafka connect call this method when it check for more input data
   *
   * @return the list of record to publish to Kafka
   */
  @Override
  public List<SourceRecord> poll() {
    try {
      return runner.getNewEvents();
    } catch (InterruptedException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public void stop() {
    log.warn("Stopping the task...");
    runner.close();
    scheduledTask.cancel(true);
    log.warn("Scheduled Runner stopped");
  }
}
