import java.io.Serializable;
import java.util.ArrayList;

public class GameRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private long gameId;
    private String date;

    private ArrayList<GamePlayerRecord> playerRecords;

    public GameRecord(
            long gameId,
            String date,
            ArrayList<GamePlayerRecord> playerRecords) {

        this.gameId = gameId;
        this.date = date;

        this.playerRecords =
                new ArrayList<GamePlayerRecord>();

        for (int i = 0;
             i < playerRecords.size();
             i++) {

            this.playerRecords.add(
                    new GamePlayerRecord(
                            playerRecords.get(i)));
        }
    }

    public GameRecord(GameRecord other) {

        this(
                other.gameId,
                other.date,
                other.playerRecords);
    }

    public long getGameId() {
        return gameId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<GamePlayerRecord>
            getPlayerRecords() {

        return playerRecords;
    }

    public GamePlayerRecord findPlayerRecord(
            long playerId) {

        for (int i = 0;
             i < playerRecords.size();
             i++) {

            GamePlayerRecord record =
                    playerRecords.get(i);

            if (record.getPlayerId()
                    == playerId) {

                return record;
            }
        }

        return null;
    }

    public long getTotalBuyInsCents() {

        long total = 0;

        for (int i = 0;
             i < playerRecords.size();
             i++) {

            total +=
                    playerRecords
                    .get(i)
                    .getTotalBuyInCents();
        }

        return total;
    }

    public long getTotalFinalAmountsCents() {

        long total = 0;

        for (int i = 0;
             i < playerRecords.size();
             i++) {

            total +=
                    playerRecords
                    .get(i)
                    .getFinalAmountCents();
        }

        return total;
    }

    public boolean isBalanced() {

        return getTotalBuyInsCents()
                == getTotalFinalAmountsCents();
    }

    public void renamePlayerSnapshot(
            long playerId,
            String newName) {

        GamePlayerRecord record =
                findPlayerRecord(playerId);

        if (record != null) {
            record.setPlayerName(newName);
        }
    }
}
