using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Opc.Ua;
using Opc.Ua.Configuration;
using Quickstarts.ConsoleReferenceClient;

namespace OpcWeb.Data
{
    public class OpcService
    {
        static readonly ApplicationInstance _application = null;
        static OpcService()
        {
            _application = new ApplicationInstance();
            _application.ApplicationName = "OpcWeb";
            _application.ApplicationType = ApplicationType.Client;

            // load the application configuration.
            _application.LoadApplicationConfiguration("OpcWeb.Config.xml", false).Wait();
            // check the application certificate.
            //_application.CheckApplicationInstanceCertificate(false, 0).Wait();
        }

        public Task<OpcValue[]> GetOpcValuesAsync()
        {
            var rng = new Random();
            return Task.Factory.StartNew(() =>
            {
                UAClient uaClient = new UAClient(_application.ApplicationConfiguration);

                try
                {
                    if (uaClient.Connect())
                    {
                        uaClient.WriteNodes();
                        return uaClient.ReadNodes();
                    }
                    else
                    {
                        throw new ApplicationException("Could not connect to server!");
                    }
                }
                finally
                {
                    uaClient.Disconnect();
                }
            });
        }
    }
}
