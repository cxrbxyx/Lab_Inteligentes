package main;

import java.util.ArrayList;

public class Vehiculo {

    private char ID;
    private int size;
    private ArrayList<Integer[]> coords;
    private boolean horizontal;

    /**
     * QUE: Construye un objeto Vehiculo.
     * POR QUE: Para representar cada coche o camión como un objeto individual
     * con sus propiedades (ID, coordenadas, tamaño, orientación).
     */
    public Vehiculo(char ID, ArrayList<Integer[]> coords) {
        this.ID = ID;
        this.coords = coords;
        this.size = this.coords.size();
        this.horizontal = isHorizontal();
    }

    /**
     * QUE: Comprueba si el vehículo está orientado horizontalmente.
     * POR QUE: Determina el eje de movimiento del vehículo (horizontal o vertical).
     * Es crucial para calcular movimientos válidos.
     */
    public boolean isHorizontal() {
        // Si hay menos de 2 coordenadas, no se puede determinar (aunque T1 valida esto)
        if (this.coords == null || this.coords.size() < 2) {
            // Por defecto, para un vehículo de tamaño 1 (si se permitiera),
            // no se podría mover, pero T1 los filtra.
            // Asumimos horizontal para el coche 'A' si algo falla,
            // aunque la validación de Nivel ya lo comprueba.
            return true;
        }
        // Comparamos la fila (índice 0) de las primeras dos coordenadas.
        // Si son iguales, es horizontal.
        Integer[] coord1 = this.coords.get(0);
        Integer[] coord2 = this.coords.get(1);
        
        return coord1[0].equals(coord2[0]);
    }
    
    /**
     * QUE: Devuelve las posiciones del vehículo en formato "(fila,col)(fila,col)".
     * [cite_start]POR QUE: Para cumplir con el formato de salida de la Tarea 1 (--whereis)[cite: 214].
     */
    public String getCoordsFormatted() {
        StringBuilder result = new StringBuilder();
        for (Integer[] pos : this.coords) {
            result.append("(").append(pos[0]).append(",").append(pos[1]).append(")");
        }
        return result.toString();
    }

    // --- Getters y Setters ---

    /**
     * QUE: Obtiene el ID (letra) del vehículo.
     * POR QUE: Para identificar unívocamente el vehículo.
     */
    public char getID() {
        return ID;
    }

    /**
     * QUE: Obtiene el tamaño (2 o 3) del vehículo.
     * POR QUE: Para saber si es un coche o un camión.
     */
    public int getSize() {
        return size;
    }

    /**
     * QUE: Obtiene la lista de coordenadas (fila, col) que ocupa el vehículo.
     * POR QUE: Para saber dónde está situado en el tablero.
     */
    public ArrayList<Integer[]> getCoords() {
        return coords;
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
}