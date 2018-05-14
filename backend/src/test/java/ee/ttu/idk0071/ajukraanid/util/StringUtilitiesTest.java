package ee.ttu.idk0071.ajukraanid.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilitiesTest {
    @Test
    void testAddZeroIndents() {
        StringBuilder builder = new StringBuilder();
        StringUtilities.addIndent(0, builder);
        assertEquals("", builder.toString());

        builder.append("abc");
        StringUtilities.addIndent(0, builder);
        assertEquals("abc", builder.toString());
    }

    @Test
    void testAddSingleIndent() {
        StringBuilder builder = new StringBuilder();
        StringUtilities.addIndent(1, builder);
        assertEquals("    ", builder.toString());

        builder.append("2");
        StringUtilities.addIndent(1, builder);
        assertEquals("    2    ", builder.toString());
    }

    @Test
    void testAddManyIndents() {
        StringBuilder builder = new StringBuilder();
        StringUtilities.addIndent(3, builder);
        assertEquals("            ", builder.toString());

        builder.append("35i");
        StringUtilities.addIndent(2, builder);
        assertEquals("            35i        ", builder.toString());
    }

    @Test
    void testUsualUseCase() {
        StringBuilder builder = new StringBuilder();
        builder.append("List:\n");
        for (int i = 0; i != 3; ++i) {
            StringUtilities.addIndent(1, builder);
            builder.append(i).append("\n");
        }
        assertEquals("List:\n    0\n    1\n    2\n", builder.toString());
    }
}
