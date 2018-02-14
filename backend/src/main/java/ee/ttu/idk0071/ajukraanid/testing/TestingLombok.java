package ee.ttu.idk0071.ajukraanid.testing;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class TestingLombok {

    @Setter @Getter private String a;

    private final int lombokTest1;

    public TestingLombok(String a, int lombokTest1) {
        this.lombokTest1 = lombokTest1;
        this.a = a;

    }
}
