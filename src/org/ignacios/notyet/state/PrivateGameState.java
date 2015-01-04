package org.ignacios.notyet.state;

import java.util.Iterator;

/**
 * Private methods for the {@link GameState} interface.
 */
public interface PrivateGameState {

    /**
     * @return the player to make the decision
     */
    Iterator<PlayerId> getPlayerOrder();

    /**
     * Mutates the game state to account for a player placing a chip.
     * @param playerId the player that placed the chip.
     */
    void playerPlacesChip(PlayerId playerId);

    /**
     * Mutates the game state to account for a player taking the card.
     * @param playerId the player that took the card.
     */
    void playerTakesCard(PlayerId playerId);
}
