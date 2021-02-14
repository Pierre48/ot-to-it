
using Opc.Ua;
using Opc.Ua.Configuration;
using Opc.Ua.Server;
using OpcServer;
using Quickstarts.ReferenceServer;
using Serilog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Quickstarts.ReferenceServer
{
    public class MyRefServer
    {
        private ReferenceServer m_server;
        private Task m_status;
        private DateTime m_lastEventTime;
        private bool m_autoAccept = false;
        private bool m_logConsole = false;
        private static ExitCode s_exitCode;

        public MyRefServer(bool autoAccept, bool logConsole)
        {
            m_autoAccept = autoAccept;
            m_logConsole = logConsole;
        }

        public void Run()
        {
            try
            {
                s_exitCode = ExitCode.ErrorServerNotStarted;
                ConsoleSampleServer().Wait();
                Console.WriteLine("Server started. Press Ctrl-C to exit...");
                s_exitCode = ExitCode.ErrorServerRunning;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: {0}", ex.Message);
                s_exitCode = ExitCode.ErrorServerException;
                return;
            }

            ManualResetEvent quitEvent = new ManualResetEvent(false);
            try
            {
                Console.CancelKeyPress += (sender, eArgs) => {
                    quitEvent.Set();
                    eArgs.Cancel = true;
                };
            }
            catch
            {
            }

            // wait for timeout or Ctrl-C
            quitEvent.WaitOne();

            if (m_server != null)
            {
                Console.WriteLine("Server stopped. Waiting for exit...");

                using (ReferenceServer server = m_server)
                {
                    // Stop status thread
                    m_server = null;
                    m_status.Wait();
                    // Stop server and dispose
                    server.Stop();
                }
            }

            s_exitCode = ExitCode.Ok;
        }

        public static ExitCode ExitCode { get => s_exitCode; }

        private void CertificateValidator_CertificateValidation(CertificateValidator validator, CertificateValidationEventArgs e)
        {
            if (e.Error.StatusCode == StatusCodes.BadCertificateUntrusted)
            {
                if (m_autoAccept)
                {
                    if (!m_logConsole)
                    {
                        Console.WriteLine("Accepted Certificate: {0}", e.Certificate.Subject);
                    }
                    Utils.Trace(Utils.TraceMasks.Security, "Accepted Certificate: {0}", e.Certificate.Subject);
                    e.Accept = true;
                    return;
                }
            }
            if (!m_logConsole)
            {
                Console.WriteLine("Rejected Certificate: {0} {1}", e.Error, e.Certificate.Subject);
            }
            Utils.Trace(Utils.TraceMasks.Security, "Rejected Certificate: {0} {1}", e.Error, e.Certificate.Subject);
        }

        private async Task ConsoleSampleServer()
        {
            ApplicationInstance.MessageDlg = new ApplicationMessageDlg();
            ApplicationInstance application = new ApplicationInstance {
                ApplicationName = "Quickstart Reference Server",
                ApplicationType = ApplicationType.Server,
                ConfigSectionName = Utils.IsRunningOnMono() ? "Quickstarts.MonoReferenceServer" : "Quickstarts.ReferenceServer"
            };

            // load the application configuration.
            ApplicationConfiguration config = await application.LoadApplicationConfiguration(false).ConfigureAwait(false);

            var loggerConfiguration = new Serilog.LoggerConfiguration();
            if (m_logConsole)
            {
                loggerConfiguration.WriteTo.Console(restrictedToMinimumLevel: Serilog.Events.LogEventLevel.Warning);
            }
#if DEBUG
            else
            {
                loggerConfiguration.WriteTo.Debug(restrictedToMinimumLevel: Serilog.Events.LogEventLevel.Warning);
            }
#endif
            SerilogTraceLogger.Create(loggerConfiguration, config);

            // check the application certificate.
            bool haveAppCertificate = await application.CheckApplicationInstanceCertificate(
                false, CertificateFactory.DefaultKeySize, CertificateFactory.DefaultLifeTime).ConfigureAwait(false);
            if (!haveAppCertificate)
            {
                throw new Exception("Application instance certificate invalid!");
            }

            if (!config.SecurityConfiguration.AutoAcceptUntrustedCertificates)
            {
                config.CertificateValidator.CertificateValidation += new CertificateValidationEventHandler(CertificateValidator_CertificateValidation);
            }

            // start the server.
            m_server = new ReferenceServer();
            await application.Start(m_server).ConfigureAwait(false);

            // print endpoint info
            var endpoints = application.Server.GetEndpoints().Select(e => e.EndpointUrl).Distinct();
            foreach (var endpoint in endpoints)
            {
                Console.WriteLine(endpoint);
            }

            // start the status thread
            m_status = Task.Run(new Action(StatusThread));

            // print notification on session events
            m_server.CurrentInstance.SessionManager.SessionActivated += EventStatus;
            m_server.CurrentInstance.SessionManager.SessionClosing += EventStatus;
            m_server.CurrentInstance.SessionManager.SessionCreated += EventStatus;
        }

        private void EventStatus(Session session, SessionEventReason reason)
        {
            m_lastEventTime = DateTime.UtcNow;
            PrintSessionStatus(session, reason.ToString());
        }

        private void PrintSessionStatus(Session session, string reason, bool lastContact = false)
        {
            lock (session.DiagnosticsLock)
            {
                StringBuilder item = new StringBuilder();
                item.Append(string.Format("{0,9}:{1,20}:", reason, session.SessionDiagnostics.SessionName));
                if (lastContact)
                {
                    item.Append(string.Format("Last Event:{0:HH:mm:ss}", session.SessionDiagnostics.ClientLastContactTime.ToLocalTime()));
                }
                else
                {
                    if (session.Identity != null)
                    {
                        item.Append(string.Format(":{0,20}", session.Identity.DisplayName));
                    }
                    item.Append(string.Format(":{0}", session.Id));
                }
                Console.WriteLine(item.ToString());
            }
        }

        private async void StatusThread()
        {
            while (m_server != null)
            {
                if (DateTime.UtcNow - m_lastEventTime > TimeSpan.FromMilliseconds(6000))
                {
                    IList<Session> sessions = m_server.CurrentInstance.SessionManager.GetSessions();
                    for (int ii = 0; ii < sessions.Count; ii++)
                    {
                        Session session = sessions[ii];
                        PrintSessionStatus(session, "-Status-", true);
                    }
                    m_lastEventTime = DateTime.UtcNow;
                }
                await Task.Delay(1000).ConfigureAwait(false);
            }
        }
    }

}
