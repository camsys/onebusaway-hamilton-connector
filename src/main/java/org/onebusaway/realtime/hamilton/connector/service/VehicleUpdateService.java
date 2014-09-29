package org.onebusaway.realtime.hamilton.connector.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.onebusaway.realtime.hamilton.connector.VehicleMessage;

public interface VehicleUpdateService {

  void receiveTCIP(byte[] buff);
  List<VehicleMessage> getRecentMessages();
  void receiveWayfarerLogOnOff(byte[] byteArray);
  void recieveGPSUpdate(ByteArrayOutputStream lost);
}
