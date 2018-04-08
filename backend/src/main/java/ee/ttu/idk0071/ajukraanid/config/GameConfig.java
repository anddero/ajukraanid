package ee.ttu.idk0071.ajukraanid.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class GameConfig {
    @Value("${ajukraanid.game.questions-per-game}")
    private int questionsPerGame;
    @Value("${ajukraanid.game.game-timeout-minutes}")
    private int gameTimeoutMinutes;
    @Value("${ajukraanid.game.questions-file-name}")
    private String questionsFile;
    @Value("${ajukraanid.game.min-players}")
    private int MINIMUM_PLAYERS;
    @Value("${ajukraanid.game.max-players}")
    private int MAXIMUM_PLAYERS;
}
