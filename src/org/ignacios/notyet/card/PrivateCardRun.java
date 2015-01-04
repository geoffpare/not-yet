package org.ignacios.notyet.card;

/**
 * Private methods for the {@link CardRun} interface
 */
public interface PrivateCardRun {

    /**
     * Add a card to the run
     * @param card the card to add
     * @throws IllegalArgumentException if the card does not prepend or extend the run
     */
    void addCard(Card card);
}
