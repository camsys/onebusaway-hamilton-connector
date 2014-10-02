package org.onebusaway.realtime.hamilton.connector.model;

public abstract class WayFarerRecordFactory<T extends WayFarerRecord> {

  public abstract WayFarerFieldDefinition<T>[] getFields();

  public abstract T createEmptyRecord();
  
  public T createRecord(byte[] bytes, int start , int end) {
    T record = createEmptyRecord();
    for (WayFarerFieldDefinition<T> f : getFields()) {
      if (f.setter != null) {
        f.setter.setData(bytes, start, start + f.length);
        f.setter.setField(record);
      }
      start += f.length;
      if (start >= end) {
        break;
      }
    }
    return record;
  }
  
  
}
