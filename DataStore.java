import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataStore {

    private static final String FILE_NAME =
            "pokerdata.dat";

    public static void save(PokerData data) {

        try {

            FileOutputStream file =
                    new FileOutputStream(
                            FILE_NAME);

            ObjectOutputStream output =
                    new ObjectOutputStream(file);

            output.writeObject(data);

            output.close();
            file.close();
        }

        catch (Exception e) {

            System.out.println(
                    "ERROR: Could not save data.");

            System.out.println(
                    e.getMessage());
        }
    }

    public static PokerData load() {

        try {

            FileInputStream file =
                    new FileInputStream(
                            FILE_NAME);

            ObjectInputStream input =
                    new ObjectInputStream(file);

            PokerData data =
                    (PokerData) input.readObject();

            input.close();
            file.close();

            System.out.println(
                    "Saved poker data loaded.");

            return data;
        }

        catch (FileNotFoundException e) {

            System.out.println(
                    "No saved poker data found. Starting fresh.");

            return new PokerData();
        }

        catch (Exception e) {

            System.out.println(
                    "ERROR: Could not load saved poker data.");

            System.out.println(
                    "Starting with a new empty data file.");

            return new PokerData();
        }
    }
}
