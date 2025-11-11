package main;

/**
 * QUE: Representa un estado sucesor, resultado de aplicar una acción.
 * POR QUE: Para encapsular la terna [acción, nuevo_estado, costo] requerida
 * por la Tarea 2.
 */
public class Sucesor {

    private String accion;
    private Tablero nuevoEstado;
    private int costo;

    public Sucesor(String accion, Tablero nuevoEstado, int costo) {
        this.accion = accion;
        this.nuevoEstado = nuevoEstado;
        this.costo = costo;
    }

    public String getAction() {
        return accion;
    }

    public Tablero getNewState() {
        return nuevoEstado;
    }

    public int getCost() {
        return costo;
    }

    /**
     * QUE: Devuelve la representación en String del sucesor.
     * POR QUE: Para cumplir con el formato de salida exacto
     */
    @Override
    public String toString() {
        return "[" + this.accion + "," + this.nuevoEstado.levelToString() + "," + this.costo + "]";
    }
}