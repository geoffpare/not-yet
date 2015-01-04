package org.ignacios.notyet.state;

import com.google.common.collect.ImmutableSet;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.card.CardRun;
import org.ignacios.notyet.card.CardUtils;
import org.ignacios.notyet.card.SimpleCardRun;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimplePlayerStateTest {

    PlayerId playerId;

    @Before
    public void setUp() {
        playerId = new SimplePlayerId(UUID.randomUUID().toString());
    }

    @Test
    public void testInitialState() {
        SimplePlayerState state = new SimplePlayerState(playerId, 5, ImmutableSet.of(CardUtils.two));
        assertEquals(playerId, state.getPlayerId());
        assertEquals(5, state.getChipCount());
        Set<Card> cards = state.getCards();
        assertEquals(1, cards.size());
        assertTrue(cards.contains(CardUtils.two));
    }

    @Test
    public void testBuildCardRuns() {
        SimpleCardRun runA = new SimpleCardRun(CardUtils.two);
        runA.addCard(CardUtils.one);
        SimpleCardRun runB = new SimpleCardRun(CardUtils.four);
        runB.addCard(CardUtils.five);

        SimplePlayerState state = new SimplePlayerState(playerId, 5, ImmutableSet.of(CardUtils.two, CardUtils.one, CardUtils.five, CardUtils.four));
        Set<CardRun> runs = state.getCardRuns();
        assertEquals(2, runs.size());
        assertTrue(runs.contains(runA));
        assertTrue(runs.contains(runB));

        assertEquals(0, state.getScore());  // 0 = CardRun(1) + CardRun(4) - 5 chips.
    }

    @Test
    public void testPlaceChip() {
        SimplePlayerState state = new SimplePlayerState(playerId, 5, ImmutableSet.of(CardUtils.two, CardUtils.one, CardUtils.five, CardUtils.four));
        assertEquals(5, state.getChipCount());
        state.placeChip();
        assertEquals(4, state.getChipCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testPlaceChipNone() {
        SimplePlayerState state = new SimplePlayerState(playerId, 0, ImmutableSet.of(CardUtils.two, CardUtils.one, CardUtils.five, CardUtils.four));
        assertEquals(0, state.getChipCount());
        state.placeChip();
    }

    @Test
    public void testTakeCard() {
        SimplePlayerState state = new SimplePlayerState(playerId, 5, ImmutableSet.<Card>of());
        assertFalse(state.getCards().contains(CardUtils.one));
        assertEquals(5, state.getChipCount());
        state.takeCard(CardUtils.one, 4);
        assertEquals(9, state.getChipCount());
        assertTrue(state.getCards().contains(CardUtils.one));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeCardBadChips() {
        SimplePlayerState state = new SimplePlayerState(playerId, 5, ImmutableSet.<Card>of());
        assertFalse(state.getCards().contains(CardUtils.one));
        assertEquals(5, state.getChipCount());
        state.takeCard(CardUtils.one, -4);
    }
}
