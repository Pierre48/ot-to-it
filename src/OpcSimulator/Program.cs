using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Opc.Ua;
using Opc.Ua.Client;
using Opc.Ua.Configuration;

namespace OpcSimulator
{
    class Program
    {
        static void Main(string[] args)
        {
            var wtoken = new CancellationTokenSource();
            var task = Task.Run(async () =>
            {
                while (true)
                {
                    await Run();
                    await Task.Delay(1000, wtoken.Token); // <- await with cancellation
                }
            }, wtoken.Token);
            task.Wait();
        }

        private static Task<Session> CreateSession(
            ApplicationConfiguration config,
            EndpointDescription selectedEndpoint,
            IUserIdentity userIdentity)
        {
            var endpointConfiguration = EndpointConfiguration.Create(config);
            var endpoint = new ConfiguredEndpoint(null, selectedEndpoint, endpointConfiguration);
            return Session.Create(config, endpoint, false, "OPC UA Complex Types Client", 60000, userIdentity, null);
        }


            static string GetEnvVar(string variable, string defaultValue = null)
            {
                var v = Environment.GetEnvironmentVariable(variable)??defaultValue;
                return v??defaultValue;
            }
        async static Task Run()
        {
            UserIdentity userIdentity = new UserIdentity(new AnonymousIdentityToken());
            ApplicationInstance application = new ApplicationInstance
            {
                ApplicationName = "UA Core Complex Client",
                ApplicationType = ApplicationType.Client,
                ConfigSectionName = "Opc.Ua.ComplexClient"
            };
            ApplicationConfiguration config = await application.LoadApplicationConfiguration("OpcConfig.xml", false).ConfigureAwait(false);
            var endpointURL = GetEnvVar("OPC_SERVER","opc.tcp://localhost:62541/Quickstarts/ReferenceServer");
            var selectedEndpoint = CoreClientUtils.SelectEndpoint(endpointURL, false && !true, 15000);
            var m_session = await CreateSession(config, selectedEndpoint, userIdentity).ConfigureAwait(false);



            WriteValueCollection nodesToWrite = new WriteValueCollection();

            // Int32 Node - Objects\CTT\Scalar\Scalar_Static\Int32
            WriteValue intWriteVal = new WriteValue();
            intWriteVal.NodeId = new NodeId("ns=2;s=Tag_String");
            intWriteVal.AttributeId = Attributes.Value;
            intWriteVal.Value = new DataValue();
            intWriteVal.Value.Value = "Pierre-" + DateTime.Now.ToLongTimeString();
            nodesToWrite.Add(intWriteVal);

            // Write the node attributes
            StatusCodeCollection results = null;
            DiagnosticInfoCollection diagnosticInfos;
            Console.WriteLine("Writing nodes...");

            // Call Write Service
            m_session.Write(null,
                            nodesToWrite,
                            out results,
                            out diagnosticInfos);

            // Validate the response
            ClientBase.ValidateResponse(results, nodesToWrite);

            // Display the results.
            Console.WriteLine("Write Results :");

            foreach (StatusCode writeResult in results)
            {
                Console.WriteLine("     {0}", writeResult);
            }


        }
    }
}
