package org.onebusaway.realtime.hamilton.connector.model;

public class ReportPOS implements WayFarerRecord {
  
  int sequenceNumber;
  int triggerType;
  int gpsTriggerWeekNumber;
  long gpsTriggerSecondsIntoWeek;
  String operatorId;
  String cellId;
  int ignitionFlag;
  int powerSourceFlag;
  int gpsSatellites;
  double lat;
  double lon;
  int gpsFixWeekNumber;
  long gpsFixSecondsIntoWeek;
  int speed;
  int heading;
  int maxSpeed;
  double distanceTravelled;
  String driverId;
  
  public void setSequenceNumber(int number) {
    this.sequenceNumber = number;
  }

  public void setTriggerType(int integer) {
    this.triggerType = integer;
  }

  public void setGpsTriggerWeekNumber(int integer) {
    this.gpsTriggerWeekNumber = integer;
  }

  public void setGpsTriggerSecondsIntoWeek(long long1) {
    this.gpsTriggerSecondsIntoWeek = long1;
  }

  public void setOperatorId(String stringData) {
    this.operatorId = stringData;
  }

  public void setCellId(String stringData) {
    this.cellId = stringData;
  }

  public void setIgnitionFlag(int integer) {
    this.ignitionFlag = integer;
  }

  public void setPowerSourceFlag(int integer) {
    this.powerSourceFlag = integer;    
  }

  public void setGpsSatellites(int integer) {
    this.gpsSatellites = integer;
  }

  public void setLat(float decimalFixedPoint) {
    this.lat = decimalFixedPoint;
  }
  
  public void setLon(float decimalFixedPoint) {
    this.lon = decimalFixedPoint;
  }

  public void setGpsFixWeekNumber(int integer) {
    this.gpsFixWeekNumber = integer;
  }

  public void setGpsFixSecondsIntoWeek(long long1) {
    this.gpsFixSecondsIntoWeek = long1;
  }

  public void setSpeed(int integer) {
    this.speed = integer;
  }
  
  public void setMaxSpeed(int integer) {
    this.maxSpeed = integer;
  }

  public void setDistanceTravelled(float decimalFixedPoint) {
    this.distanceTravelled = decimalFixedPoint;
  }
  
}
