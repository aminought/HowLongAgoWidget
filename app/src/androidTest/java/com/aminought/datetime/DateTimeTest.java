package com.aminought.datetime;

import junit.framework.TestCase;

public class DateTimeTest extends TestCase {

    public void testDiff() throws Exception {
        DateTime dt1 = new DateTime(2015, 4, 29, 17, 38);
        DateTime dt2 = new DateTime(2015, 4, 29, 20, 19);
        DateTime resActual1 = dt1.diff(dt2);
        DateTime resActual2 = dt2.diff(dt1);
        assertEquals(resActual1.getMinute(), resActual2.getMinute());
        assertEquals(resActual1.getHour(), resActual2.getHour());
        assertEquals(resActual1.getDay(), resActual2.getDay());
        assertEquals(resActual1.getMonth(), resActual2.getMonth());
        assertEquals(resActual1.getYear(), resActual2.getYear());
    }
}