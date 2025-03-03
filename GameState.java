import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<String> deck;
    List<List<String>> players;
    List<String> center;
    String centerCard;
    int currentPlayer;
    int trickNumber;
}
