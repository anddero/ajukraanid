package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.config.GameConfig;
import ee.ttu.idk0071.ajukraanid.database.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class GameRunner implements Runnable {
    private static final int MILLISECONDS_IN_SECOND = 1_000;
    private static final Logger log = LoggerFactory.getLogger(GameRunner.class);

    private final GameConfig config;
    private final Game game;
    private LocalDateTime countdownStart;
    private int totalCountdown;

    GameRunner(GameConfig config, Game game) {
        this.config = config;
        this.game = game;
        countdownStart = null;
        totalCountdown = 0;
    }

    private void runRound() throws InterruptedException {
        game.setGameState(Game.State.ANSWERING);
        synchronized (this) {
            totalCountdown = config.getAnsweringTimeSeconds();
            countdownStart = LocalDateTime.now();
        }
        wait(totalCountdown * MILLISECONDS_IN_SECOND);

        game.setGameState(Game.State.GRADING);
        synchronized (this) {
            totalCountdown = config.getGradingTimeSeconds();
            countdownStart = LocalDateTime.now();
        }
        wait(totalCountdown * MILLISECONDS_IN_SECOND);

        game.setGameState(Game.State.RESULTS);
        synchronized (this) {
            totalCountdown = config.getResultsTimeSeconds();
            countdownStart = LocalDateTime.now();
        }
        wait(totalCountdown * MILLISECONDS_IN_SECOND);
    }

    @Override
    public void run() {
        try {
            for (int i = game.getQuestionNumber(); i != game.getQuestions().size(); ++i) {
                game.setQuestionNumber(i);
                runRound();
            }
            game.setGameState(Game.State.INACTIVE);
            synchronized (this) {
                totalCountdown = 0;
                countdownStart = null;
            }
        } catch (InterruptedException e) {
            log.error("Unexpected interruption", e);
        }
    }

    synchronized int getTimeLeftSeconds() {
        if (countdownStart == null) {
            return 0;
        }
        int timeLeftMillis = totalCountdown * MILLISECONDS_IN_SECOND
                - (int) countdownStart.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        if (timeLeftMillis < 0) {
            return 0;
        } else {
            return (timeLeftMillis + 500) / MILLISECONDS_IN_SECOND; // To round to the nearest second.
        }
    }
}
