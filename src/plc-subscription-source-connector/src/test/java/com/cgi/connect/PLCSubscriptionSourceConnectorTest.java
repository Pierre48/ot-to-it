package com.cgi.connect;

import static com.cgi.connect.PLCSubscriptionSourceConfig.PLC_POLL_INTERVAL;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.connector.ConnectorContext;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PLCSubscriptionSourceConnectorTest extends EasyMockSupport {

  private PLCSubscriptionSourceConnector connector;
  private Map<String, String> sourceProperties;

  @BeforeEach
  public void before() {

    final ConnectorContext context = createMock(ConnectorContext.class);

    connector = new PLCSubscriptionSourceConnector();
    connector.initialize(context);

    sourceProperties = new HashMap<>();
    sourceProperties.put(PLCSubscriptionSourceConfig.SUBSCRIPTIONS, "tag1,tag2");
    sourceProperties.put(PLCSubscriptionSourceConfig.OUTPUT_KEY, "field1,field2");
    sourceProperties.put(PLCSubscriptionSourceConfig.PLC_CONNECTION_STRING, "myConnectionString");
    sourceProperties.put(PLCSubscriptionSourceConfig.KAFKA_TOPIC, "output_topic");
    sourceProperties.put(PLC_POLL_INTERVAL, "10000");
  }

  @Test
  public void testConnectorTaskClass() {
    replayAll();
    connector.start(sourceProperties);

    assertEquals(PLCSubscriptionSourceTask.class, connector.taskClass());
    verifyAll();
  }

  @Test
  public void testSingleSourceTasksBeingCreated() {
    replayAll();
    connector.start(sourceProperties);

    final List<Map<String, String>> oneTaskConfig = connector.taskConfigs(1);
    assertEquals(1, oneTaskConfig.size());

    verifyAll();
  }

  @Test
  public void testMultipleSourceTasksBeingCreatedAsOne() {
    replayAll();
    connector.start(sourceProperties);

    final List<Map<String, String>> manyTaskConfigs = connector.taskConfigs(50);
    assertEquals(1, manyTaskConfigs.size());

    verifyAll();
  }
}
