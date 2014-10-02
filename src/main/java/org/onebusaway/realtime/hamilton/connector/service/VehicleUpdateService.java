package org.onebusaway.realtime.hamilton.connector.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.onebusaway.realtime.hamilton.connector.VehicleMessage;
import org.onebusaway.realtime.hamilton.connector.model.WayFarerRecord;

public interface VehicleUpdateService {

  void receiveTCIP(byte[] buff);
  List<VehicleMessage> getRecentMessages();
  void receiveWayfarerLogOnOff(byte[] byteArray);
  WayFarerRecord recieveGPSUpdate(byte[] byteArray);
  boolean dispatch(InputStream inputStream) throws Exception;
}
