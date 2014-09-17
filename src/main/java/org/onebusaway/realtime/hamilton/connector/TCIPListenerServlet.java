package org.onebusaway.realtime.hamilton.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.onebusaway.realtime.hamilton.connector.service.VehicleUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TCIPListenerServlet extends HttpServlet {
  
  private static final Logger _log = LoggerFactory.getLogger(TCIPListenerServlet.class);
  private static final int CHUNK_SIZE = 4096;
  @Autowired
  private VehicleUpdateService _updateService;
  
  public synchronized void init() throws ServletException {
    
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String unsupported = "Please use HTTP POST for this feature";
    response.getWriter().append(unsupported);
    _log.error("discarding get request=" + request.getParameterMap());
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(CHUNK_SIZE);
      IOUtils.copy(request.getInputStream(), baos);
      _log.error("POST:" + new String(baos.toByteArray()));
      // TODO lookup udpateService from Spring Context
      //_updateService.receiveTCIP(baos.toByteArray());
  }

}
