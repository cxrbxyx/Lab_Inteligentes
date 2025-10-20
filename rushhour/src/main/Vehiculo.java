package main;
import java.util.ArrayList;

public class Vehiculo {

    private char ID;
    private int size;
    private ArrayList<Integer[]> coords;
    private boolean horizontal;

    public Vehiculo(char ID, ArrayList<Integer[]> coords) {
        this.ID = ID;
        this.coords = coords;
        this.size = this.coords.size();
        this.horizontal = isHorizontal();
    }

    public char getID() {
        return ID;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Integer[]> getCoords() {
        return coords;
    }

    public boolean isHorizontal() {
        boolean horizontal = true;
        ArrayList<Integer[]> auxCoords = this.coords;
        for (int i = 0; i < auxCoords.size(); i++) {
            Integer[] actualCoord = auxCoords.get(i);
            Integer[] nextCoord = auxCoords.get(i + 1);
            if (Math.abs(actualCoord[0] - nextCoord[0]) != 0 && Math.abs(actualCoord[1] - nextCoord[1]) != 1)
                horizontal = false;
        }

        return horizontal;
    }

    public void setID(char iD) {
        ID = iD;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCoords(ArrayList<Integer[]> coords) {
        this.coords = coords;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public static void printCoords() {

    }

    public static boolean isValid() {
    	return true;
    }

}
