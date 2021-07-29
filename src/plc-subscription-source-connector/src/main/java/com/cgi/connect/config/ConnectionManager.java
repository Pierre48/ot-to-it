package com.cgi.connect.config;

import org.apache.plc4x.java.PlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;

public class ConnectionManager {

  /**
   * @param connectionString the connection string to PlcConnection
   * @return PlcConnection
   * @throws PlcConnectionException in case of issue with connection process
   */
  public static PlcConnection getConnection(String connectionString) throws PlcConnectionException {
    return new PlcDriverManager().getConnection(connectionString);
  }
}
