package org.ignacios.notyet.game;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.card.SimpleCard;
import org.ignacios.notyet.player.Action;
import org.ignacios.notyet.player.Player;
import org.ignacios.notyet.state.GameConfig;
import org.ignacios.notyet.state.PlayerId;
import org.ignacios.notyet.state.PlayerState;
import org.ignacios.notyet.state.SimpleGameState;
import org.ignacios.notyet.turn.SimpleTurn;
import org.ignacios.notyet.turn.Turn;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class SimpleGame {
    private final GameConfig gameConfig;
    private final Map<PlayerId, Player> bots;
    private final List<Turn> turns;
    private SimpleGameState gameState;
    private List<Card> initialCards;

    public SimpleGame(GameConfig gameConfig, Map<PlayerId, Player> bots) {
        this.gameConfig = checkNotNull(gameConfig);
        this.bots = checkNotNull(bots);
        this.turns = Lists.newLinkedList();
    }

    private void initializeGame() {
        // maxCard is inclusive, so we have to +1
        int numCards = 1 + gameConfig.getMaxCard().getValue() - gameConfig.getMinCard().getValue();
        List<Card> cards = Lists.newArrayListWithCapacity(numCards);
        for (int i = gameConfig.getMinCard().getValue(); i <=  gameConfig.getMaxCard().getValue(); i++) {
            // Allow us to choose Card implementation at runtime
            cards.add(new SimpleCard(i));
        }

        Collections.shuffle(cards);

        // Remove the cards to randomize the possible runs.
        for (int j = 0; j < gameConfig.getNumberOfCardsRemoved(); j++) {
            cards.remove(0);
        }

        // Save this, possible for game replay, algorithmic study.
        initialCards = ImmutableList.copyOf(cards);

        for (PlayerId playerId: gameConfig.getPlayerOrder()) {
            checkState(bots.containsKey(playerId));
        }

        // Create the GameState
        gameState = new SimpleGameState(gameConfig, cards);
        turns.clear();
    }

    public SimpleGameState getGameState() {
        return gameState;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void runGame() {
        initializeGame();
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
            checkState(gameState.getCurrentCard().isPresent());

            playerId = gameState.getPlayerOrder().next();
            playerState = gameState.getPlayerState(playerId);
            checkState(playerState.isPresent());

            if (playerState.get().getChipCount() == 0) {
                action = Action.TAKE_CARD;
            } else {
                bot = bots.get(playerId);
                action = bot.playTurn(gameConfig, gameState, playerState.get(), turns);
            }

            // Process the action
            switch (action) {
                case PLACE_CHIP:
                    // move the chip
                    addTurn(new SimpleTurn(playerId, gameState.getCurrentCard().get(), gameState.getCurrentChipCount(), Action.PLACE_CHIP));
                    gameState.playerPlacesChip(playerId);
                    break;
                case TAKE_CARD:
                    // move the chips
                    // add the card
                    addTurn(new SimpleTurn(playerId, gameState.getCurrentCard().get(), gameState.getCurrentChipCount(), Action.TAKE_CARD));
                    gameState.playerTakesCard(playerId);
                    return;
            }
        }
    }

    private void addTurn(SimpleTurn turn) {
        turns.add(turn);
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
