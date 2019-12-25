package com.cmlanche;

import com.cmlanche.core.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will startTask on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testTimeDistance() {
        int sec = 23 * 1000;
        int min = 12 * 60 * 1000;
        int hour = 7 * 60 * 60 * 1000;
        int day = 12 * 24 * 60 * 60 * 1000;
        System.out.println(Utils.getTimeDescription(sec));
        System.out.println(Utils.getTimeDescription(min));
        System.out.println(Utils.getTimeDescription(hour));
        System.out.println(Utils.getTimeDescription(day));
    }
}