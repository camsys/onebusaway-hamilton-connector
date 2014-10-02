package org.onebusaway.realtime.hamilton.connector.model;

public abstract class WayFarerFieldSetter<T extends WayFarerRecord> {
  protected byte[] bytes;
  protected int start;
  protected int end;
  public void setData(byte[] bytes, int start, int end) {
    this.bytes = bytes;
    this.start = start;
    this.end = end;
  }
  public abstract void setField(T record);
  
  public String getStringDataUppercased() {
    return getStringData().toUpperCase();
  }
  
  public String getStringData() {
    if (start > bytes.length) {
      return "";
    }
    if (end > bytes.length) {
      return new String(bytes, start, bytes.length - start).trim();
    }
    return new String(bytes, start, end - start).trim();
  }

  public int getInteger() {
    return Integer.parseInt(getStringData());
  }
  
  public int getIntegerFromHex() {
    return Integer.parseInt(getStringData(), 16);
  }

  public long getLong() {
    return Long.parseLong(getStringData());
  }

  public int getIntegerSafe() {
    String data = getStringData();
    try {
      return Integer.parseInt(data);
    } catch (NumberFormatException e) {
      return -1;
    }
  }
  
  public float getDecimalFixedPoint(int digits) {
    while(bytes[start] == ' ') {
      start += 1;
    }
    if (bytes[start] == '-') {
      digits += 1;
    }
    String beforePoint = new String(bytes, start, digits).trim();
    String afterPoint = new String(bytes, start + digits, (end - start) - digits).trim();
    return Float.parseFloat(beforePoint + "." + afterPoint);
  }

  public boolean getBoolean() {
    while(bytes[start] == ' ') {
      start++;
    }
    return bytes[start] == 'Y';
  }


}
