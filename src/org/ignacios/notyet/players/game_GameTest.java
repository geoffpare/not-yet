package org.ignacios.notyet.game;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.ignacios.notyet.card.Card;
import org.ignacios.notyet.card.SimpleCard;
import org.ignacios.notyet.player.NaivePessimisticPlayer;
import org.ignacios.notyet.player.Player;
import org.ignacios.notyet.player.TakeWhenFreePlayer;
import org.ignacios.notyet.player.TakeWhenRatioPlayer;
import org.ignacios.notyet.state.GameConfig;
import org.ignacios.notyet.state.GameState;
import org.ignacios.notyet.state.PlayerId;
import org.ignacios.notyet.state.SimplePlayerId;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class GameTest {

    final PlayerId dave = new SimplePlayerId("dave");
    final PlayerId geoff = new SimplePlayerId("geoff");
    final PlayerId jay = new SimplePlayerId("jay");
    final PlayerId eli = new SimplePlayerId("eli");
    final Player naivePessimisticPlayer = new NaivePessimisticPlayer();
    final Player takeWhenFreePlayer = new TakeWhenFreePlayer();
    final Player takeWhenHalfPlayer = new TakeWhenRatioPlayer(0.5f);
    final Player takeWhenSixtyPlayer = new TakeWhenRatioPlayer(0.55f);

    private GameConfig createGameConfig(final List<PlayerId> playerOrder) {
        return new GameConfig() {

            @Override
            public Card getMinCard() {
                return new SimpleCard(3);
            }

            @Override
            public Card getMaxCard() {
                return new SimpleCard(35);
            }

            @Override
            public int getNumberOfCardsRemoved() {
                return 9;
            }

            @Override
            public int getNumberOfChipsPerPlayer() {
                return 11;
            }

            @Override
            public List<PlayerId> getPlayerOrder() {
                return playerOrder;
            }
        };
    }

    private void runGame(GameConfig config, SimpleGame game, Map<PlayerId, Integer> scores) {
        checkNotNull(game);
        checkNotNull(scores);

        game.runGame();
        GameState gameState = game.getGameState();
        for (PlayerId playerId: config.getPlayerOrder()) {
            checkState(gameState.getPlayerState(playerId).isPresent());
            scores.put(playerId, scores.getOrDefault(playerId, 0) + gameState.getPlayerState(playerId).get().getScore());
        }
    }

    private void runGamesWithPlayerOrder(List<PlayerId> playerOrder, Map<PlayerId, Player> bots, int numGames) {
        System.out.println(String.format("After %d games, player order: %s", numGames, playerOrder));
        Map<PlayerId, Integer> scores = Maps.newHashMapWithExpectedSize(playerOrder.size());
        GameConfig config = createGameConfig(playerOrder);
        SimpleGame game = new SimpleGame(config, bots);
        for (int i = 0; i < numGames; i++) {
            runGame(config, game, scores);
        }

        Optional<PlayerId> lowestPlayerId = Optional.absent();
        Optional<Integer> lowestScore = Optional.absent();
        for (Map.Entry<PlayerId, Integer> score: scores.entrySet()) {
            if (!lowestScore.isPresent() || lowestScore.get() > score.getValue()) {
                lowestPlayerId = Optional.of(score.getKey());
                lowestScore = Optional.of(score.getValue());
            }
            System.out.println(String.format("%s: %d", score.getKey(), score.getValue()));
        }
        System.out.println(String.format("WINNER:  %s - %s: %s", bots.get(lowestPlayerId.get()).getClass(), lowestPlayerId, lowestScore));
    }

    @Test
    public void testTakeWhenFree() {
        final Map<PlayerId, Player> bots = ImmutableMap.of(dave, takeWhenFreePlayer,
                geoff, naivePessimisticPlayer,
                jay, naivePessimisticPlayer,
                eli, naivePessimisticPlayer);

        int numGames = 10000;
        List<PlayerId> playerOrder = ImmutableList.of(dave, geoff, jay, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, dave, jay, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, jay, dave, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, jay, eli, dave);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
    }

    @Test
    public void testTakeWhenHalf() {
        final Map<PlayerId, Player> bots = ImmutableMap.of(dave, takeWhenHalfPlayer,
                geoff, naivePessimisticPlayer,
                jay, naivePessimisticPlayer,
                eli, naivePessimisticPlayer);

        int numGames = 10000;
        List<PlayerId> playerOrder = ImmutableList.of(dave, geoff, jay, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, dave, jay, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, jay, dave, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, jay, eli, dave);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
    }

    @Test
    public void testTakeWhenSixty() {
        final Map<PlayerId, Player> bots = ImmutableMap.of(dave, takeWhenSixtyPlayer,
                geoff, takeWhenHalfPlayer,
                jay, takeWhenHalfPlayer,
                eli, takeWhenHalfPlayer);

        int numGames = 10000;
        List<PlayerId> playerOrder = ImmutableList.of(dave, geoff, jay, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, dave, jay, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, jay, dave, eli);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
        playerOrder = ImmutableList.of(geoff, jay, eli, dave);
        runGamesWithPlayerOrder(playerOrder, bots, numGames);
    }
}
