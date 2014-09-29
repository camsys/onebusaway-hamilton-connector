package org.onebusaway.realtime.hamilton.connector.model;

public class WayFarerFieldDefinition<T extends WayFarerRecord> {
  int length;
  String name;
  WayFarerFieldSetter<T> setter;
  
  public WayFarerFieldDefinition(int lenght, String name, WayFarerFieldSetter<T> setter) {
    this.length = lenght;
    this.name = name;
    this.setter = setter;
  }
}
