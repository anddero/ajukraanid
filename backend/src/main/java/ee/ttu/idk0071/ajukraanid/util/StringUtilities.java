package ee.ttu.idk0071.ajukraanid.util;
//
public abstract class StringUtilities {
    private static final int SINGLE_INDENT_SIZE_SPACES = 4;

    public static void repeat(String substring, int amount, StringBuilder stringBuilder) {
        if (amount < 0) {
            throw new IllegalArgumentException("Repeat amount must not be negative");
        }
        for (int i = 0; i != amount; ++i) {
            stringBuilder.append(substring);
        }
    }

    public static void addIndent(int size, StringBuilder stringBuilder) {
        if (size < 0) {
            throw new IllegalArgumentException("Indent size must not be negative");
        }
        repeat(" ", size * SINGLE_INDENT_SIZE_SPACES, stringBuilder);
    }
}
