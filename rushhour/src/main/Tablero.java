package main;

import java.util.ArrayList;
import java.util.HashSet;

public class Tablero {
    private char[][] casillas;
    private ArrayList<Vehiculo> vehiculos; // ¡NUEVO!

    /**
     * QUE: Construye un Tablero a partir de su matriz de casillas.
     * POR QUE: Es la representación principal del estado del juego.
     */
    public Tablero(char[][] casillas) {
        this.casillas = casillas;
        this.vehiculos = new ArrayList<>(); // Inicializa la lista
    }

    // --- Getters y Setters ---
    
    public char[][] getCasillas() {
        return casillas;
    }

    public void setCasillas(char[][] casillas) {
        this.casillas = casillas;
    }
    
    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(ArrayList<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
    
    /**
     * QUE: Busca y devuelve un objeto Vehiculo basado en su ID.
     * POR QUE: Método de ayuda para acceder rápidamente a un vehículo
     * concreto desde la lista de vehículos del tablero.
     */
    private Vehiculo getVehiculoById(char id) {
        for (Vehiculo v : this.vehiculos) {
            if (v.getID() == id) {
                return v;
            }
        }
        return null;
    }


    /**
     * QUE: Convierte un nivel válido en un objeto Tablero (matriz 6x6)
     * y lo puebla con objetos Vehiculo.
     * POR QUE: Transforma el string lineal en una estructura de objetos
     * fácil de manipular para el juego y las consultas.
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
        
        // --- LÓGICA AÑADIDA PARA CREAR VEHÍCULOS ---
        HashSet<Character> idsEncontrados = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                char id = casillas[i][j];
                if (id != 'o' && !idsEncontrados.contains(id)) {
                    // Si es un vehículo y no lo hemos creado ya
                    ArrayList<Integer[]> coords = whereIsAVehicle(tablero, id);
                    Vehiculo v = new Vehiculo(id, coords);
                    tablero.getVehiculos().add(v);
                    idsEncontrados.add(id);
                }
            }
        }
        // ---------------------------------------------
        
        return tablero;
    }

    /**
     * QUE: Encuentra todas las posiciones (fila, columna) de un vehículo específico.
     * POR QUE: Necesario para crear el objeto Vehiculo durante la inicialización
     * del tablero.
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
     * POR QUE: Proporcionar una representación legible (requerido por T1 --whereis).
     * Ahora delega la lógica de formato al objeto Vehiculo.
     */
    public String getVehiclePositionsFormatted(char vehicle) {
        Vehiculo v = getVehiculoById(vehicle);
        if (v == null) {
            return "Vehiculo '" + vehicle + "' no encontrado";
        }
        return v.getCoordsFormatted();
    }

    /**
     * QUE: Devuelve el tamaño (casillas que ocupa) de un vehículo específico.
     * POR QUE: Determinar si es un coche o un camión (requerido por T1 --size).
     * Ahora obtiene esta información directamente del objeto Vehiculo.
     */
    public int getVehicleSize(char vehicle) {
        Vehiculo v = getVehiculoById(vehicle);
        if (v == null) {
            return 0; // Vehículo no encontrado
        }
        return v.getSize();
    }

    /**
     * QUE: Devuelve qué vehículo está en una posición específica del tablero.
     * POR QUE: Consultar el contenido de una casilla (requerido por T1 --what).
     */
    public String getVehicleAt(int row, int col) {
        if (row < 0 || row > 5 || col < 0 || col > 5) {
            return "Error: Posicion fuera de rango (0-5)";
        }
        char vehicle = this.getCasillas()[row][col];
        return String.valueOf(vehicle);
    }

    /**
     * QUE: Cuenta el número total de vehículos diferentes en el tablero.
     * POR QUE: Proporcionar estadísticas del nivel (requerido por T1 --howmany).
     * Ahora solo consulta el tamaño de la lista de vehículos.
     */
    public int countVehicles() {
        return this.vehiculos.size();
    }
}