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

    public String getAccion() {
        return accion;
    }

    public Tablero getNuevoEstado() {
        return nuevoEstado;
    }

    public int getCosto() {
        return costo;
    }

    /**
     * QUE: Devuelve la representación en String del sucesor.
     * POR QUE: Para cumplir con el formato de salida exacto 
     * [accion,estado_string,costo]
     * requerido por la Tarea 2[cite: 439].
     */
    @Override
    public String toString() {
        // Llama a levelToString() del nuevo tablero para obtener el string de 36
        // caracteres
        return "[" + this.accion + "," + this.nuevoEstado.levelToString() + "," + this.costo + "]";
    }
}