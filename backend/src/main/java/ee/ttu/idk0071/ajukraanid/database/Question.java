package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class Question implements Entry {
    // inaccessible
    private final int id; // private key
    // accessible
    @Getter private final Player player; // foreign key
    @Getter private final String text;
    // referenced by
    @Getter private final ArrayList<Answer> answers = new ArrayList<>();

    @Override
    public void setTable(Table table) {
        throw new UnsupportedOperationException("Partially implemented class definition");
    }
}
