package ee.ttu.idk0071.ajukraanid.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
public class Question {

    @Getter ArrayList<Answer> answers;



}
