import java.io.*;
import java.net.*;

import pack.*;

public class App {
    static Table table = new Table();
    static Boats boats = new Boats(table);
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static OutputStream outputStream;
    static DataOutputStream dataOutputStream;
    static InputStream inputStream;
    static DataInputStream dataInputStream;
    static Socket socket;
    static ServerSocket ss;
    static boolean isServer, isReady = false;
    static String opponentMatrix;
    static int randomValue;

    private static void print(String s) {
        System.out.print(s);
    }
    
    public static void clearScreen() throws IOException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
        System.out.flush();
    }

    private static void closeConnection() throws IOException {
        if (isServer)
            ss.close();
        socket.close();
    }

    // Get remote IP
    private static String getRemoteIP() throws IOException {
        String ip;
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        ip = in.readLine();
        return ip;
    }

    // Initialize connection as server
    private static void serverConnection() throws IOException {
        isServer = true;
        System.out.println("My IP: " + getRemoteIP());

        ss = new ServerSocket(8989);
        socket = ss.accept(); // Accepts the connection
        print("Connesso correttamente\n");
    }

    // Initialize connection as client
    private static void clientConnection() throws IOException, InterruptedException {
        boolean condition;
        print("Inserisci un indirizzo IP: ");
        do {
            condition = false;
            try {
                String ip = reader.readLine();
                socket = new Socket(ip, 8989);
            } catch (Exception e) {
                clearScreen();
                print("Inserisci un indirizzo IP valido: ");
                condition = true;
            }
        } while (condition);
        print("Connesso correttamente\n");
    }

    // Create connection as server or client
    private static void createConnection(int choice) throws IOException, InterruptedException {
        switch (choice) {
            case 1:
                // SERVER
                serverConnection();
                break;
            case 2:
                // CLIENT
                clientConnection();
                break;
            default:
                break;
        }
    }

    public static boolean askForBoats() throws IOException, InterruptedException {
        int boat = 0, x = 0, y = 0, d = 0;
        String coords;
        do {
            try {
                boat = boats.getBoat();
                print(table.getTable(table.matrix) + "\n\n[Barca da " + boat + "] Inserisci coordinate: ");
                coords = reader.readLine();
                y = convertCoords(String.valueOf(coords.charAt(0)));
                if (coords.length() > 2)
                    x = Integer.parseInt(coords.substring(1, 3)) - 1;
                else
                    x = Integer.parseUnsignedInt(String.valueOf(coords.charAt(1))) - 1;
                print("Inserisci la direzione [1=DESTRA, 2=GIU]: ");
                d = Integer.parseInt(reader.readLine());
                boats.placeBoat(x, y, d, boat);
                clearScreen();
                boat = boats.getBoat();
            } catch (Exception e) {
                clearScreen();
            }
        } while (boat != 0);
        return true;
    }

    public static int convertCoords(String x) {
        x = x.toLowerCase();
        switch (x) {
            case "a":
                return 0;
            case "b":
                return 1;
            case "c":
                return 2;
            case "d":
                return 3;
            case "e":
                return 4;
            case "f":
                return 5;
            case "g":
                return 6;
            case "h":
                return 7;
            case "i":
                return 8;
            case "j":
                return 9;
        }
        return 0;
    }

    public static String askForCoords() throws IOException {
        int x = 0, y = 0;
        do {
            try {
                print("\n\n\n\n\n" + table.getAllTables(opponentMatrix));
                print("\nInserisci coordinate: ");
                String coords = reader.readLine();
                y = convertCoords(String.valueOf(coords.charAt(0)));
                if (coords.length() > 2)
                    x = Integer.parseInt(coords.substring(1, 3)) - 1;
                else
                    x = Integer.parseUnsignedInt(String.valueOf(coords.charAt(1))) - 1;
            } catch (Exception e) {
                x = 120;
            }
        } while (!boats.isInTable(x, y));
        return x + ";" + y;
    }

    public static void updateScreen() throws IOException {
        dataOutputStream.flush();
        if (randomValue == 0) {
            opponentMatrix = dataInputStream.readUTF(); // GET TABLE
            dataOutputStream.writeUTF(table.stringTable()); // SEND TABLE
        } else {
            dataOutputStream.writeUTF(table.stringTable()); // SEND TABLE
            opponentMatrix = dataInputStream.readUTF(); // GET TABLE
        }
        dataOutputStream.flush();
        print(table.getAllTables(opponentMatrix));
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("1. Host game");
        System.out.println("2. Connect to a game");

        int choice = Integer.parseInt(reader.readLine());
        createConnection(choice);

        outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        inputStream = socket.getInputStream();
        dataInputStream = new DataInputStream(inputStream);

        /*boats.placeBoat(0, 0, 1, 6);
        boats.placeBoat(2, 0, 1, 4);
        boats.placeBoat(4, 0, 1, 4);
        boats.placeBoat(6, 0, 1, 3);
        boats.placeBoat(8, 0, 1, 3);
        boats.placeBoat(0, 7, 1, 3);
        boats.placeBoat(2, 7, 1, 2);
        boats.placeBoat(4, 7, 1, 2);
        boats.placeBoat(6, 7, 1, 2);
        boats.placeBoat(0, 0, 1, 2);
        isReady = true;*/

        isReady = askForBoats();

        randomValue = (int) (Math.random() * 2);

        // Server
        if (isServer && isReady) {
            if (dataInputStream.readUTF().equals("ready")) {
                dataOutputStream.writeUTF("ready");
                print("Server pronto\n");
                dataOutputStream.writeUTF(((randomValue + 1) % 2) + "");
            }
        }

        // Client
        else if (!isServer && isReady) {
            dataOutputStream.writeUTF("ready");
            if (dataInputStream.readUTF().equals("ready")) {
                print("Client pronto\n");
                randomValue = Integer.parseInt(dataInputStream.readUTF());
            }
        }

        if (randomValue == 0) {
            updateScreen();
        } else {
            updateScreen();
            String[] parti = dataInputStream.readUTF().split(";"); // GET COORDS
            boats.hitCoords(Integer.parseInt(parti[0]), Integer.parseInt(parti[1]));
            updateScreen();
        }

        do {
            dataOutputStream.flush();
            dataOutputStream.writeUTF(askForCoords()); // SEND COORDS
            updateScreen();
            if (table.iWon || table.opponentWon)
                break;
            String[] parti = dataInputStream.readUTF().split(";"); // GET COORDS
            boats.hitCoords(Integer.parseInt(parti[0]), Integer.parseInt(parti[1]));
            updateScreen();
        } while (!table.iWon && !table.opponentWon);

        if (table.opponentWon)
            print("EZ");
        else if (table.iWon)
            print("GG");

        closeConnection();
    }
}