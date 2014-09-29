package org.onebusaway.realtime.hamilton.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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
              ByteArrayOutputStream baos = null;
              // we can't use a simple copy here because of tcp keep alive
              // look for sentinel?  assume pauses are complete?
              
              int read = client.getInputStream().read();
              // single character reads are a bad idea
              // this data is small however, and we look for a sentinel
              while (read >= 0) {
                // align on boundry
                ByteArrayOutputStream update = null;
                while (read != 2) {
                  // if its not a log on/off message, its a GPS update
                  update = new ByteArrayOutputStream();
                  
                  read = client.getInputStream().read(); // discard
                  //_log.debug("discard=" + read);
                  if (read != 2) {
                    update.write(read);
                  }
                }
                
                updateService.recieveGPSUpdate(update);
                
                read = client.getInputStream().read();
                if (read == 81) {
                  baos = new ByteArrayOutputStream();
                  int len = client.getInputStream().read();
//                  _log.info("found expected boundary, length=" + len);
                  for (int i = 0; i < len; i++) {
                    int c = client.getInputStream().read();
//                    _log.info("r=" + Integer.toHexString(c));
                    baos.write(c);
                  }
                  updateService.receiveWayfarerLogOnOff(baos.toByteArray());
                  //_log.info("received=" + String.format("%x",  baos.toByteArray()));
                  // TODO send this somewhere
                }
                
                read = client.getInputStream().read();
                //_log.info("r=" + Integer.toHexString(read));
              }
              
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
