package pack;

public class Table {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public final int WATER = 0, BOAT = 1, HIT = 2, MISS = 3, DOWN = 2, RIGHT = 1;

    public int[][] matrix = new int[10][10];
    public int[][] opponentMatrix = new int[10][10];
    
    public boolean iWon, opponentWon;

    public Table() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                matrix[i][j] = WATER;
    }
    int boats = 9;
    private String table;

    public String getVal(int flag) {
        if (flag == BOAT) 
            return ANSI_YELLOW+"███"+ANSI_RESET;
        else if (flag == HIT) 
            return ANSI_RED+"███"+ANSI_RESET;
        else if (flag == MISS) 
            return ANSI_CYAN+"~~~"+ANSI_RESET;
        return "   ";
    }

    public String getValOpponent(int flag) {
        if (flag == HIT) 
            return ANSI_RED+"███"+ANSI_RESET;
        else if (flag == MISS) 
            return ANSI_CYAN+"~~~"+ANSI_RESET;
        return "   ";
    }

    public String stringTable() {
        String send = "";
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                send = send + matrix[x][y] + ";";
            }
        }
        return send;
    }

    public String getAllTables(String stringa) {
        iWon = true;
        opponentWon = true;
        String[] parti = stringa.split(";");
        int l = 0;
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++) {
                opponentMatrix[x][y] = Integer.parseInt(parti[l]);
                l += 1;
            }
        String tables = "";
        tables = "\n  |                  IO                   |\t\t  |              AVVERSARIO               |\n" + 
                 "  |-A---B---C---D---E---F---G---H---I---J-|\t\t  |-A---B---C---D---E---F---G---H---I---J-|\n";
        for (int x = 0; x < 10; x++) {
            tables += (x + 1) + (x==9?"|":" |");
            for (int y = 0; y < 10; y++) {
                if (matrix[x][y] == BOAT)
                    iWon = false;
                if (y == 9) {
                    tables += getVal(matrix[x][y]) + "|\t\t";
                } else {
                    tables += getVal(matrix[x][y]) + "|";
                }
            }
            tables += (x + 1) + (x==9?"|":" |");
            for (int k = 0; k < 10; k++) {
                if (opponentMatrix[x][k] == BOAT)
                    opponentWon = false;
                    
                if (k == 9) {
                    tables += getValOpponent(opponentMatrix[x][k]) + "|\n";
                } else {
                    tables += getValOpponent(opponentMatrix[x][k]) + "|";
                }
            }
        }
        return tables;
    }

    public String getTable(int[][] m) {
        table = "  |-A---B---C---D---E---F---G---H---I---J-|\n";
        for (int x = 0; x < 10; x++) {
            table = table + (x + 1) + (x==9?"|":" |");
            for (int y = 0; y < 10; y++) {
                if (y == 9)
                    table = table + getVal(m[x][y]) + "|\n";
                else
                    table = table + getVal(m[x][y]) + "|";
            }
        }
        return table;
    }
}