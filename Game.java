import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    private long gameId;
    private String date;

    private ArrayList<PlayerSession> players;

    public Game() {

        gameId =
                System.currentTimeMillis();

        date =
                LocalDate.now().toString();

        players =
                new ArrayList<PlayerSession>();
    }

    public long getGameId() {
        return gameId;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<PlayerSession> getPlayers() {
        return players;
    }

    public void addPlayer(
            Player player,
            long initialBuyInCents) {

        players.add(
                new PlayerSession(
                        player,
                        initialBuyInCents));
    }

    public boolean containsPlayerId(
            long playerId) {

        return findPlayerSession(playerId)
                != null;
    }

    public PlayerSession findPlayerSession(
            long playerId) {

        for (int i = 0;
             i < players.size();
             i++) {

            if (players.get(i).getPlayerId()
                    == playerId) {

                return players.get(i);
            }
        }

        return null;
    }

    public void renamePlayerSnapshot(
            long playerId,
            String newName) {

        PlayerSession session =
                findPlayerSession(playerId);

        if (session != null) {
            session.setPlayerName(newName);
        }
    }

    public long getTotalBuyInsCents() {

        long total = 0;

        for (int i = 0;
             i < players.size();
             i++) {

            total +=
                    players.get(i)
                    .getTotalBuyInCents();
        }

        return total;
    }

    public boolean allFinalAmountsEntered() {

        for (int i = 0;
             i < players.size();
             i++) {

            if (!players.get(i).hasFinalAmount()) {
                return false;
            }
        }

        return true;
    }

    public long getTotalFinalAmountsCents() {

        long total = 0;

        for (int i = 0;
             i < players.size();
             i++) {

            Long amount =
                    players.get(i)
                    .getFinalAmountCents();

            if (amount != null) {
                total += amount;
            }
        }

        return total;
    }

    public boolean isBalanced() {

        return allFinalAmountsEntered()
                && getTotalBuyInsCents()
                == getTotalFinalAmountsCents();
    }

    public GameRecord createGameRecord() {

        if (!isBalanced()) {
            return null;
        }

        ArrayList<GamePlayerRecord> records =
                new ArrayList<GamePlayerRecord>();

        for (int i = 0;
             i < players.size();
             i++) {

            GamePlayerRecord record =
                    players.get(i)
                    .createRecord();

            records.add(record);
        }

        return new GameRecord(
                gameId,
                date,
                records);
    }
}
