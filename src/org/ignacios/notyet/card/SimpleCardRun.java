package org.ignacios.notyet.card;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleCardRun implements CardRun, PrivateCardRun {

    private final TreeSet<Card> cards;

    public SimpleCardRun(Card initialCard) {
        cards = Sets.newTreeSet();
        addCard(initialCard);
    }

    // Card Run Methods
    @Override
    public boolean doesCardPrependRun(Card card) {
        checkNotNull(card);

        return cards.isEmpty() || cards.first().isAfter(card);
    }

    @Override
    public boolean doesCardExtendRun(Card card) {
        checkNotNull(card);

        return cards.isEmpty() || cards.last().isBefore(card);
    }

    @Override
    public int distanceFromStart(Card card) {
        checkNotNull(card);

        if (cards.isEmpty() || cards.contains(card)) {
            return 0;
        }

        // card is less than the head of the run
        if (card.compareTo(cards.first()) < 0) {
            return cards.first().getValue() - card.getValue();
        }

        // card is greater than the head, but not part of the run
        return -1;
    }

    @Override
    public int distanceFromEnd(Card card) {
        checkNotNull(card);

        if (cards.isEmpty() || cards.contains(card)) {
            return 0;
        }

        // card is greater than the tail of the run
        if (card.compareTo(cards.first()) > 0) {
            return card.getValue() - cards.first().getValue();
        }

        // card is less than the tail but not part of the run
        return -1;
    }

    @Override
    public List<Card> getCards() {
        return ImmutableList.<Card>copyOf(cards);
    }

    @Override
    public int getValue() {
        return cards.first().getValue();
    }

    // Private Card Run Methods
    @Override
    public void addCard(Card card) {
        checkArgument(doesCardPrependRun(card) || doesCardExtendRun(card));

        cards.add(card);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
//                .add("cards", cards)
                .add("value", getValue())
                .add("first", cards.first())
                .add("last", cards.last())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cards);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final SimpleCardRun other = (SimpleCardRun) obj;
        return Objects.equal(this.cards, other.cards);
    }
}
