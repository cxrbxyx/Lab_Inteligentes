package main;
import java.util.ArrayList;

public class Tablero {
    private char[][] casillas;

    public Tablero(char[][] casillas) {
        this.casillas = casillas;
    }

    public char[][] getCasillas() {
        return casillas;
    }

    public void setCasillas(char[][] casillas) {
        this.casillas = casillas;
    }

    /**
     * QUE: Convierte un nivel válido en un objeto Tablero con matriz 6x6.
     * POR QUE: Transformar el string lineal en una estructura bidimensional
     *          más fácil de manipular para el juego.
     */
    public static Tablero create_tablero(Nivel nivel) {
        char[] auxCasillas = Nivel.getArrayLevel();
        char[][] casillas = new char[6][6];
        if (Nivel.verify_level(auxCasillas) == 0) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    casillas[i][j] = auxCasillas[(i * 6) + j];
                }
            }
        } else {
            System.out.println("Nivel no valido");
            return null;
        }
        Tablero tablero = new Tablero(casillas);
        return tablero;
    }

    /**
     * QUE: Encuentra todas las posiciones (fila, columna) de un vehículo específico.
     * POR QUE: Necesario para mostrar dónde está ubicado un vehículo en el tablero.
     */
    public static ArrayList<Integer[]> whereIsAVehicle(Tablero tablero, char vehicle) {
        ArrayList<Integer[]> positions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tablero.casillas[i][j] == vehicle) {
                    positions.add(new Integer[] { i, j });
                }
            }
        }
        return positions;
    }

    /**
     * QUE: Devuelve las posiciones de un vehículo en formato "(fila,col)(fila,col)".
     * POR QUE: Proporcionar una representación legible de las coordenadas para el usuario.
     */
    public static String getVehiclePositionsFormatted(Tablero tablero, char vehicle) {
        ArrayList<Integer[]> positions = whereIsAVehicle(tablero, vehicle);
        if (positions.isEmpty()) {
            return "Vehiculo '" + vehicle + "' no encontrado";
        }
        StringBuilder result = new StringBuilder();
        for (Integer[] pos : positions) {
            result.append("(").append(pos[0]).append(",").append(pos[1]).append(")");
        }
        return result.toString();
    }

    /**
     * QUE: Cuenta cuántas casillas ocupa un vehículo específico.
     * POR QUE: Determinar si es un coche o un camión.
     */
    public static int getVehicleSize(Tablero tablero, char vehicle) {
        int count = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tablero.casillas[i][j] == vehicle) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * QUE: Devuelve qué vehículo está en una posición específica del tablero.
     * POR QUE: Consultar el contenido de una casilla concreta por coordenadas.
     */
    public static String getVehicleAt(Tablero tablero, int row, int col) {
        if (row < 0 || row > 5 || col < 0 || col > 5) {
            return "Error: Posicion fuera de rango (0-5)";
        }
        char vehicle = tablero.getCasillas()[row][col];
        return String.valueOf(vehicle);
    }

    /**
     * QUE: Cuenta el número total de vehículos diferentes en el tablero.
     * POR QUE: Proporcionar estadísticas del nivel.
     */
    public static int countVehicles(Tablero tablero) {
        String vehicles = "";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                char c = tablero.casillas[i][j];
                if (c != 'o' && !vehicles.contains(String.valueOf(c))) {
                    vehicles += c;
                }
            }
        }
        return vehicles.length();
    }

}
