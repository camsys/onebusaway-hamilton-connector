package org.onebusaway.realtime.hamilton.connector.service;

import java.util.List;

import org.onebusaway.realtime.hamilton.connector.VehicleMessage;

public interface VehicleUpdateService {

  void receiveTCIP(byte[] buff);
  List<VehicleMessage> getRecentMessages();
}
