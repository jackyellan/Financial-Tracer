import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    public Player(long id, String name) {

        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesPlayed(
            ArrayList<GameRecord> games) {

        int count = 0;

        for (int i = 0; i < games.size(); i++) {

            if (games.get(i).findPlayerRecord(id) != null) {
                count++;
            }
        }

        return count;
    }

    public long getTotalBuyInsCents(
            ArrayList<GameRecord> games) {

        long total = 0;

        for (int i = 0; i < games.size(); i++) {

            GamePlayerRecord record =
                    games.get(i).findPlayerRecord(id);

            if (record != null) {
                total += record.getTotalBuyInCents();
            }
        }

        return total;
    }

    public long getTotalCashOutsCents(
            ArrayList<GameRecord> games) {

        long total = 0;

        for (int i = 0; i < games.size(); i++) {

            GamePlayerRecord record =
                    games.get(i).findPlayerRecord(id);

            if (record != null) {
                total += record.getFinalAmountCents();
            }
        }

        return total;
    }

    public long getTotalProfitCents(
            ArrayList<GameRecord> games) {

        long total = 0;

        for (int i = 0; i < games.size(); i++) {

            GamePlayerRecord record =
                    games.get(i).findPlayerRecord(id);

            if (record != null) {
                total += record.getProfitLossCents();
            }
        }

        return total;
    }

    public long getAverageResultCents(
            ArrayList<GameRecord> games) {

        int gamesPlayed = getGamesPlayed(games);

        if (gamesPlayed == 0) {
            return 0;
        }

        return Math.round(
                (double) getTotalProfitCents(games)
                / gamesPlayed);
    }

    public long getBiggestWinCents(
            ArrayList<GameRecord> games) {

        long biggestWin = 0;

        for (int i = 0; i < games.size(); i++) {

            GamePlayerRecord record =
                    games.get(i).findPlayerRecord(id);

            if (record != null
                    && record.getProfitLossCents() > biggestWin) {

                biggestWin =
                        record.getProfitLossCents();
            }
        }

        return biggestWin;
    }

    public long getBiggestLossCents(
            ArrayList<GameRecord> games) {

        long biggestLoss = 0;

        for (int i = 0; i < games.size(); i++) {

            GamePlayerRecord record =
                    games.get(i).findPlayerRecord(id);

            if (record != null
                    && record.getProfitLossCents() < biggestLoss) {

                biggestLoss =
                        record.getProfitLossCents();
            }
        }

        return biggestLoss;
    }
}
