using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading;
using System.Threading.Tasks;
using Opc.Ua;
using Opc.Ua.Client;
using Opc.Ua.Configuration;

namespace OpcSimulator
{
    class Program
    {
        static int idF= 1;
        private static Session  m_session;

        static void Main(string[] args)
        {

            UserIdentity userIdentity = new UserIdentity(new AnonymousIdentityToken());
            ApplicationInstance application = new ApplicationInstance
            {
                ApplicationName = "UA Core Complex Client",
                ApplicationType = ApplicationType.Client,
                ConfigSectionName = "Opc.Ua.ComplexClient"
            };
            ApplicationConfiguration config = application.LoadApplicationConfiguration("OpcConfig.xml", false).Result;
            var endpointURL = GetEnvVar("OPC_SERVER","opc.tcp://localhost:62541/Quickstarts/ReferenceServer");
            var selectedEndpoint = CoreClientUtils.SelectEndpoint(endpointURL, false && !true, 15000);
            m_session = CreateSession(config, selectedEndpoint, userIdentity).Result;



            var wtoken = new CancellationTokenSource();
            var task = Task.Run(async () =>
            {
                while (true)
                {
                    await Run();
                    await Task.Delay(200, wtoken.Token); // <- await with cancellation
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
            var i = new Random(DateTime.Now.Millisecond).Next(19)+1;


            WriteValueCollection nodesToWrite = new WriteValueCollection();

            WriteValue weight = new WriteValue();
            weight.NodeId = new NodeId($"ns=2;s=Factory.Machine{i}.Fabrication.Weight");
            weight.AttributeId = Attributes.Value;
            weight.Value = new DataValue();
            weight.Value.Value = (float)(new Random(DateTime.Now.Millisecond).Next(10000) / 100);
            nodesToWrite.Add(weight);
            WriteValue dateFabrication = new WriteValue();
            dateFabrication.NodeId = new NodeId($"ns=2;s=Factory.Machine{i}.Fabrication.Date");
            dateFabrication.AttributeId = Attributes.Value;
            dateFabrication.Value = new DataValue();
            dateFabrication.Value.Value = DateTime.Now.ToLongDateString() + " " + DateTime.Now.ToLongTimeString();
            nodesToWrite.Add(dateFabrication);
            WriteValue idFabrication = new WriteValue();
            idFabrication.NodeId = new NodeId($"ns=2;s=Factory.Machine{i}.Fabrication.Id");
            idFabrication.AttributeId = Attributes.Value;
            idFabrication.Value = new DataValue();
            idFabrication.Value.Value = idF++.ToString();
            nodesToWrite.Add(idFabrication);
            WriteValue mixingDuration = new WriteValue();
            mixingDuration.NodeId = new NodeId($"ns=2;s=Factory.Machine{i}.Fabrication.Operations.Mixing.Duration");
            mixingDuration.AttributeId = Attributes.Value;
            mixingDuration.Value = new DataValue();
            mixingDuration.Value.Value = (float)(new Random(DateTime.Now.Millisecond).Next(10000) / 100);
            nodesToWrite.Add(mixingDuration);
            WriteValue curingDuration = new WriteValue();
            curingDuration.NodeId = new NodeId($"ns=2;s=Factory.Machine{i}.Fabrication.Operations.Curing.Duration");
            curingDuration.AttributeId = Attributes.Value;
            curingDuration.Value = new DataValue();
            curingDuration.Value.Value = (float)(new Random(DateTime.Now.Millisecond).Next(10000) / 100);
            nodesToWrite.Add(curingDuration);


            // Write the node attributes
            StatusCodeCollection results = null;
            DiagnosticInfoCollection diagnosticInfos;
            Console.WriteLine("Fabrication for machine " + i);

            // Call Write Service
            m_session.Write(null,
                            nodesToWrite,
                            out results,
                            out diagnosticInfos);

            // Validate the response
            ClientBase.ValidateResponse(results, nodesToWrite);

            foreach (StatusCode writeResult in results)
            {
                Console.WriteLine("     {0}", writeResult);
            }


        }
    }
}
