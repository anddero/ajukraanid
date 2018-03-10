package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class Question extends Entry {
    // inaccessible
    private final int id = 0; // private key
    // accessible
    @Getter private final String text = "LEMME SMASH?"; // ganerate this text in questions constructor??????
    // referenced by
    @Getter private final ArrayList<Answer> answers = new ArrayList<>();
    @Getter private final ArrayList<Evaluation> evaluations = new ArrayList<>();

    @Override
    protected Database getDatabase() {
        throw new UnsupportedOperationException("Partially implemented class definition");
    }
}
