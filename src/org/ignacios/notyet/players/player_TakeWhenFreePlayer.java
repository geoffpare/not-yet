package org.ignacios.notyet.player;


import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.card.CardRun;
import org.ignacios.notyet.state.GameConfig;
import org.ignacios.notyet.state.GameState;
import org.ignacios.notyet.state.PlayerState;
import org.ignacios.notyet.turn.Turn;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Naive player that makes some common sense choices.
 */
public class TakeWhenFreePlayer implements Player {

    @Override
    public Action playTurn(GameConfig config, GameState gameState, PlayerState playerState, List<Turn> turns) {
        // Check for free cards
        checkState(gameState.getCurrentCard().isPresent());
        Card card = gameState.getCurrentCard().get();
        if (card.getValue() == gameState.getCurrentChipCount() + 1) {
//            System.out.println(String.format("free card, taking %s", card));
            return Action.TAKE_CARD;
        }

        for (CardRun run: playerState.getCardRuns()) {
            if (run.doesCardPrependRun(card) || run.doesCardExtendRun(card)) {
//                System.out.println(String.format("card extends a run, taking %s", card));
                return Action.TAKE_CARD;
            }
        }

        return Action.PLACE_CHIP;
    }
}
