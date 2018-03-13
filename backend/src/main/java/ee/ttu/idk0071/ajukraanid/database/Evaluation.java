package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Evaluations;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;
import lombok.Setter;

public class Evaluation extends Entry {
    // inaccessible
    private final Question question;
    private Evaluations evaluation;
    // accessible
    @Getter private final Player giver;
    @Getter private final Player target; // TODO Fix to answer

    /**
     * From existing database entry.
     */
    Evaluation(Question question, Evaluations evaluation) {
        this.question = question;
        question.getEvaluations().add(this);
        this.evaluation = evaluation;
        giver = question.getGame().getPlayers().stream().filter(player -> player.getPlayer().getPlayer_id().equals
                (evaluation.getGiverId())).findAny().orElseThrow(() -> new RuntimeException("Player with id " +
                evaluation.getGiverId() + " not found, database is broken ;(")); // TODO OMG
        target = question.getGame().getPlayers().stream().filter(player -> player.getPlayer().getPlayer_id().equals
                (evaluation.getTargetId())).findAny().orElseThrow(() -> new RuntimeException("Player with id " +
                evaluation.getTargetId() + " not found, database is broken ;(")); // TODO FIX!!
    }

    /**
     * Completely new.
     */
    public Evaluation(Question question, Player giver, Player target) {
        this.question = question;
        question.getEvaluations().add(this);
        evaluation = new Evaluations(question.getQuestion().getId(), giver.getPlayer().getPlayer_id(),
                target.getPlayer().getPlayer_id());
        this.giver = giver;
        this.target = target;
        target.setPoints(target.getPoints() + 100); // TODO temp stuff
        evaluation = getDatabase().getEvaluationsRepository().save(evaluation);
    }

    @Override
    protected Database getDatabase() {
        return question.getDatabase();
    }
}
