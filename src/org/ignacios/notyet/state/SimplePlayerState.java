package org.ignacios.notyet.state;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.card.CardRun;
import org.ignacios.notyet.card.SimpleCardRun;

import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SimplePlayerState implements PlayerState, PrivatePlayerState {
    private final PlayerId playerId;
    private final TreeSet<Card> cards;
    private Optional<Set<CardRun>> cardRuns;
    private int chipCount;

    public SimplePlayerState(PlayerId playerId, int chipCount, Set<Card> cards) {
        this.playerId = checkNotNull(playerId);
        this.cards = checkNotNull(Sets.newTreeSet(cards));
        this.chipCount = chipCount;
        this.cardRuns = Optional.absent();
    }

    private Optional<Set<CardRun>> buildCardRuns() {
        ImmutableSet.Builder<CardRun> builder = ImmutableSet.builder();
        Optional<SimpleCardRun> current = Optional.absent();
        // We can iterate this way since cards is sorted
        for (Card card: cards) {
            if (current.isPresent() && current.get().doesCardExtendRun(card)) {
                current.get().addCard(card);
            } else {
                current = Optional.of(new SimpleCardRun(card));
                builder.add(current.get());
            }
        }

        // Done!
        return Optional.<Set<CardRun>>of(builder.build());
    }

    // Player State Methods
    @Override
    public PlayerId getPlayerId() {
        return playerId;
    }

    @Override
    public Set<Card> getCards() {
        return ImmutableSet.copyOf(cards);
    }

    @Override
    public Set<CardRun> getCardRuns() {
        if (!cardRuns.isPresent()) {
            cardRuns = buildCardRuns();
        }
        return cardRuns.get();
    }

    @Override
    public int getChipCount() {
        return chipCount;
    }

    @Override
    public int getScore() {
        int score = 0;
        for (CardRun cardRun: getCardRuns()) {
            score += cardRun.getValue();
        }
        return score - chipCount;
    }

    // Private Player State Methods
    @Override
    public void placeChip() {
        checkState(chipCount >= 1);

        chipCount -= 1;
    }

    @Override
    public void takeCard(Card card, int chips) {
        checkNotNull(card);
        checkArgument(chips >= 0);

        cards.add(card);
        chipCount += chips;
        cardRuns = Optional.absent();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("playerId", playerId)
                .add("score", getScore())
                .add("chipCount", chipCount)
//                .add("cards", cards)
                .add("runs", getCardRuns())
                .toString();

    }
}
