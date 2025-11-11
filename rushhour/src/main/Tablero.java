package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
     * POR QUE: Transforma el string lineal en una estructura de objetos
     * para el juego y las consultas.
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
        tablero.poblarVehiculos();
        return tablero;
    }

    /**
     * QUE: Comprueba si un vehículo está almacenado en la lista de Vehículos.
     * POR QUE: Para comprobar que no se dupliquen vehículos al escanear el tablero.
     */
    private boolean idYaExiste(char id) {
        for (Vehiculo v : this.vehiculos) {
            if (v.getID() == id)
                return true;
        }
        return false;
    }

    /**
     * QUE: Escanea la matriz de casillas y crea los objetos Vehiculo.
     * POR QUE: Para poblar la lista de vehículos del tablero. Se usa en
     * create_tablero y en applyMove.
     */
    private void poblarVehiculos() {
        this.vehiculos.clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                char id = this.casillas[i][j];
                if (id != 'o' && !idYaExiste(id)) {
                    ArrayList<Integer[]> coords = this.findVehicleCoords(id);
                    Vehiculo v = new Vehiculo(id, coords);
                    this.getVehiculos().add(v);
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

    /**
     * QUE: Convierte el estado actual del tablero a un string de 36 caracteres.
     * POR QUE: Necesario para la salida de los comandos 'successors' y 'move'
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
        Collections.sort(this.vehiculos, new Comparator<Vehiculo>() {
            @Override
            public int compare(Vehiculo v1, Vehiculo v2) {
                return Character.compare(v1.getID(), v2.getID());
            }
        });

        for (Vehiculo v : this.vehiculos) {

            findMoves(v, sucesores, true);

            findMoves(v, sucesores, false);
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
            dc = positiveDir ? 1 : -1;
        } else {
            dr = positiveDir ? -1 : 1;
        }

        for (int dist = 1; dist <= 5; dist++) {
            if (canMove(v, dr * dist, dc * dist)) {

                String accion = "" + v.getID() + dirChar + dist;
                int costo = 6 - dist;
                Tablero nuevoEstado = applyMove(v, dr * dist, dc * dist);
                sucesores.add(new Sucesor(accion, nuevoEstado, costo));
            } else {

                break;
            }
        }
    }

    /**
     * QUE: Verifica si un vehículo puede moverse una distancia (dr, dc).
     * POR QUE: Para saber si la casilla destino está libre y dentro del tablero.
     */
    private boolean canMove(Vehiculo v, int dr, int dc) {

        ArrayList<Integer[]> coords = v.getCoords();
        Integer[] coordToCheck;

        if (dr > 0)
            coordToCheck = coords.get(coords.size() - 1);
        else if (dr < 0)
            coordToCheck = coords.get(0);
        else if (dc > 0)
            coordToCheck = coords.get(coords.size() - 1);
        else
            coordToCheck = coords.get(0);

        int newRow = coordToCheck[0] + dr;
        int newCol = coordToCheck[1] + dc;

        if (newRow < 0 || newRow > 5 || newCol < 0 || newCol > 5) {
            return false;
        }

        return this.casillas[newRow][newCol] == 'o';
    }

    /**
     * QUE: Aplica un movimiento a un vehículo y devuelve un NUEVO estado.
     * POR QUE: Para generar los tableros de los sucesores y para el comando
     * '--move'.
     */
    private Tablero applyMove(Vehiculo v, int dr, int dc) {

        char[][] nuevasCasillas = new char[6][6];
        for (int i = 0; i < 6; i++) {
            nuevasCasillas[i] = this.casillas[i].clone();
        }

        for (Integer[] coord : v.getCoords()) {
            nuevasCasillas[coord[0]][coord[1]] = 'o';
        }

        for (Integer[] coord : v.getCoords()) {
            nuevasCasillas[coord[0] + dr][coord[1] + dc] = v.getID();
        }

        Tablero nuevoTablero = new Tablero(nuevasCasillas);
        nuevoTablero.poblarVehiculos();

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
            return this;
        }

        int dr = 0;
        int dc = 0;

        if (v.isHorizontal()) {
            dc = (dir == '+') ? dist : -dist;
        } else {
            dr = (dir == '+') ? -dist : dist;
        }

        if (canMove(v, (dir == '+') ? dr : dr * dist, (dir == '+') ? dc : dc * dist)) {
            return applyMove(v, dr, dc);
        } else {

            Tablero estadoIntermedio = this;
            int dr_step = (dr == 0) ? 0 : (dr > 0 ? 1 : -1);
            int dc_step = (dc == 0) ? 0 : (dc > 0 ? 1 : -1);

            for (int i = 0; i < dist; i++) {
                Vehiculo v_actual = estadoIntermedio.getVehiculoById(id);
                if (estadoIntermedio.canMove(v_actual, dr_step, dc_step)) {
                    estadoIntermedio = estadoIntermedio.applyMove(v_actual, dr_step, dc_step);
                } else {
                    System.out.println("Error: Movimiento '" + accion + "' es inválido.");
                    return this;
                }
            }
            return estadoIntermedio;
        }
    }
}