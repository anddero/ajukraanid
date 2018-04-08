package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameRunner implements Runnable {
    private static final int ANSWERING_TIME_MS = 25_000;
    private static final int GRADING_TIME_MS = 25_000;
    private static final int RESULTS_TIME_MS = 10_000;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Game game;

    GameRunner(Game game) {
        this.game = game;
    }

    private void runRound() throws InterruptedException {
        this.game.setGameState(Game.State.ANSWERING);
        Thread.sleep(ANSWERING_TIME_MS);

        this.game.setGameState(Game.State.GRADING);
        Thread.sleep(GRADING_TIME_MS);

        this.game.setGameState(Game.State.RESULTS);
        Thread.sleep(RESULTS_TIME_MS);
    }

    @Override
    public void run() {
        try {
            for (int i = game.getQuestionNumber(); i != game.getQuestions().size(); ++i) {
                game.setQuestionNumber(i);
                runRound();
            }
            game.setGameState(Game.State.INACTIVE);
        } catch (InterruptedException e) {
            log.error("Unexpected interruption", e);
        }
    }
}
