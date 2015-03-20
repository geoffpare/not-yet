package org.ignacios.notyet.player;

import org.ignacios.notyet.state.GameConfig;
import org.ignacios.notyet.state.GameState;
import org.ignacios.notyet.state.PlayerState;
import org.ignacios.notyet.turn.Turn;

import java.util.List;

/**
 * Naively pessimistic player that always chooses to place a chip
 */
public class NaivePessimisticPlayer implements Player {

    @Override
    public Action playTurn(GameConfig config, GameState gameState, PlayerState playerState, List<Turn> turns) {
        return Action.PLACE_CHIP;
    }
}
