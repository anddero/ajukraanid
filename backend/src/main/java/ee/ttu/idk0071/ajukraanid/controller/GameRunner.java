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
        Thread.sleep(25000);
        this.game.setGameState("chooseBestAnswer");

        Thread.sleep(25000);
        this.game.setQuestionNumber(game.getQuestionNumber() + 1);
        System.out.println(this.game.getQuestionNumber());
        this.game.setGameState("awarding");

        Thread.sleep(25000);
        if (questionsLeft > 0) {
            questionsLeft--;
            runRound(); // TODO make iterative
        } else game.setGameState("ended");

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
