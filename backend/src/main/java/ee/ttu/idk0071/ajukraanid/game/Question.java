package ee.ttu.idk0071.ajukraanid.game;

import lombok.Getter;

import java.util.ArrayList;

public class Question {
    @Getter private final ArrayList<Answer> answers = new ArrayList<>();
}
