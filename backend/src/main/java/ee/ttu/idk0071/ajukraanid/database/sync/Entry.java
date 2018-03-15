package ee.ttu.idk0071.ajukraanid.database.sync;

import ee.ttu.idk0071.ajukraanid.database.Database;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Entry {
    protected abstract Database getDatabase();
    protected abstract void appendTo(StringBuilder stringBuilder, int indentSize);
}
