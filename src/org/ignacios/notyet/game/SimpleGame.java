package org.ignacios.notyet.game;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.card.SimpleCard;
import org.ignacios.notyet.player.Action;
import org.ignacios.notyet.player.Player;
import org.ignacios.notyet.state.InitialState;
import org.ignacios.notyet.state.PlayerId;
import org.ignacios.notyet.state.PlayerState;
import org.ignacios.notyet.state.SimpleGameState;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SimpleGame {
    private final InitialState initialState;
    private final Map<PlayerId, Player> bots;
    private SimpleGameState gameState;
    private List<Card> initialCards;

    public SimpleGame(InitialState initialState, Map<PlayerId, Player> bots) {
        this.initialState = checkNotNull(initialState);
        this.bots = checkNotNull(bots);
    }

    public void initialize() {
        // maxCard is inclusive, so we have to +1
        int numCards = 1 + initialState.getMaxCard().getValue() - initialState.getMinCard().getValue();
        List<Card> cards = Lists.newArrayListWithCapacity(numCards);
        for (int i = initialState.getMinCard().getValue(); i <=  initialState.getMaxCard().getValue(); i++) {
            // Allow us to choose Card implementation at runtime
            cards.add(new SimpleCard(i));
        }

        Collections.shuffle(cards);

        // Remove the cards to randomize the possible runs.
        for (int j = 0; j < initialState.getNumberOfCardsRemoved(); j++) {
            cards.remove(0);
        }

        // Save this, possible for game replay, algorithmic study.
        initialCards = ImmutableList.copyOf(cards);

        for (PlayerId playerId: initialState.getPlayerOrder()) {
            checkState(bots.containsKey(playerId));
        }

        // Create the GameState
        gameState = new SimpleGameState(initialState, cards);
    }

    public void runGame() {
        while (gameState.getCurrentCard().isPresent()) {
            decideNextCard();
        }
    }

    private void decideNextCard() {
        PlayerId playerId;
        Optional<PlayerState> playerState;
        Player bot;
        Action action;

        while (gameState.getPlayerOrder().hasNext()) {
            playerId = gameState.getPlayerOrder().next();
            playerState = gameState.getPlayerState(playerId);
            checkState(playerState.isPresent());

            if (playerState.get().getChipCount() == 0) {
                action = Action.TAKE_CARD;
            } else {
                bot = bots.get(playerId);
                action = bot.playTurn(gameState, playerState.get());
            }

            // Process the action
            switch (action) {
                case PLACE_CHIP:
                    // move the chip
                    gameState.playerPlacesChip(playerId);
                    break;
                case TAKE_CARD:
                    // move the chips
                    // add the card
                    gameState.playerTakesCard(playerId);
                    return;
            }
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
//                .add("initialState", initialState)
//                .add("initialCards", initialCards)
//                .add("bots", bots)
                .add("gameState", gameState)
                .toString();
    }
}
