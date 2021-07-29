package com.cgi.connect;

class VersionUtil {
  public static String getVersion() {
    try {
      return VersionUtil.class.getPackage().getImplementationVersion();
    } catch (Exception ex) {
      return "1.0.0.0";
    }
  }
}
