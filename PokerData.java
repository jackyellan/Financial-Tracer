import java.io.Serializable;
import java.util.ArrayList;

public class PokerData implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Player> players;

    private ArrayList<GameRecord> gameHistory;

    private Game activeGame;

    private long nextPlayerId;

    public PokerData() {

        players =
                new ArrayList<Player>();

        gameHistory =
                new ArrayList<GameRecord>();

        activeGame = null;

        nextPlayerId = 1;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<GameRecord> getGameHistory() {
        return gameHistory;
    }

    public Game getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(Game activeGame) {
        this.activeGame = activeGame;
    }

    public long getNextPlayerId() {

        long id = nextPlayerId;

        nextPlayerId++;

        return id;
    }
}
