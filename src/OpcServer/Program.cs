using System;
using System.Collections.Generic;
using Mono.Options;
using Quickstarts.ReferenceServer;

namespace OpcServer
{
    class Program
    {
        public static int Main(string[] args)
        {
            Console.WriteLine("OPC UA Reference Server.Net Core");

            // command line options
            bool showHelp = false;
            bool autoAccept = false;
            bool console = false;

            Mono.Options.OptionSet options = new Mono.Options.OptionSet {
                { "h|help", "show this message and exit", h => showHelp = h != null },
                { "a|autoaccept", "auto accept certificates (for testing only)", a => autoAccept = a != null },
                { "c|console", "log trace to console", c => console = c != null }
            };

            try
            {
                IList<string> extraArgs = options.Parse(args);
                foreach (string extraArg in extraArgs)
                {
                    Console.WriteLine("Error: Unknown option: {0}", extraArg);
                    showHelp = true;
                }
            }
            catch (OptionException e)
            {
                Console.WriteLine(e.Message);
                showHelp = true;
            }

            if (showHelp)
            {
                Console.WriteLine("Usage: dotnet ConsoleReferenceServer.dll [OPTIONS]");
                Console.WriteLine();

                Console.WriteLine("Options:");
                options.WriteOptionDescriptions(Console.Out);
                return (int)ExitCode.ErrorInvalidCommandLine;
            }

            MyRefServer server = new MyRefServer(autoAccept, console);
            server.Run();

            return (int)MyRefServer.ExitCode;
        }
    }


    public enum ExitCode : int
    {
        Ok = 0,
        ErrorServerNotStarted = 0x80,
        ErrorServerRunning = 0x81,
        ErrorServerException = 0x82,
        ErrorInvalidCommandLine = 0x100
    };


}
