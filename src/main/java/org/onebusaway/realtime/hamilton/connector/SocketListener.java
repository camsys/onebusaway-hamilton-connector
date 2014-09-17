package org.onebusaway.realtime.hamilton.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.apache.commons.compress.utils.IOUtils;
import org.onebusaway.realtime.hamilton.connector.service.VehicleUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketListener {

  private static final Logger _log = LoggerFactory.getLogger(SocketListener.class);
  
  private int _port = 9999;
  private VehicleUpdateService _updateService;
  public void setVehicleUpdateService(VehicleUpdateService updateService) {
    _updateService = updateService;
    // TOOD remove this
    start();
  }
  
  public void setPort(int port) {
    _port = port;
  }

  
  public SocketListener() {
  }
  
  @PostConstruct
  public void start() {
    Thread t = new Thread(new ListenerThread(_updateService, _port));
    t.start();
  }
  
  
  private static class ListenerThread implements Runnable {
    private ServerSocket serverSocket = null;
    private int port;
    private VehicleUpdateService updateService;
    
    public ListenerThread(VehicleUpdateService updateService, int port) {
      this.updateService = updateService;
      this.port = port;
    }
    
    public void run() {
      Socket client = null;
      while (!Thread.currentThread().isInterrupted()) {
        if (serverSocket == null || serverSocket.isClosed()) {
          try {
            serverSocket = new ServerSocket(port);
            _log.info("listening for TCIP on port " + port);
          } catch (IOException e) {
            _log.error("issue creating socket: " + e, e);
          }
        }
        
        
        if (serverSocket != null) {
          try {
            client = serverSocket.accept();
          } catch (IOException e) {
            _log.error("issue accepting socket: " + e, e);
          }
          
          if (client != null) {
            try {
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
              IOUtils.copy(client.getInputStream(), baos);
              _log.info("socket received: " + new String(baos.toByteArray()));
              updateService.receiveTCIP(baos.toByteArray());
              client.close();
            } catch (Exception e) {
              _log.error("issue reading from socket: " + e, e);
            }
          }
        }
      }
      if (serverSocket != null) {
        try {
          _log.info("closing socket on port " + port);
          serverSocket.close();
        } catch (IOException e) {
          // bury
        }
      }
    }
    
  }
}
