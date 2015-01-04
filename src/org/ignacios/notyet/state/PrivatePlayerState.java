package org.ignacios.notyet.state;


import org.ignacios.notyet.card.Card;

/**
 * Private methods for the {@link PlayerState} interface
 */
public interface PrivatePlayerState {

    /**
     * Mutates the player's state to account for placing a chip.
     */
    void placeChip();

    /**
     * Mutates the player's state to account for taking a card.
     * @param card the card to take
     * @param chips the number of chips also taken
     */
    void takeCard(Card card, int chips);
}
