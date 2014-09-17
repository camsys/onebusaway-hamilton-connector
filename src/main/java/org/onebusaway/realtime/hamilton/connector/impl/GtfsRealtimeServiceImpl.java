package org.onebusaway.realtime.hamilton.connector.impl;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


import org.onebusaway.gtfs_realtime.exporter.GtfsRealtimeLibrary;
import org.onebusaway.gtfs_realtime.exporter.GtfsRealtimeMutableProvider;
import org.onebusaway.realtime.hamilton.connector.VehicleMessage;
import org.onebusaway.realtime.hamilton.connector.service.GtfsRealtimeService;
import org.onebusaway.realtime.hamilton.connector.service.VehicleUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.transit.realtime.GtfsRealtime.FeedMessage;

public class GtfsRealtimeServiceImpl implements GtfsRealtimeService {

  private static final Logger _log = LoggerFactory.getLogger(GtfsRealtimeServiceImpl.class);
  private GtfsRealtimeMutableProvider _gtfsRealtimeProvider;
  private ScheduledExecutorService _refreshExecutor;
  private VehicleUpdateService _updateService;
  private int _refreshInterval = 60; // seconds
  
  public void setGtfsRealtimeProvider(GtfsRealtimeMutableProvider p) {
    _gtfsRealtimeProvider = p;
    _log.info("provider injection");
  }
  public void setVehicleUpdateService(VehicleUpdateService vus) {
    _updateService = vus;
    _log.info("vus injection");
    // TODO: temporary hack until PostConstruct is supported properly
    start();
  }
  
  public void GtfsRealtimeService() {
  }
  
  @PostConstruct
  public void start() {
    _log.error("starting GTFS-realtime service");
    _refreshExecutor = Executors.newSingleThreadScheduledExecutor();
    _refreshExecutor.scheduleAtFixedRate(new RefreshTransitData(), 0,
    _refreshInterval, TimeUnit.SECONDS);    
    
  }

  @PreDestroy
  public void stop() {
    _log.info("stopping GTFS-realtime service");
    if (_refreshExecutor != null) {
      _refreshExecutor.shutdownNow();
    }    
    
  }

  public void writeGtfsRealtimeOutput() {
    List<VehicleMessage> vms = _updateService.getRecentMessages();
    
    _gtfsRealtimeProvider.setTripUpdates(buildTripUpdates(vms));
    
  }
  private FeedMessage buildTripUpdates(List<VehicleMessage> vms) {
    FeedMessage.Builder tripUpdates = GtfsRealtimeLibrary.createFeedMessageBuilder();
    for (VehicleMessage vm : vms) {
      _log.info("processing vm=" + vm);
    }
    
    return tripUpdates.build();
  }
  
  private class RefreshTransitData implements Runnable {
    public void run() {
      try {
        _log.info("refreshing vehicles");
        writeGtfsRealtimeOutput();
      } catch (Exception ex) {
        _log.error("Failed to refresh TransitData: " + ex.getMessage());
        _log.error(ex.toString(), ex);
      }
    }
  }
  
}
