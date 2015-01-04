package org.ignacios.notyet.turn;


import com.google.common.base.MoreObjects;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.player.Action;
import org.ignacios.notyet.state.PlayerId;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleTurn implements Turn {

    private final PlayerId playerId;
    private final Card card;
    private final int chipCount;
    private final Action action;

    public SimpleTurn(PlayerId playerId, Card card, int chipCount, Action action) {
        this.playerId = checkNotNull(playerId);
        this.card = checkNotNull(card);
        checkArgument(chipCount >= 0);
        this.chipCount = chipCount;
        this.action = checkNotNull(action);

    }

    @Override
    public PlayerId getPlayerId() {
        return playerId;
    }

    @Override
    public Card getCard() {
        return card;
    }

    @Override
    public int getChipCount() {
        return chipCount;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("playerId", playerId)
                .add("card", card)
                .add("chipCount", chipCount)
                .add("action", action)
                .toString();
    }
}
