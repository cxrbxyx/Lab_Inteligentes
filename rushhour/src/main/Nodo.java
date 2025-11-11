package main;

import java.util.ArrayList;
import java.util.Collections;

/**
 * QUE: Representa un nodo en el árbol de búsqueda para Rush Hour.
 * POR QUE: Almacenar el estado del juego, costos y referencias necesarias para
 * la búsqueda y reconstrucción de la solución.
 */
public class Nodo implements Comparable<Nodo> {

    private int ID;
    private Nodo padre;
    private String accion;
    private String estado;
    private int costo;
    private int profundidad;
    private double heuristica;
    private int valor;

    private static int nextID = 0;

    /**
     * QUE: Constructor para el nodo raíz.
     * POR QUE: Inicializar la búsqueda con el estado inicial del juego.
     */
    public Nodo(String estadoInicial) {
        this.ID = nextID++;
        this.padre = null;
        this.accion = "none";
        this.estado = estadoInicial;
        this.costo = 0;
        this.profundidad = 0;
        this.heuristica = 0;
        this.valor = 0;
    }

    /**
     * QUE: Constructor para un nodo sucesor.
     * POR QUE: Crear nuevos nodos durante la expansión del árbol de búsqueda.
     */
    public Nodo(Nodo padre, Sucesor sucesor, String estrategia) {
        this.ID = nextID++;
        this.padre = padre;
        this.accion = sucesor.getAction();
        this.estado = sucesor.getNewState().levelToString();

        this.profundidad = padre.getProfundidad() + 1;
        this.costo = padre.getCosto() + sucesor.getCost();
        this.heuristica = 0;

        // Asignar valor según la estrategia de búsqueda
        asignarValor(estrategia);
    }

    /**
     * QUE: Asigna el valor del nodo según la estrategia de búsqueda.
     * POR QUE: Determinar la prioridad del nodo en la frontera según la estrategia.
     * Separar esta lógica mejora la claridad y facilita añadir estrategias.
     */
    private void asignarValor(String estrategia) {
        switch (estrategia) {
            case "BFS":
                
                this.valor = this.profundidad;
                break;
            case "DFS":
                this.valor = (int) (1000.0 / (this.profundidad + 1));
                break;
            case "UCS":
                this.valor = this.costo;
                break;
            default:
                this.valor = this.profundidad;
        }
    }

    /**
     * QUE: Resetea el contador de IDs.
     * POR QUE: Asegurar que cada ejecución del solver comience con IDs desde 0.
     */
    public static void resetIDCounter() {
        nextID = 0;
    }

    /**
     * QUE: Compara dos nodos para ordenación en la frontera.
     * POR QUE: Implementar Comparable para usar en PriorityQueue y cumplir con
     * los criterios de desempate (primero por valor, luego por ID).
     */
    @Override
    public int compareTo(Nodo other) {
        // Primero comparar por valor
        int cmpValor = Integer.compare(this.valor, other.getValor());
        if (cmpValor != 0) {
            return cmpValor;
        }
        // Si empatan en valor, desempatar por ID (menor ID primero)
        return Integer.compare(this.ID, other.getID());
    }

    /**
     * QUE: Genera la representación textual del nodo.
     * POR QUE: Cumplir con el formato de salida exacto requerido:
     * [ID,parentID,accion,estado,costo,profundidad,h,valor]
     */
    @Override
    public String toString() {
        String parentIdStr = (this.padre != null) ? String.valueOf(this.padre.getID()) : "none";
        int h = (int) this.heuristica;

        return "[" + this.ID + "," + parentIdStr + "," + this.accion + "," +
                this.estado + "," + this.costo + "," + this.profundidad + "," +
                h + "," + this.valor + "]";
    }

    /**
     * QUE: Reconstruye el camino desde la raíz hasta este nodo.
     * POR QUE: Obtener la secuencia completa de nodos que forman la solución,
     * necesario para imprimir el camino de la solución encontrada.
     */
    public ArrayList<Nodo> getCamino() {
        ArrayList<Nodo> camino = new ArrayList<>();
        Nodo actual = this;

        
        while (actual != null) {
            camino.add(actual);
            actual = actual.getPadre();
        }

        
        Collections.reverse(camino);
        return camino;
    }

    /**
     * QUE: Obtiene el ID único del nodo.
     * POR QUE: Identificar y comparar nodos durante la búsqueda.
     */
    public int getID() {
        return ID;
    }

    /**
     * QUE: Obtiene el nodo padre.
     * POR QUE: Reconstruir el camino de la solución hacia atrás.
     */
    public Nodo getPadre() {
        return padre;
    }

    /**
     * QUE: Obtiene la acción que generó este nodo.
     * POR QUE: Mostrar la secuencia de movimientos de la solución.
     */
    public String getAccion() {
        return accion;
    }

    /**
     * QUE: Obtiene el estado del tablero en este nodo.
     * POR QUE: Comparar estados para detección de repetidos y verificar objetivo.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * QUE: Obtiene el costo acumulado hasta este nodo.
     * POR QUE: Calcular el valor de prioridad en UCS y A*.
     */
    public int getCosto() {
        return costo;
    }

    /**
     * QUE: Obtiene la profundidad del nodo en el árbol.
     * POR QUE: Calcular el valor de prioridad en BFS y DFS.
     */
    public int getProfundidad() {
        return profundidad;
    }

    /**
     * QUE: Obtiene el valor heurístico del nodo.
     * POR QUE: Usar en estrategias informadas (GREEDY, A*) cuando se implementen.
     */
    public double getHeuristica() {
        return heuristica;
    }

    /**
     * QUE: Obtiene el valor de prioridad del nodo.
     * POR QUE: Determinar el orden de expansión en la frontera.
     */
    public int getValor() {
        return valor;
    }
}
