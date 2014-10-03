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
    byte[] msg = "RTCPFCE3018124053060000241000000000022053001-192F01011-411361867+1748403917000106181207827200022580000003000000000000057A;ID=00000001;*".getBytes();
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

    String msg = ""
        + "\u0002" // STX
        + "\u0051" // device address
        + "\u001C" // length
        + "\u00C2" + ""// command LOGON
        + 0x01 + "" + 0x23 + "" + 0x45 + "" + 0x67 // driver number 1234567
        + "\u0065\u0043\u0021" // module 654321
        + "\u0002\u0001\u0005" // running board 20105
        + "\u0000\u0012\u0024" // duty 1234
        + "\u0032\u0032\u0041\u0020" // service 12A
        + "\u0001\u0052" // journey 152
        + "\u0000" // direction outward
        + "\u0030" // depot 30
        + "\u0010\u0027" // time 10:27
        + "\u0014" // states
        + "LH" // CRC low/high
        ;
    
    AVLRecord wfr = impl.receiveWayfarerLogOnOff(msg.getBytes()); 
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
