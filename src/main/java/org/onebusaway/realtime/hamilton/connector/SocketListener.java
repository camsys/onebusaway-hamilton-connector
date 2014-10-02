package org.onebusaway.realtime.hamilton.connector;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

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
              
              InputStream inputStream = new BufferedInputStream(client.getInputStream());
              inputStream.mark(1);
              int read = inputStream.read();
              inputStream.reset();
              while (read > 0) {
                boolean found = findBoundary(inputStream, 2);
                if (found) {
                  boolean success = updateService.dispatch(inputStream);
                  while (success) {
                    updateService.dispatch(inputStream);
                  }
                } 
                inputStream.mark(1);
                read = inputStream.read();
                inputStream.reset();
              }
             client.close();
            } catch (Exception e) {
              _log.error("issue reading from socket: " + e, e);
            } finally {
              if (client != null) {
                // tell the client we can't continue
                try {
                  client.close();
                } catch (IOException e) {
                  // bury
                }
              }
            }
          }
        }
      }
      if (serverSocket != null) 
      {
        try {
          _log.info("closing socket on port " + port);
          serverSocket.close();
        } catch (IOException e) {
          // bury
        }
      }
    }
    private boolean findBoundary(InputStream inputStream, int sentinel) throws Exception {
      _log.info("looking for sentinel");
      inputStream.mark(1);
      int read = inputStream.read();
      ByteArrayOutputStream update = new ByteArrayOutputStream();
      // do we have data ready?
      while (read >= 0) {
        // align on boundry
        while (read != sentinel) {
          inputStream.mark(1);
          read = inputStream.read(); // discard
          if (read != sentinel) {
            update.write(read);
          } else {
            inputStream.reset();
            return true;
          }
        }
        // our very first read was on a boundary
        if (read == 2) {
          inputStream.reset();
          return true;
        }
      }
      _log.info("discarded " + new String(update.toByteArray()));
      return false;
    }

  }


}
