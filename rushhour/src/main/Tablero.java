package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Tablero {
    private char[][] casillas;
    private ArrayList<Vehiculo> vehiculos;

    /**
     * QUE: Construye un Tablero a partir de su matriz de casillas.
     * POR QUE: Es la representación principal del estado del juego.
     */
    public Tablero(char[][] casillas) {
        this.casillas = casillas;
        this.vehiculos = new ArrayList<>(); // Inicializa la lista
    }

    // --- Getters y Setters (Sin cambios) ---
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
        tablero.poblarVehiculos(); // Lógica de escaneo extraída a un método
        return tablero;
    }

    /**
     * QUE: Escanea la matriz de casillas y crea los objetos Vehiculo.
     * POR QUE: Para poblar la lista de vehículos del tablero. Se usa en
     * create_tablero y en applyMove.
     */
    private void poblarVehiculos() {
        this.vehiculos.clear();
        HashSet<Character> idsEncontrados = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                char id = this.casillas[i][j];
                if (id != 'o' && !idsEncontrados.contains(id)) {
                    ArrayList<Integer[]> coords = this.findVehicleCoords(id);
                    Vehiculo v = new Vehiculo(id, coords);
                    this.getVehiculos().add(v);
                    idsEncontrados.add(id);
                }
            }
        }
    }

    /**
     * QUE: Encuentra todas las posiciones (fila, columna) de un vehículo
     * específico.
     * POR QUE: Necesario para crear el objeto Vehiculo durante la inicialización
     * del tablero.
     */
    private ArrayList<Integer[]> findVehicleCoords(char vehicle) {
        ArrayList<Integer[]> positions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.casillas[i][j] == vehicle) {
                    positions.add(new Integer[] { i, j });
                }
            }
        }
        return positions;
    }

    // --- MÉTODOS TAREA 1 (Refactorizados a no-estáticos) ---

    public String getVehiclePositionsFormatted(char vehicle) {
        Vehiculo v = getVehiculoById(vehicle);
        if (v == null) {
            return "Vehiculo '" + vehicle + "' no encontrado";
        }
        return v.getCoordsFormatted();
    }

    public int getVehicleSize(char vehicle) {
        Vehiculo v = getVehiculoById(vehicle);
        if (v == null) {
            return 0;
        }
        return v.getSize();
    }

    public String getVehicleAt(int row, int col) {
        if (row < 0 || row > 5 || col < 0 || col > 5) {
            return "Error: Posicion fuera de rango (0-5)";
        }
        char vehicle = this.getCasillas()[row][col];
        return String.valueOf(vehicle);
    }

    public int countVehicles() {
        return this.vehiculos.size();
    }

    // --- ¡NUEVOS MÉTODOS TAREA 2! ---

    /**
     * QUE: Convierte el estado actual del tablero a un string de 36 caracteres.
     * POR QUE: Necesario para la salida de los comandos 'successors' y 'move'
     *[cite: 439, 451].
     */
    public String levelToString() {
        StringBuilder sb = new StringBuilder(36);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                sb.append(this.casillas[i][j]);
            }
        }
        return sb.toString();
    }

    /**
     * QUE: Verifica si el estado actual es un estado objetivo.
     * POR QUE: Requerido por la Tarea 2, opción '--goal'.
     */
    public boolean isGoal() {
        Vehiculo cocheA = getVehiculoById('A');
        if (cocheA == null)
            return false;
        
        // El objetivo es que 'A' alcance la casilla (2,5) [cite: 430, 431]
        for (Integer[] coord : cocheA.getCoords()) {
            if (coord[0] == 2 && coord[1] == 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * QUE: Genera todos los estados sucesores válidos desde el estado actual.
     * POR QUE: Requerido por la Tarea 2, comando 'successors'.
     */
    public ArrayList<Sucesor> getSuccessors() {
        ArrayList<Sucesor> sucesores = new ArrayList<>();
        
        // Regla 1: Ordenar vehículos alfabéticamente [cite: 418]
        Collections.sort(this.vehiculos, new Comparator<Vehiculo>() {
            @Override
            public int compare(Vehiculo v1, Vehiculo v2) {
                return Character.compare(v1.getID(), v2.getID());
            }
        });

        for (Vehiculo v : this.vehiculos) {
            // Regla 2: Primero movimientos '+' (derecha/abajo) [cite: 419]
            findMoves(v, sucesores, true); // true para '+'
            // Regla 2: Luego movimientos '-' (izquierda/arriba) [cite: 419]
            findMoves(v, sucesores, false); // false para '-'
        }

        return sucesores;
    }

    /**
     * QUE: Encuentra movimientos válidos para un vehículo en una dirección.
     * POR QUE: Método de ayuda para getSuccessors(), maneja la lógica de
     * escaneo.
     */
    private void findMoves(Vehiculo v, ArrayList<Sucesor> sucesores, boolean positiveDir) {
        char dirChar = positiveDir ? '+' : '-';
        int dr = 0, dc = 0;

        if (v.isHorizontal()) {
            dc = positiveDir ? 1 : -1; // Derecha (+) o Izquierda (-)
        } else {
            // INVERSIÓN: '+' será hacia ARRIBA (-1), '-' será hacia ABAJO (+1)
            dr = positiveDir ? -1 : 1;
        }

        // Regla 3: Orden creciente de casillas (1, 2, 3...) [cite: 420]
        for (int dist = 1; dist <= 5; dist++) {
            if (canMove(v, dr * dist, dc * dist)) {
                // Es un movimiento válido
                String accion = "" + v.getID() + dirChar + dist;
                int costo = 6 - dist; // Costo = 6 - casillas movidas [cite: 411]
                Tablero nuevoEstado = applyMove(v, dr * dist, dc * dist);
                sucesores.add(new Sucesor(accion, nuevoEstado, costo));
            } else {
                // Si no puede moverse 'dist', tampoco podrá 'dist+1'
                break;
            }
        }
    }

    /**
     * QUE: Verifica si un vehículo puede moverse una distancia (dr, dc).
     * POR QUE: Para saber si la casilla destino está libre y dentro del tablero.
     */
    private boolean canMove(Vehiculo v, int dr, int dc) {
        // Solo necesitamos comprobar la "punta" del vehículo en la dirección
        // del movimiento.
        ArrayList<Integer[]> coords = v.getCoords();
        Integer[] coordToCheck;

        if (dr > 0)
            coordToCheck = coords.get(coords.size() - 1); // Abajo
        else if (dr < 0)
            coordToCheck = coords.get(0); // Arriba
        else if (dc > 0)
            coordToCheck = coords.get(coords.size() - 1); // Derecha
        else
            coordToCheck = coords.get(0); // Izquierda

        int newRow = coordToCheck[0] + dr;
        int newCol = coordToCheck[1] + dc;

        // 1. Comprobar límites del tablero
        if (newRow < 0 || newRow > 5 || newCol < 0 || newCol > 5) {
            return false;
        }
        // 2. Comprobar si la casilla está vacía
        return this.casillas[newRow][newCol] == 'o';
    }

    /**
     * QUE: Aplica un movimiento a un vehículo y devuelve un NUEVO estado.
     * POR QUE: Para generar los tableros de los sucesores y para el comando
     * '--move'.
     */
    private Tablero applyMove(Vehiculo v, int dr, int dc) {
        // 1. Crear una copia profunda de las casillas
        char[][] nuevasCasillas = new char[6][6];
        for (int i = 0; i < 6; i++) {
            nuevasCasillas[i] = this.casillas[i].clone();
        }

        // 2. Borrar el vehículo de su posición antigua en las nuevas casillas
        for (Integer[] coord : v.getCoords()) {
            nuevasCasillas[coord[0]][coord[1]] = 'o';
        }

        // 3. Dibujar el vehículo en su posición nueva
        for (Integer[] coord : v.getCoords()) {
            nuevasCasillas[coord[0] + dr][coord[1] + dc] = v.getID();
        }

        // 4. Crear un nuevo objeto Tablero y poblar sus vehículos
        Tablero nuevoTablero = new Tablero(nuevasCasillas);
        nuevoTablero.poblarVehiculos(); // Re-escanear para crear nuevos objetos Vehiculo

        return nuevoTablero;
    }

    /**
     * QUE: Aplica una acción descrita por un string (ej. "A+1").
     * POR QUE: Requerido por la Tarea 2, opción '--move'.
     */
    public Tablero applyMove(String accion) {
        char id = accion.charAt(0);
        char dir = accion.charAt(1);
        int dist = Integer.parseInt(accion.substring(2));

        Vehiculo v = getVehiculoById(id);
        if (v == null) {
            System.out.println("Error: Vehículo '" + id + "' no encontrado en --move.");
            return this; // Devuelve el estado actual si hay error
        }

        int dr = 0;
        int dc = 0;

        if (v.isHorizontal()) {
            dc = (dir == '+') ? dist : -dist;
        } else {
            dr = (dir == '+') ? dist : -dist;
        }

        // Comprobación simple (no comprueba todos los pasos intermedios,
        // pero T2 asume acciones válidas)
        if (canMove(v, (dir == '+') ? dr : dr * dist, (dir == '+') ? dc : dc * dist)) {
             return applyMove(v, dr, dc);
        } else {
            // Chequeo más robusto por si el 'canMove' simple falla (ej. A+2)
             Tablero estadoIntermedio = this;
             int dr_step = (dr == 0) ? 0 : (dr > 0 ? 1 : -1);
             int dc_step = (dc == 0) ? 0 : (dc > 0 ? 1 : -1);
             
             for(int i=0; i<dist; i++) {
                 Vehiculo v_actual = estadoIntermedio.getVehiculoById(id);
                 if (estadoIntermedio.canMove(v_actual, dr_step, dc_step)) {
                     estadoIntermedio = estadoIntermedio.applyMove(v_actual, dr_step, dc_step);
                 } else {
                     System.out.println("Error: Movimiento '" + accion + "' es inválido.");
                     return this; // Devuelve estado original
                 }
             }
             return estadoIntermedio;
        }
    }
}