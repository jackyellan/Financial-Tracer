import java.io.Serializable;
import java.util.ArrayList;

public class PlayerSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private long playerId;
    private String playerName;

    private long initialBuyInCents;

    private ArrayList<Long> rebuysCents;

    private Long finalAmountCents;

    public PlayerSession(
            Player player,
            long initialBuyInCents) {

        this.playerId = player.getId();
        this.playerName = player.getName();

        this.initialBuyInCents =
                initialBuyInCents;

        this.rebuysCents =
                new ArrayList<Long>();

        this.finalAmountCents = null;
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

    public void addRebuy(long amountCents) {
        rebuysCents.add(amountCents);
    }

    public ArrayList<Long> getRebuysCents() {
        return new ArrayList<Long>(rebuysCents);
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

    public void setFinalAmountCents(
            long finalAmountCents) {

        this.finalAmountCents =
                finalAmountCents;
    }

    public Long getFinalAmountCents() {
        return finalAmountCents;
    }

    public boolean hasFinalAmount() {
        return finalAmountCents != null;
    }

    public long getProfitLossCents() {

        if (finalAmountCents == null) {
            return 0;
        }

        return finalAmountCents
                - getTotalBuyInCents();
    }

    public GamePlayerRecord createRecord() {

        if (finalAmountCents == null) {
            return null;
        }

        return new GamePlayerRecord(
                playerId,
                playerName,
                initialBuyInCents,
                rebuysCents,
                finalAmountCents);
    }
}
