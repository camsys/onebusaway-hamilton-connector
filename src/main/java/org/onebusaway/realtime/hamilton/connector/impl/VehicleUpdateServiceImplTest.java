package org.onebusaway.realtime.hamilton.connector.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.onebusaway.realtime.hamilton.connector.model.ReportPOS;
import org.onebusaway.realtime.hamilton.connector.model.WayFarerRecord;

public class VehicleUpdateServiceImplTest {

  @Test
  public void testRecieveGPSUpdate() throws Exception {
    VehicleUpdateServiceImpl impl = new VehicleUpdateServiceImpl();
    byte[] msg = "RTCPFCE3018124053060000241000000000022053001-192F01011-411361867+1748403917000106181207827200022580000003000000000000057A;ID=00000001;*".getBytes();
    WayFarerRecord wfr = impl.recieveGPSUpdate(msg); 
    assertNotNull(wfr);
    assertTrue(wfr instanceof ReportPOS);
    
    ReportPOS rp = (ReportPOS) wfr;
    
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

}
