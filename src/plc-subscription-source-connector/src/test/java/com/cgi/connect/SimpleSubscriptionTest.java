package com.cgi.connect;

public class SimpleSubscriptionTest {

  /*
  @Ignore
  @Test
  public void shoudlWorkWithSubscriptionRequest() throws PlcConnectionException, InterruptedException {

    var plcConnection =
        ConnectionManager.getConnection("opcua:tcp://localhost:62541/Quickstarts/ReferenceServer");

    // Connection OK : lets subscribe
    var subBuilder = plcConnection.subscriptionRequestBuilder();

    subBuilder.addChangeOfStateField("machine1", "ns=2;s=Fabrication_Id_1:STRING");

    var subscriptionRequest = subBuilder.build();
    var subResponse = subscriptionRequest.execute();

    subResponse.whenComplete(
        (response, ex) -> {
            System.out.println("Result : "+ response + " - "+ex);

          for (String subscriptionName : response.getFieldNames()) {
            final var subscriptionHandle = response.getSubscriptionHandle(subscriptionName);
            subscriptionHandle.register(System.out::println);
          }
        });

    Thread.sleep(1000000000);
  }


    @Ignore
    @Test
    public void shoudlWorkWithReadRequest() throws PlcConnectionException, InterruptedException {

        var plcConnection =
                ConnectionManager.getConnection("opcua:tcp://localhost:62541/Quickstarts/ReferenceServer");

        // Connection OK : lets subscribe
        var subBuilder = plcConnection.readRequestBuilder();

        subBuilder.addItem("value-1", "ns=2;s=Fabrication_Id_1:STRING");

        var subscriptionRequest = subBuilder.build();
        var subResponse = subscriptionRequest.execute();

        subResponse.whenComplete(
                (response, ex) -> {
                    System.out.println("Result : "+ response + " - "+ex);
                });

        Thread.sleep(1000000000);
    }

    @Ignore
    @Test
    public void shoudlWorkWithReadRequestSync() throws PlcConnectionException, InterruptedException, ExecutionException {

        var plcConnection =
                ConnectionManager.getConnection("opcua:tcp://localhost:62541/Quickstarts/ReferenceServer");

        // Connection OK : lets subscribe
        var subBuilder = plcConnection.readRequestBuilder();

        subBuilder.addItem("value-1", "ns=2;s=Fabrication_Id_1:STRING");

        var subscriptionRequest = subBuilder.build();
        try {
            var response = subscriptionRequest.execute().get();
            System.out.println(response);

        } catch (Exception e) {
            System.out.println(e);
        }
  }*/
}
