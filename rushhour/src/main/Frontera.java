package main;

import java.util.PriorityQueue;

/**
 * QUE: Representa la frontera de nodos por explorar en el árbol de búsqueda.
 * POR QUE: Gestionar el orden de exploración de los nodos según la estrategia,
 * manteniendo automáticamente el orden correcto basado en el valor y ID.
 */
public class Frontera {

    private PriorityQueue<Nodo> estructura;

    /**
     * QUE: Constructor que inicializa la estructura de la frontera.
     * POR QUE: Crear una cola de prioridad que ordene los nodos automáticamente
     * según su método compareTo (valor, luego ID).
     */
    public Frontera() {
        this.estructura = new PriorityQueue<>();
    }

    /**
     * QUE: Agrega un nodo a la frontera.
     * POR QUE: Añadir nuevos nodos candidatos para explorar, manteniendo
     * automáticamente el orden según la prioridad (valor + ID).
     */
    public void agregar(Nodo nodo) {
        estructura.add(nodo);
    }

    /**
     * QUE: Extrae el nodo con mayor prioridad (menor valor) de la frontera.
     * POR QUE: Obtener el siguiente nodo a expandir según la estrategia de
     * búsqueda.
     * La PriorityQueue garantiza que siempre se extrae el de menor valor.
     */
    public Nodo extraer() {
        return estructura.poll(); // poll() devuelve null si está vacía
    }

    /**
     * QUE: Comprueba si la frontera está vacía.
     * POR QUE: Determinar si quedan nodos por explorar o si la búsqueda debe
     * terminar.
     */
    public boolean vacia() {
        return estructura.isEmpty();
    }
}