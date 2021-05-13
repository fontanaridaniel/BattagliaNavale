package pack;

public class Boats {
    int[] boats = { 6, 4, 4, 3, 3, 3, 2, 2, 2, 2 };
    Table table;

    public Boats(Table t) {
        table = t;
    }

    // Get boat
    public int getBoat() {
        for (int i = 0; i < boats.length; i++)
            if (boats[i] > 0)
                return boats[i];
        return 0;
    }

    // Check if coordinates are in the table
    public boolean isBoatInTable(int x, int y, int d, int l) { // d=direction l=lenght(of the boat)
        if ((d == table.RIGHT && (y + l - 1) < 10) || (d == table.DOWN && (x + l - 1) < 10))
            return true;
        return false;
    }

    // Check if a position and neighbours are empty
    public boolean isEmptyPosition(int x, int y, int d, int l) {
        try {
            if (table.matrix[x][y] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x][y - 1] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x][y + 1] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x + 1][y] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x + 1][y - 1] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x + 1][y + 1] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x - 1][y] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x - 1][y - 1] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        try {
            if (table.matrix[x - 1][y + 1] == table.BOAT)
                return false;
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isValidPosition(int x, int y, int d, int l) {
        int i;
        if (isBoatInTable(x, y, d, l)) {
            if (d == table.DOWN) {
                for (i = 0; i < l; i++)
                    if (!isEmptyPosition(x + i, y, d, l))
                        return false;
            } else {
                for (i = 0; i < l; i++)
                    if (!isEmptyPosition(x, y + i, d, l))
                        return false;
            }
            return true;
        }
        return false;
    }

    public boolean isInTable(int x, int y) {
        if (x < 0 || y < 0 || x > 9 || y > 9)
            return false;
        return true;
    }

    public void hitCoords(int x, int y) {
        if (table.matrix[x][y] == table.WATER)
            table.matrix[x][y] = table.MISS;
        else if (table.matrix[x][y] == table.BOAT)
            table.matrix[x][y] = table.HIT;
    }

    public boolean placeBoat(int x, int y, int d, int l) {
        if (!isInTable(x, y))
            return false;
        if (!isValidPosition(x, y, d, l))
            return false;
        int i;

        if (d == table.DOWN)
            for (i = 0; i < l; i++)
                table.matrix[x + i][y] = table.BOAT;
        else
            for (i = 0; i < l; i++)
                table.matrix[x][y + i] = table.BOAT;
        for (i = 0; i < boats.length; i++) {
            if (boats[i] != 0) {
                boats[i] = 0;
                break;
            }
        }
        return true;
    }
}