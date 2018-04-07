package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Evaluations;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.util.StringUtilities;
import lombok.Getter;

public final class Evaluation extends Entry {
    // inaccessible
    private final Question question;
    private Evaluations evaluation;
    // accessible
    @Getter private final Player giver;
    @Getter private final Player target;

    /**
     * From existing database entry.
     */
    Evaluation(Question question, Evaluations evaluation) {
        this.question = question;
        question.getEvaluations().add(this);
        this.evaluation = evaluation;
        giver = question.getGame().getPlayers().stream().filter(player -> player.getPlayer().getId().equals
                (evaluation.getGiverId())).findAny().orElseThrow(() -> new RuntimeException("Player with id " +
                evaluation.getGiverId() + " not found, database is broken ;(")); // TODO Review
        target = question.getGame().getPlayers().stream().filter(player -> player.getPlayer().getId().equals
                (evaluation.getTargetId())).findAny().orElseThrow(() -> new RuntimeException("Player with id " +
                evaluation.getTargetId() + " not found, database is broken ;(")); // TODO Review
    }

    /**
     * Completely new.
     */
    public Evaluation(Question question, Player giver, Player target) {
        this.question = question;
        question.getEvaluations().add(this);
        evaluation = new Evaluations(question.getQuestion().getId(), giver.getPlayer().getId(),
                target.getPlayer().getId());
        this.giver = giver;
        this.target = target;
        evaluation = getDatabase().getEvaluationsRepository().save(evaluation);
    }

    @Override
    protected void appendTo(StringBuilder stringBuilder, int indentSize) {
        StringUtilities.addIndent(indentSize, stringBuilder);
        stringBuilder.append(giver.getName()).append(" -> ").append(target.getName()).append("\n");
    }

    @Override
    protected Database getDatabase() {
        return question.getDatabase();
    }
}
