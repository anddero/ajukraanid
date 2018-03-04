package ee.ttu.idk0071.ajukraanid.database.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Table<Enterery extends Entry> {
    private final List<Enterery> entries = new ArrayList<>();

    public Stream<Enterery> stream() {
        return entries.stream();
    }

    public void forEach(Consumer<? super Enterery> action) {
        entries.forEach(action);
    }

    public boolean add(Enterery entry) {
        entry.setTable(this);
        return entries.add(entry);
    }

    public boolean remove(Enterery entry) {
        return entries.remove(entry);
    }

    public Enterery remove(int index) {
        return entries.remove(index);
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}
