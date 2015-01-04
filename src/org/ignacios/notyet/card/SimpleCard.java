package org.ignacios.notyet.card;

import com.google.common.base.MoreObjects;

public class SimpleCard implements Card {
    private final int value;

    public SimpleCard(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean isBefore(Card card) {
        return card.getValue() - 1 == getValue();
    }

    @Override
    public boolean isAfter(Card card) {
        return card.getValue() + 1 == getValue();
    }

    @Override
    public int compareTo(Card o) {
        if (value < o.getValue()) {
            return -1;
        } else if (value > o.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .toString();
    }
}
