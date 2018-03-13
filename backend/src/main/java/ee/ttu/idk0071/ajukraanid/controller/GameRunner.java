package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.Game;

public class GameRunner implements Runnable {

    private final Game game;
    private int questionsLeft = 3;

    GameRunner(Game game) {
        this.game = game;
    }

    void runRound() throws InterruptedException {
        this.game.setGameState("question");
        Thread.sleep(30000);
        this.game.setGameState("chooseBestAnswer");
        Thread.sleep(30000);
        this.game.setGameState("awarding");
        if (questionsLeft > 0) {
            questionsLeft--;
            runRound();
        }
        Thread.sleep(30000);
    }

    @Override
    public void run() {
        try {
            runRound();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
