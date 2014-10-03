package org.onebusaway.realtime.hamilton.connector.model;

public class AVLFieldDefinition<T extends AVLRecord> {
  int length;
  String name;
  AVLRecordFieldSetter<T> setter;
  
  public AVLFieldDefinition(int length, String name, AVLRecordFieldSetter<T> setter) {
    this.length = length;
    this.name = name;
    this.setter = setter;
  }
}
