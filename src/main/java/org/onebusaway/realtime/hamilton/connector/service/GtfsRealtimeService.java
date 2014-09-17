package org.onebusaway.realtime.hamilton.connector.service;

public interface GtfsRealtimeService {

  void start();
  void stop();
  void writeGtfsRealtimeOutput();
}
