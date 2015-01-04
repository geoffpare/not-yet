package org.ignacios.notyet.card;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleCardRunTest {

    @Test
    public void testDoesCardPrependRunTrue() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.two);
        assertTrue(run.doesCardPrependRun(CardUtils.one));
    }

    @Test
    public void testDoesCardPrependRunFalse() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.three);
        assertFalse(run.doesCardPrependRun(CardUtils.one));
    }

    @Test
    public void testDoesCardExtendRunTrue() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.two);
        assertTrue(run.doesCardExtendRun(CardUtils.three));
    }

    @Test
    public void testDoesCardExtendRunFalse() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.one);
        assertFalse(run.doesCardExtendRun(CardUtils.three));
    }

    @Test
    public void testDistanceFromStart() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.three);
        assertEquals(0, run.distanceFromStart(CardUtils.three));
        assertEquals(2, run.distanceFromStart(CardUtils.one));
        assertEquals(-1, run.distanceFromStart(CardUtils.four));
        assertEquals(-1, run.distanceFromStart(CardUtils.five));
    }

    @Test
    public void testDistanceFromEnd() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.three);
        assertEquals(0, run.distanceFromEnd(CardUtils.three));
        assertEquals(2, run.distanceFromEnd(CardUtils.five));
        assertEquals(-1, run.distanceFromEnd(CardUtils.two));
        assertEquals(-1, run.distanceFromEnd(CardUtils.one));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadAddCard() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.two);
        run.addCard(CardUtils.four);
    }

    @Test
    public void testAddCard() {
        SimpleCardRun run = new SimpleCardRun(CardUtils.three);
        assertEquals(3, run.getValue());
        run.addCard(CardUtils.two);
        assertEquals(2, run.getValue());
        run.addCard(CardUtils.four);
        assertEquals(2, run.getValue());
    }

    @Test
    public void testEquals() {
        SimpleCardRun runA = new SimpleCardRun(CardUtils.three);
        runA.addCard(CardUtils.two);
        runA.addCard(CardUtils.four);

        SimpleCardRun runB = new SimpleCardRun(CardUtils.four);
        runB.addCard(CardUtils.three);
        runB.addCard(CardUtils.two);

        assertEquals(runA, runB);
    }
}
