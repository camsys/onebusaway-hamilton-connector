package org.onebusaway.realtime.hamilton.connector.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.onebusaway.realtime.hamilton.connector.model.PositionReport;
import org.onebusaway.realtime.hamilton.connector.model.AVLRecord;
import org.onebusaway.realtime.hamilton.connector.model.WayfarerLogon;

public class VehicleUpdateServiceImplTest {

//  @Test
  public void testRecieveGPSUpdate() throws Exception {
    VehicleUpdateServiceImpl impl = new VehicleUpdateServiceImpl();
    byte[] msg = "RTCPFCE3018124053060000241000000000022053001-192F01011-411361867,1748403917000106181207827200022580000003000000000000057A;ID=00000001;*".getBytes();
    AVLRecord wfr = impl.recieveGPSUpdate(msg); 
    assertNotNull(wfr);
    assertTrue(wfr instanceof PositionReport);
    
    PositionReport rp = (PositionReport) wfr;
    
    assertEquals(64739, rp.getSequenceNumber());
    assertEquals(1812, rp.getGpsTriggerWeekNumber()); // verified Oct 2014
    assertEquals(405306, rp.getGpsTriggerSecondsIntoWeek()); // Oct 3 NZ time
    assertEquals("53001-", rp.getOperatorId());
    assertEquals("192F", rp.getCellId());
    assertEquals(1, rp.getIgnitionFlag());
    assertEquals(0, rp.getPowerSourceFlag());
    assertEquals(1, rp.getGpsSatellites());
    assertEquals(-411.361867, rp.getLat(), 0.001);
    assertEquals(174.8403917, rp.getLon(), 0.001);
    assertEquals(1812, rp.getGpsFixWeekNumber());
    assertEquals(78272, rp.getGpsFixSecondsIntoWeek());

    assertEquals(2, rp.getSpeed());
    assertEquals(258, rp.getHeading());
    assertEquals(0, rp.getMaxSpeed());
    assertEquals(0.0, rp.getDistanceTravelled(), 0.001);
  }

  @Test
  public void testReceiveWayfarerLogOnOff() {
    VehicleUpdateServiceImpl impl = new VehicleUpdateServiceImpl();

    byte[] msg = {
         (byte)0x02 // STX
        , (byte)0x51 // device address
        , (byte)0x1C // length
        , (byte)0xC2 // command LOGON
        , (byte)0x01 , (byte)0x23  , (byte)0x45 , (byte)0x67 // driver number 1234567
        , (byte)0x65 , (byte)0x43 , (byte)0x21 // module 654321
        , (byte)0x02 , (byte)0x01  , (byte)0x05 // running board 20105
        , (byte)0x00 , (byte)0x12 , (byte)0x34 // duty 1234
        , (byte)0x31 , (byte)0x32 , (byte)0x41 , (byte)0x20 // service 12A
        , (byte)0x01 , (byte)0x52 // journey 152
        , (byte)0x00 // direction outward
        , (byte)0x30 // depot 30
        , (byte)0x10 , (byte)0x27 // time 10:27
        , (byte)0x14 // states
        , (byte)0x00 , (byte)0x00 // CRC low/high
    };
    
    AVLRecord wfr = impl.receiveWayfarerLogOnOff(msg); 
    assertNotNull(wfr);
    assertTrue(wfr instanceof WayfarerLogon);
    
    WayfarerLogon wl = (WayfarerLogon) wfr;
    assertEquals("1234567", wl.getDriver());
    assertEquals("654321", wl.getModule());
    assertEquals("20105", wl.getRunning());
    assertEquals("1234", wl.getDuty());
    assertEquals("12A", wl.getService());
    assertEquals("152", wl.getJourney());
    assertEquals(false, wl.isInbound());
    assertEquals("30", wl.getDepot());
    assertEquals("10:27", wl.getDepartureTimeString());
    assertEquals("20", wl.getStates());
  }
}
