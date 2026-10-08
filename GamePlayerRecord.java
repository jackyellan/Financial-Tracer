import java.io.Serializable;
import java.util.ArrayList;

public class GamePlayerRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private long playerId;
    private String playerName;

    private long initialBuyInCents;

    private ArrayList<Long> rebuysCents;

    private long finalAmountCents;

    public GamePlayerRecord(
            long playerId,
            String playerName,
            long initialBuyInCents,
            ArrayList<Long> rebuysCents,
            long finalAmountCents) {

        this.playerId = playerId;
        this.playerName = playerName;

        this.initialBuyInCents =
                initialBuyInCents;

        this.rebuysCents =
                new ArrayList<Long>(rebuysCents);

        this.finalAmountCents =
                finalAmountCents;
    }

    public GamePlayerRecord(
            GamePlayerRecord other) {

        this(
                other.playerId,
                other.playerName,
                other.initialBuyInCents,
                other.rebuysCents,
                other.finalAmountCents);
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getInitialBuyInCents() {
        return initialBuyInCents;
    }

    public void setInitialBuyInCents(
            long initialBuyInCents) {

        this.initialBuyInCents =
                initialBuyInCents;
    }

    public ArrayList<Long> getRebuysCents() {

        return new ArrayList<Long>(rebuysCents);
    }

    public void setRebuysCents(
            ArrayList<Long> rebuysCents) {

        this.rebuysCents =
                new ArrayList<Long>(rebuysCents);
    }

    public long getTotalRebuysCents() {

        long total = 0;

        for (int i = 0;
             i < rebuysCents.size();
             i++) {

            total += rebuysCents.get(i);
        }

        return total;
    }

    public long getTotalBuyInCents() {

        return initialBuyInCents
                + getTotalRebuysCents();
    }

    public long getFinalAmountCents() {
        return finalAmountCents;
    }

    public void setFinalAmountCents(
            long finalAmountCents) {

        this.finalAmountCents =
                finalAmountCents;
    }

    public long getProfitLossCents() {

        return finalAmountCents
                - getTotalBuyInCents();
    }
}
