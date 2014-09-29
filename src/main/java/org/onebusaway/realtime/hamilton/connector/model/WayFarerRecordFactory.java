package org.onebusaway.realtime.hamilton.connector.model;

public abstract class WayFarerRecordFactory<T extends WayFarerRecord> {

  public abstract WayFarerFieldDefinition<T>[] getFields();

  public abstract T createEmptyRecord();
  
  public void createRecord(byte[] bytes, int start , int end) {
    T record = createEmptyRecord();
    for (WayFarerFieldDefinition<T> f : getFields()) {
      if (f.setter != null) {
        f.setter.setData(bytes, start, end);
        f.setter.setField(record);
      }
    }
  }
  
  
}
