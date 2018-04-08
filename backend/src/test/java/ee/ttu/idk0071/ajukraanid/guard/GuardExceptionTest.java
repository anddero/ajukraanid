package ee.ttu.idk0071.ajukraanid.guard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuardExceptionTest {
    @Test
    void testConstructor() {
        GuardException e = new GuardException("a message");
        assertEquals("a message", e.getMessage());
    }

    @Test
    void testThrow() {
        assertThrows(GuardException.class, () -> {
            throw new GuardException("error");
        });
    }
}