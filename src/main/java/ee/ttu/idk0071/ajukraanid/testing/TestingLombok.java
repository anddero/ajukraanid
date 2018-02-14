package ee.ttu.idk0071.ajukraanid.testing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class TestingLombok {
    @Getter private final String a;
    @Setter private int b;
    private final boolean c;
}
