package org.ignacios.notyet.card;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleCardTest {

    @Test
    public void testIsBeforeTrue() {
        assertTrue(CardUtils.one.isBefore(CardUtils.two));
    }

    @Test
    public void testIsBeforeFalse() {
        assertFalse(CardUtils.two.isBefore(CardUtils.one));
    }

    @Test
    public void testIsWayBefore() {
        assertFalse(CardUtils.one.isBefore(CardUtils.three));
    }

    @Test
    public void testIsAfterTrue() {
        assertTrue(CardUtils.two.isAfter(CardUtils.one));
    }

    @Test
    public void testIsAfterFalse() {
        assertFalse(CardUtils.one.isAfter(CardUtils.two));
    }

    @Test
    public void testIsWayAfter() {
        assertFalse(CardUtils.three.isAfter(CardUtils.one));
    }
}
