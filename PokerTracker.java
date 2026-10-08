import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PokerTracker {

    private PokerData data;

    private Scanner input;

    public PokerTracker() {

        input = new Scanner(System.in);

        data = DataStore.load();
    }

    public void start() {

        if (data.getActiveGame() != null) {

            System.out.println();
            System.out.println(
                    "A unfinished poker game was restored.");
        }

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("------------------------------");
            System.out.println("        POKER TRACKER");
            System.out.println("------------------------------");

            System.out.println("1. Add Player");
            System.out.println("2. View Player Profiles");

            if (data.getActiveGame() == null) {

                System.out.println(
                        "3. Start New Game");
            }

            else {

                System.out.println(
                        "3. Continue Current Game");
            }

            System.out.println("4. View All Games");
            System.out.println("5. Rename Player");
            System.out.println("6. Delete Player Profile");
            System.out.println("7. Exit");

            int choice =
                    readInt(
                            "Choose an option: ",
                            1,
                            7);

            if (choice == 1) {
                addPlayer();
            }

            else if (choice == 2) {
                viewPlayers();
            }

            else if (choice == 3) {

                if (data.getActiveGame() == null) {
                    startGame();
                }

                else {
                    playGame(
                            data.getActiveGame());
                }
            }

            else if (choice == 4) {
                viewAllGames();
            }

            else if (choice == 5) {
                renamePlayer();
            }

            else if (choice == 6) {
                deletePlayer();
            }

            else if (choice == 7) {

                saveData();

                running = false;
            }
        }

        System.out.println("BYE.");
    }

    public void addPlayer() {

        System.out.print(
                "Enter player name: ");

        String name =
                input.nextLine().trim();

        if (name.length() == 0) {

            System.out.println(
                    "Player name cannot be empty.");

            return;
        }

        if (findPlayerByName(name) != null) {

            System.out.println(
                    "A player with that name already exists.");

            return;
        }

        Player player =
                new Player(
                        data.getNextPlayerId(),
                        name);

        data.getPlayers().add(player);

        saveData();

        System.out.println(
                name + " has been added.");
    }

    public void viewPlayers() {

        ArrayList<Player> players =
                data.getPlayers();

        if (players.size() == 0) {

            System.out.println(
                    "No player profiles have been created.");

            return;
        }

        System.out.println();
        System.out.println("------------------------------");
        System.out.println("       PLAYER PROFILES");
        System.out.println("------------------------------");

        printPlayerList();

        System.out.println("0. Back");

        int choice =
                readInt(
                        "Select player: ",
                        0,
                        players.size());

        if (choice == 0) {
            return;
        }

        showPlayerProfile(
                players.get(choice - 1));
    }

    public void showPlayerProfile(
            Player player) {

        ArrayList<GameRecord> games =
                data.getGameHistory();

        System.out.println();
        System.out.println("==============================");
        System.out.println(
                "       " + player.getName());
        System.out.println("==============================");

        System.out.println(
                "Games Played: "
                + player.getGamesPlayed(games));

        System.out.println(
                "Total Buy Ins: "
                + Money.format(
                        player.getTotalBuyInsCents(
                                games)));

        System.out.println(
                "Total Cash Outs: "
                + Money.format(
                        player.getTotalCashOutsCents(
                                games)));

        System.out.println(
                "Overall Profit/Loss: "
                + Money.formatSigned(
                        player.getTotalProfitCents(
                                games)));

        System.out.println(
                "Average Per Game: "
                + Money.formatSigned(
                        player.getAverageResultCents(
                                games)));

        System.out.println(
                "Biggest Win: "
                + Money.formatSigned(
                        player.getBiggestWinCents(
                                games)));

        System.out.println(
                "Biggest Loss: "
                + Money.formatSigned(
                        player.getBiggestLossCents(
                                games)));

        System.out.println();
        System.out.println("------------------------------");
        System.out.println("         GAME HISTORY");
        System.out.println("------------------------------");

        boolean foundGame = false;

        int playerGameNumber = 1;

        for (int i = 0;
             i < games.size();
             i++) {

            GameRecord game =
                    games.get(i);

            GamePlayerRecord record =
                    game.findPlayerRecord(
                            player.getId());

            if (record != null) {

                foundGame = true;

                System.out.println();

                System.out.println(
                        "Game "
                        + playerGameNumber);

                System.out.println(
                        "Date: "
                        + game.getDate());

                System.out.println(
                        "Initial Buy-In: "
                        + Money.format(
                                record.getInitialBuyInCents()));

                System.out.println(
                        "Rebuys: "
                        + Money.format(
                                record.getTotalRebuysCents()));

                System.out.println(
                        "Total Invested: "
                        + Money.format(
                                record.getTotalBuyInCents()));

                System.out.println(
                        "Cash Out: "
                        + Money.format(
                                record.getFinalAmountCents()));

                System.out.println(
                        "Result: "
                        + Money.formatSigned(
                                record.getProfitLossCents()));

                System.out.println(
                        "------------------------------");

                playerGameNumber++;
            }
        }

        if (!foundGame) {

            System.out.println(
                    "No games played yet.");
        }
    }

    public void startGame() {

        ArrayList<Player> players =
                data.getPlayers();

        if (players.size() < 2) {

            System.out.println(
                    "You need at least 2 player profiles created to start a game.");

            return;
        }

        System.out.println();
        System.out.println("AVAILABLE PLAYERS");
        System.out.println("------------------------------");

        printPlayerList();

        int numberPlaying =
                readInt(
                        "How many people are playing: ",
                        2,
                        players.size());

        ArrayList<Integer> selected =
                new ArrayList<Integer>();

        Game game =
                new Game();

        for (int i = 0;
             i < numberPlaying;
             i++) {

            int playerNumber =
                    readInt(
                            "Enter number of player "
                            + (i + 1)
                            + ": ",
                            1,
                            players.size());

            if (selected.contains(
                    playerNumber)) {

                System.out.println(
                        "That player has already been selected.");

                i--;

                continue;
            }

            selected.add(playerNumber);

            Player player =
                    players.get(
                            playerNumber - 1);

            long buyInCents =
                    readMoney(
                            "Enter "
                            + player.getName()
                            + "'s buy in: $",
                            false);

            game.addPlayer(
                    player,
                    buyInCents);
        }

        data.setActiveGame(game);

        saveData();

        System.out.println();
        System.out.println(
                "Game started and saved.");

        playGame(game);
    }

    public void playGame(Game game) {

        boolean stayInGame = true;

        while (stayInGame
                && data.getActiveGame() != null) {

            System.out.println();
            System.out.println("------------------------------");
            System.out.println("        CURRENT GAME");
            System.out.println("------------------------------");

            System.out.println(
                    "Date: " + game.getDate());

            showCurrentGame(game);

            System.out.println();
            System.out.println("1. Add Rebuy");
            System.out.println("2. End Game");
            System.out.println("3. Return to Main Menu");
            System.out.println("4. Cancel Current Game");

            int choice =
                    readInt(
                            "Choose an option: ",
                            1,
                            4);

            if (choice == 1) {
                addRebuy(game);
            }

            else if (choice == 2) {

                if (finishGame(game)) {
                    stayInGame = false;
                }
            }

            else if (choice == 3) {

                saveData();

                stayInGame = false;
            }

            else if (choice == 4) {

                if (confirm(
                        "Are you sure you want to end this game, all unsaved data will be lost.")) {

                    data.setActiveGame(null);

                    saveData();

                    System.out.println(
                            "Current game ended.");

                    stayInGame = false;
                }
            }
        }
    }

    public void showCurrentGame(
            Game game) {

        ArrayList<PlayerSession> sessions =
                game.getPlayers();

        for (int i = 0;
             i < sessions.size();
             i++) {

            PlayerSession session =
                    sessions.get(i);

            System.out.println();

            System.out.println(
                    (i + 1)
                    + ". "
                    + session.getPlayerName());

            System.out.println(
                    "   Initial Buy In: "
                    + Money.format(
                            session.getInitialBuyInCents()));

            System.out.println(
                    "   Rebuys: "
                    + Money.format(
                            session.getTotalRebuysCents()));

            System.out.println(
                    "   Total Invested: "
                    + Money.format(
                            session.getTotalBuyInCents()));
        }

        System.out.println();

        System.out.println(
                "Total money in game: "
                + Money.format(
                        game.getTotalBuyInsCents()));
    }

    public void addRebuy(
            Game game) {

        ArrayList<PlayerSession> sessions =
                game.getPlayers();

        System.out.println();
        System.out.println("PLAYERS");

        for (int i = 0;
             i < sessions.size();
             i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + sessions.get(i)
                    .getPlayerName());
        }

        int playerNumber =
                readInt(
                        "Which player is rebuying: ",
                        1,
                        sessions.size());

        PlayerSession session =
                sessions.get(
                        playerNumber - 1);

        long amountCents =
                readMoney(
                        "Rebuy amount: $",
                        false);

        session.addRebuy(
                amountCents);

        saveData();

        System.out.println(
                session.getPlayerName()
                + " rebought for "
                + Money.format(
                        amountCents)
                + ".");

        System.out.println(
                "New total invested: "
                + Money.format(
                        session.getTotalBuyInCents()));
    }

    public boolean finishGame(
            Game game) {

        ArrayList<PlayerSession> sessions =
                game.getPlayers();

        System.out.println();
        System.out.println("ENTER FINAL AMOUNTS $$$");
        System.out.println("------------------------------");

        for (int i = 0;
             i < sessions.size();
             i++) {

            PlayerSession session =
                    sessions.get(i);

            long finalAmount =
                    readMoney(
                            session.getPlayerName()
                            + " finished with: $",
                            true);

            session.setFinalAmountCents(
                    finalAmount);

            saveData();
        }

        if (!game.isBalanced()) {

            System.out.println();
            System.out.println(
                    "ERROR: Money distribution does not make sense try again.");

            System.out.println(
                    "Total Buy Ins: "
                    + Money.format(
                            game.getTotalBuyInsCents()));

            System.out.println(
                    "Total Final Money: "
                    + Money.format(
                            game.getTotalFinalAmountsCents()));

            long difference =
                    game.getTotalFinalAmountsCents()
                    - game.getTotalBuyInsCents();

            System.out.println(
                    "Difference: "
                    + Money.formatSigned(
                            difference));

            System.out.println(
                    "The game was not completed. You can choose End Game again and re-enter the cash outs.");

            return false;
        }

        GameRecord completed =
                game.createGameRecord();

        data.getGameHistory().add(
                completed);

        data.setActiveGame(null);

        saveData();

        System.out.println();
        System.out.println("------------------------------");
        System.out.println("          GAME RESULTS");
        System.out.println("------------------------------");

        ArrayList<GamePlayerRecord> records =
                completed.getPlayerRecords();

        for (int i = 0;
             i < records.size();
             i++) {

            GamePlayerRecord record =
                    records.get(i);

            System.out.println();

            System.out.println(
                    record.getPlayerName());

            System.out.println(
                    "Total Buy In: "
                    + Money.format(
                            record.getTotalBuyInCents()));

            System.out.println(
                    "Cash-Out: "
                    + Money.format(
                            record.getFinalAmountCents()));

            System.out.println(
                    "Result: "
                    + Money.formatSigned(
                            record.getProfitLossCents()));
        }

        System.out.println();
        System.out.println(
                "Game saved to game history.");

        return true;
    }

    public void viewAllGames() {

        ArrayList<GameRecord> games =
                data.getGameHistory();

        if (games.size() == 0) {

            System.out.println(
                    "No completed games have been saved.");

            return;
        }

        boolean viewing = true;

        while (viewing) {

            System.out.println();
            System.out.println("------------------------------");
            System.out.println("          ALL GAMES");
            System.out.println("------------------------------");

            for (int i = 0;
                 i < games.size();
                 i++) {

                GameRecord game =
                        games.get(i);

                System.out.println(
                        (i + 1)
                        + ". "
                        + game.getDate()
                        + " - "
                        + game.getPlayerRecords().size()
                        + " players");
            }

            System.out.println("0. Back");

            int choice =
                    readInt(
                            "Select game: ",
                            0,
                            games.size());

            if (choice == 0) {

                viewing = false;
            }

            else {

                manageCompletedGame(
                        choice - 1);
            }
        }
    }

    public void manageCompletedGame(
            int gameIndex) {

        ArrayList<GameRecord> games =
                data.getGameHistory();

        boolean viewing = true;

        while (viewing
                && gameIndex < games.size()) {

            GameRecord game =
                    games.get(gameIndex);

            showCompletedGame(game);

            System.out.println();
            System.out.println("1. Edit Game");
            System.out.println("2. Delete Game");
            System.out.println("0. Back");

            int choice =
                    readInt(
                            "Choose a option: ",
                            0,
                            2);

            if (choice == 0) {

                viewing = false;
            }

            else if (choice == 1) {

                editCompletedGame(
                        gameIndex);
            }

            else if (choice == 2) {

                if (confirm(
                        "Delete this completed game")) {

                    games.remove(gameIndex);

                    saveData();

                    System.out.println(
                            "Game deleted. The player statistics were automatically updated.");

                    viewing = false;
                }
            }
        }
    }

    public void showCompletedGame(
            GameRecord game) {

        System.out.println();
        System.out.println("------------------------------");
        System.out.println(
                "GAME - " + game.getDate());
        System.out.println("------------------------------");

        ArrayList<GamePlayerRecord> records =
                game.getPlayerRecords();

        for (int i = 0;
             i < records.size();
             i++) {

            GamePlayerRecord record =
                    records.get(i);

            System.out.println();

            System.out.println(
                    (i + 1)
                    + ". "
                    + record.getPlayerName());

            System.out.println(
                    "   Initial Buy In: "
                    + Money.format(
                            record.getInitialBuyInCents()));

            System.out.println(
                    "   Rebuys: "
                    + Money.format(
                            record.getTotalRebuysCents()));

            System.out.println(
                    "   Total Invested: "
                    + Money.format(
                            record.getTotalBuyInCents()));

            System.out.println(
                    "   Cash Out: "
                    + Money.format(
                            record.getFinalAmountCents()));

            System.out.println(
                    "   Result: "
                    + Money.formatSigned(
                            record.getProfitLossCents()));
        }

        System.out.println();

        System.out.println(
                "Total Buy Ins: "
                + Money.format(
                        game.getTotalBuyInsCents()));

        System.out.println(
                "Total Cash Outs: "
                + Money.format(
                        game.getTotalFinalAmountsCents()));

        System.out.println(
                "Balanced: "
                + (game.isBalanced()
                ? "Yes"
                : "No"));
    }

    public void editCompletedGame(
            int gameIndex) {

        GameRecord original =
                data.getGameHistory()
                .get(gameIndex);

        GameRecord workingCopy =
                new GameRecord(original);

        boolean editing = true;

        while (editing) {

            System.out.println();
            System.out.println("------------------------------");
            System.out.println("          EDIT GAME");
            System.out.println("------------------------------");

            showCompletedGame(
                    workingCopy);

            System.out.println();
            System.out.println("1. Change Date");
            System.out.println("2. Edit Player Amounts");
            System.out.println("3. Save Changes");
            System.out.println("4. Cancel Changes");

            int choice =
                    readInt(
                            "Choose an option: ",
                            1,
                            4);

            if (choice == 1) {

                changeGameDate(
                        workingCopy);
            }

            else if (choice == 2) {

                editGamePlayer(
                        workingCopy);
            }

            else if (choice == 3) {

                if (!workingCopy.isBalanced()) {

                    System.out.println(
                            "Cannot save. The edited game does not balance.");

                    System.out.println(
                            "Buy Ins: "
                            + Money.format(
                                    workingCopy.getTotalBuyInsCents()));

                    System.out.println(
                            "Cash Outs: "
                            + Money.format(
                                    workingCopy.getTotalFinalAmountsCents()));
                }

                else {

                    data.getGameHistory().set(
                            gameIndex,
                            workingCopy);

                    saveData();

                    System.out.println(
                            "Game changes saved. Player statistics were automatically updated.");

                    editing = false;
                }
            }

            else if (choice == 4) {

                System.out.println(
                        "Changes cancelled.");

                editing = false;
            }
        }
    }

    public void changeGameDate(
            GameRecord game) {

        while (true) {

            System.out.print(
                    "Enter date as YYYY-MM-DD: ");

            String date =
                    input.nextLine().trim();

            try {

                LocalDate.parse(date);

                game.setDate(date);

                return;
            }

            catch (Exception e) {

                System.out.println(
                        "Invalid date. Example: 2026-09-30");
            }
        }
    }

    public void editGamePlayer(
            GameRecord game) {

        ArrayList<GamePlayerRecord> records =
                game.getPlayerRecords();

        for (int i = 0;
             i < records.size();
             i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + records.get(i)
                    .getPlayerName());
        }

        int playerNumber =
                readInt(
                        "Select player: ",
                        1,
                        records.size());

        GamePlayerRecord record =
                records.get(
                        playerNumber - 1);

        boolean editingPlayer = true;

        while (editingPlayer) {

            System.out.println();
            System.out.println(
                    "Editing "
                    + record.getPlayerName());

            System.out.println(
                    "1. Change Initial Buy In");

            System.out.println(
                    "2. Replace Rebuy List");

            System.out.println(
                    "3. Change Cash Out");

            System.out.println(
                    "0. Back");

            int choice =
                    readInt(
                            "Choose an option: ",
                            0,
                            3);

            if (choice == 0) {

                editingPlayer = false;
            }

            else if (choice == 1) {

                long amount =
                        readMoney(
                                "New initial buy in: $",
                                false);

                record.setInitialBuyInCents(
                        amount);
            }

            else if (choice == 2) {

                int count =
                        readInt(
                                "How many rebuys should this player have? ",
                                0,
                                100);

                ArrayList<Long> rebuys =
                        new ArrayList<Long>();

                for (int i = 0;
                     i < count;
                     i++) {

                    long amount =
                            readMoney(
                                    "Rebuy "
                                    + (i + 1)
                                    + ": $",
                                    false);

                    rebuys.add(amount);
                }

                record.setRebuysCents(
                        rebuys);
            }

            else if (choice == 3) {

                long amount =
                        readMoney(
                                "New cash out: $",
                                true);

                record.setFinalAmountCents(
                        amount);
            }
        }
    }

    public void renamePlayer() {

        ArrayList<Player> players =
                data.getPlayers();

        if (players.size() == 0) {

            System.out.println(
                    "No player profiles exist.");

            return;
        }

        printPlayerList();

        int playerNumber =
                readInt(
                        "Select player to rename: ",
                        1,
                        players.size());

        Player player =
                players.get(
                        playerNumber - 1);

        System.out.print(
                "Enter new name for "
                + player.getName()
                + ": ");

        String newName =
                input.nextLine().trim();

        if (newName.length() == 0) {

            System.out.println(
                    "You cannont have no name.");

            return;
        }

        Player duplicate =
                findPlayerByName(newName);

        if (duplicate != null
                && duplicate.getId()
                != player.getId()) {

            System.out.println(
                    "Bro this name is taken.");

            return;
        }

        player.setName(newName);

        ArrayList<GameRecord> games =
                data.getGameHistory();

        for (int i = 0;
             i < games.size();
             i++) {

            games.get(i)
                    .renamePlayerSnapshot(
                            player.getId(),
                            newName);
        }

        if (data.getActiveGame() != null) {

            data.getActiveGame()
                    .renamePlayerSnapshot(
                            player.getId(),
                            newName);
        }

        saveData();

        System.out.println(
                "Player renamed to "
                + newName
                + ".");
    }

    public void deletePlayer() {

        ArrayList<Player> players =
                data.getPlayers();

        if (players.size() == 0) {

            System.out.println(
                    "There are no player profiles to delete.");

            return;
        }

        printPlayerList();

        int playerNumber =
                readInt(
                        "Select a player to delete: ",
                        1,
                        players.size());

        Player player =
                players.get(
                        playerNumber - 1);

        if (data.getActiveGame() != null
                && data.getActiveGame()
                .containsPlayerId(
                        player.getId())) {

            System.out.println(
                    "You cannot delete this player while they are in the active game.");

            return;
        }

        System.out.println(
                "Deleting the profile will not delete old completed game history.");

        if (!confirm(
                "Delete "
                + player.getName()
                + "'s profile")) {

            System.out.println(
                    "Delete cancelled.");

            return;
        }

        players.remove(
                playerNumber - 1);

        saveData();

        System.out.println(
                player.getName()
                + "'s profile has been deleted.");
    }

    public Player findPlayerByName(
            String name) {

        ArrayList<Player> players =
                data.getPlayers();

        for (int i = 0;
             i < players.size();
             i++) {

            if (players.get(i)
                    .getName()
                    .equalsIgnoreCase(name)) {

                return players.get(i);
            }
        }

        return null;
    }

    public void printPlayerList() {

        ArrayList<Player> players =
                data.getPlayers();

        for (int i = 0;
             i < players.size();
             i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + players.get(i)
                    .getName());
        }
    }

    public int readInt(
            String prompt,
            int minimum,
            int maximum) {

        while (true) {

            System.out.print(prompt);

            String text =
                    input.nextLine().trim();

            try {

                int value =
                        Integer.parseInt(text);

                if (value >= minimum
                        && value <= maximum) {

                    return value;
                }
            }

            catch (NumberFormatException e) {
            }

            System.out.println(
                    "Enter a number from "
                    + minimum
                    + " to "
                    + maximum
                    + ".");
        }
    }

    public long readMoney(
            String prompt,
            boolean allowZero) {

        while (true) {

            System.out.print(prompt);

            String text =
                    input.nextLine().trim();

            try {

                long cents =
                        Money.parseCents(text);

                if (allowZero
                        && cents >= 0) {

                    return cents;
                }

                if (!allowZero
                        && cents > 0) {

                    return cents;
                }
            }

            catch (Exception e) {
            }

            if (allowZero) {

                System.out.println(
                        "Enter an amount of $0.00 >=.");
            }

            else {

                System.out.println(
                        "Enter an amount > $0.00.");
            }
        }
    }

    public boolean confirm(
            String message) {

        while (true) {

            System.out.print(
                    message
                    + " (yes/no): ");

            String answer =
                    input.nextLine().trim();

            if (answer.equalsIgnoreCase(
                    "yes")) {

                return true;
            }

            if (answer.equalsIgnoreCase(
                    "no")) {

                return false;
            }

            System.out.println(
                    "Type yes or no.");
        }
    }

    public void saveData() {

        DataStore.save(data);
    }
}
