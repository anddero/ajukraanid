package ee.ttu.idk0071.ajukraanid.database.sync;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Table<Enterery extends Entry> {
    private final List<Enterery> entries = new ArrayList<>();

    public Stream<Enterery> stream() {
        return entries.stream();
    }

    public void forEach(Consumer<? super Enterery> action) {
        entries.forEach(action);
    }

    public boolean add(Enterery entry) {
        return entries.add(entry);
    }

    public boolean remove(Enterery entry) {
        throw new UnsupportedOperationException("Unimplemented");
        // return entries.remove(entry);
    }

    public Enterery remove(int index) {
        throw new UnsupportedOperationException("Unimplemented");
        // return entries.remove(index);
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}
