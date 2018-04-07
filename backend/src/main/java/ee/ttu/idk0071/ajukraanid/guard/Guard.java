package ee.ttu.idk0071.ajukraanid.guard;

import ee.ttu.idk0071.ajukraanid.database.Database;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Guard {
    private final Database database;

    public boolean canCreateGame() {
        return false;
    }

    public boolean canJoinGame() {
        return false;
    }

    public boolean canStartGame() {
        return false;
    }

    public boolean canFetchState() {
        return false;
    }

    public boolean canSubmitAnswer() {
        return false;
    }

    public boolean canGivePoints() {
        return false;
    }

    public boolean canGetPoints() {
        return false;
    }

    public boolean canGetAnswers() {
        return false;
    }

    public boolean canRemovePlayer() {
        return false;
    }

    public boolean canGetQuestion() {
        return false;
    }
}
