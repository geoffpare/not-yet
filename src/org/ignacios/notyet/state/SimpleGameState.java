package org.ignacios.notyet.state;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.player.Action;
import org.ignacios.notyet.turn.SimpleTurn;
import org.ignacios.notyet.turn.Turn;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SimpleGameState implements GameState, PrivateGameState {

    private final InitialState initialState;
    private final PeekingIterator<Card> cards;
    private final List<Turn> turns;
    private final Iterator<PlayerId> playerOrder;
    private final Map<PlayerId, SimplePlayerState> players;
    private int chipCount;

    public SimpleGameState(InitialState initialState, List<Card> cards) {
        this.initialState = checkNotNull(initialState);
        this.cards = Iterators.peekingIterator(checkNotNull(cards).iterator());
        this.turns = Lists.newArrayList();
        this.playerOrder = Iterators.cycle(initialState.getPlayerOrder());
        this.players = initializePlayerState(initialState);
        this.chipCount = 0;
    }

    private static Map<PlayerId, SimplePlayerState> initializePlayerState(InitialState initialState) {
        Map<PlayerId, SimplePlayerState> players = Maps.newHashMap();
        for (PlayerId playerId: initialState.getPlayerOrder()) {
            players.put(playerId, new SimplePlayerState(playerId, initialState.getNumberOfChipsPerPlayer(), ImmutableSet.<Card>of()));
        }
        return players;
    }

    // Game State Methods
    @Override
    public InitialState getInitialState() {
        return initialState;
    }

    @Override
    public Optional<Card> getCurrentCard() {
        if (cards.hasNext()) {
            Card card = cards.peek();
            return Optional.of(card);
        }
        return Optional.absent();
    }

    @Override
    public int getCurrentChipCount() {
        return chipCount;
    }

    @Override
    public Optional<PlayerState> getPlayerState(PlayerId playerId) {
        return Optional.<PlayerState>fromNullable(players.get(playerId));
    }

    @Override
    public List<Turn> getTurns() {
        return ImmutableList.copyOf(turns);
    }

    // Private Game State Methods
    @Override
    public Iterator<PlayerId> getPlayerOrder() {
        return playerOrder;
    }

    private void addTurn(SimpleTurn turn) {
//        System.out.println(turn);
        turns.add(turn);
    }

    @Override
    public void playerPlacesChip(PlayerId playerId) {
        checkState(players.containsKey(playerId));
        checkState(cards.hasNext());

        addTurn(new SimpleTurn(playerId, cards.peek(), getCurrentChipCount(), Action.PLACE_CHIP));

        SimplePlayerState player = players.get(playerId);
        player.placeChip();
        chipCount += 1;
    }

    @Override
    public void playerTakesCard(PlayerId playerId) {
        checkState(players.containsKey(playerId));
        checkState(cards.hasNext());
        Card cardToTake = cards.next();
        addTurn(new SimpleTurn(playerId, cardToTake, getCurrentChipCount(), Action.TAKE_CARD));

        SimplePlayerState player = players.get(playerId);
        player.takeCard(cardToTake, getCurrentChipCount());
        chipCount = 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cards", cards)
                .add("chipCount", chipCount)
                .add("players", players)
                .toString();
    }

}
