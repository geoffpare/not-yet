package org.ignacios.notyet.state;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.player.Action;
import org.ignacios.notyet.turn.SimpleTurn;
import org.ignacios.notyet.turn.Turn;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;


public class SimpleGameState implements GameState, PrivateGameState {

    private final InitialState initialState;
    private final List<Card> cardsLeft;
    private final TreeSet<Card> cardsTaken;
    private final Iterator<Card> cardIterator;
    private final List<Turn> turns;
    private final Iterator<PlayerId> playerOrder;
    private final Map<PlayerId, SimplePlayerState> players;
    private Optional<Card> currentCard;
    private int chipCount;

    public SimpleGameState(InitialState initialState, List<Card> cards) {
        this.initialState = checkNotNull(initialState);
        this.cardsLeft = checkNotNull(cards);
        checkArgument(!cards.isEmpty());
        this.cardsTaken = Sets.newTreeSet();
        this.cardIterator = cards.iterator();
        this.turns = Lists.newArrayList();
        this.playerOrder = Iterators.cycle(initialState.getPlayerOrder());
        this.players = initializePlayerState(initialState);
        this.currentCard = Optional.of(cardIterator.next());
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
    public int getNumberOfCardsLeft() {
        return cardsLeft.size();
    }

    @Override
    public Set<Card> getCardsTaken() {
        return ImmutableSet.copyOf(cardsTaken);
    }

    @Override
    public Optional<Card> getCurrentCard() {
        return currentCard;
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
        checkState(currentCard.isPresent());

        addTurn(new SimpleTurn(playerId, currentCard.get(), getCurrentChipCount(), Action.PLACE_CHIP));

        SimplePlayerState player = players.get(playerId);
        player.placeChip();
        chipCount += 1;
    }

    @Override
    public void playerTakesCard(PlayerId playerId) {
        checkState(players.containsKey(playerId));
        checkState(currentCard.isPresent());
        cardIterator.remove();
        cardsTaken.add(currentCard.get());
        addTurn(new SimpleTurn(playerId, currentCard.get(), getCurrentChipCount(), Action.TAKE_CARD));

        SimplePlayerState player = players.get(playerId);
        player.takeCard(currentCard.get(), getCurrentChipCount());
        if (cardIterator.hasNext()) {
            currentCard = Optional.of(cardIterator.next());
        } else {
            currentCard = Optional.absent();
        }
        chipCount = 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cardsLeft", cardsLeft)
                .add("chipCount", chipCount)
                .add("players", players)
                .toString();
    }

}
