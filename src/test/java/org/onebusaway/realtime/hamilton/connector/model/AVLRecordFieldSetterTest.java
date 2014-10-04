package org.onebusaway.realtime.hamilton.connector.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AVLRecordFieldSetterTest {

	@Test
	public void test() {
		AVLRecordFieldSetter a = new AVLRecordFieldSetter() {

			@Override
			public void setField(AVLRecord record) {
				// TODO Auto-generated method stub
				
			}
			
		};
		assertEquals("01", a.bcdToString((byte)0x1));
		assertEquals("23", a.bcdToString((byte)0x23));
		assertEquals("45", a.bcdToString((byte)0x45));
		assertEquals("67", a.bcdToString((byte)0x67));
		
		byte[] buff = {(byte)0x1, (byte)0x23, (byte)0x45, (byte)0x67};
		assertEquals("1", a.bcdToString(buff, 0, 1));
		assertEquals("123", a.bcdToString(buff, 0, 2));
		assertEquals("12345", a.bcdToString(buff, 0, 3));
		assertEquals("1234567", a.bcdToString(buff, 0, 4));
		
	}

}
