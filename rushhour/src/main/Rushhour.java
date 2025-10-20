package main;

public class Rushhour {

    /**
     * QUE: Punto de entrada principal que procesa argumentos de línea de comandos.
     * POR QUE: Permitir ejecutar el programa desde terminal con diferentes comandos.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        String command = args[0];

        switch (command) {
            case "verify":
                handleVerify(args);
                break;
            case "question":
                handleQuestion(args);
                break;
            default:
                System.out.println("Comando desconocido: " + command);
        }
    }

    /**
     * QUE: Maneja el comando verify que valida un nivel.
     * POR QUE: Verificar si un nivel es válido antes de jugar, mostrando código de error.
     */
    private static void handleVerify(String[] args) {
        if (args.length < 3 || !args[1].equals("-s")) {
            System.out.println("Uso: rushhour verify -s <nivel>");
            return;
        }

        String level = args[2];
        new Nivel(level);
        int resultado = Nivel.verify_level(Nivel.getArrayLevel());
        System.out.println(resultado);
    }

    /**
     * QUE: Maneja el comando question que consulta información del tablero.
     * POR QUE: Proporcionar una interfaz de consultas sobre el estado del juego.
     */
    private static void handleQuestion(String[] args) {
        if (args.length < 4 || !args[1].equals("-s")) {
            System.out.println("Uso: java -jar rushhour.jar question -s <nivel> <opcion> [parametro]");
            System.out.println("Opciones:");
            System.out.println("  --whereis <vehiculo>  : Devuelve la posicion del vehiculo");
            System.out.println("  --howmany             : Devuelve el numero de vehiculos");
            System.out.println("  --size <vehiculo>     : Devuelve el tamaño del vehiculo");
            System.out.println("  --what <fila>,<col>   : Devuelve que vehiculo esta en esa posicion");
            return;
        }

        String level = args[2];
        Nivel nivel = new Nivel(level);

        int resultado = Nivel.verify_level(Nivel.getArrayLevel());
        if (resultado != 0) {
            System.out.println("Error: Nivel no valido (codigo: " + resultado + ")");
            return;
        }

        Tablero tablero = Tablero.create_tablero(nivel);
        if (tablero == null) {
            System.out.println("Error: No se pudo crear el tablero");
            return;
        }

        String option = args[3];

        switch (option) {
            case "--whereis":
                if (args.length < 5) {
                    System.out.println("Error: Falta el parametro <vehiculo>");
                    return;
                }
                // --- REFACTORIZADO ---
                // Llama al método de instancia de tablero
                System.out.println(tablero.getVehiclePositionsFormatted(args[4].charAt(0)));
                break;

            case "--howmany":
                // --- REFACTORIZADO ---
                // Llama al método de instancia de tablero
                System.out.println(tablero.countVehicles());
                break;

            case "--size":
                if (args.length < 5) {
                    System.out.println("Error: Falta el parametro <vehiculo>");
                    return;
                }
                // --- REFACTORIZADO ---
                // Llama al método de instancia de tablero
                int size = tablero.getVehicleSize(args[4].charAt(0));
                if (size == 0) {
                    System.out.println("Vehiculo '" + args[4].charAt(0) + "' no encontrado");
                } else {
                    System.out.println(size);
                }
                break;

            case "--what":
                if (args.length < 5) {
                    System.out.println("Error: Falta el parametro <fila>,<columna>");
                    return;
                }
                // Pasamos el objeto tablero (que es necesario para el método no estático)
                handleWhat(tablero, args[4]);
                break;

            default:
                System.out.println("Opcion desconocida: " + option);
        }
    }

    /**
     * QUE: Procesa la opción '--what' que consulta qué vehículo hay en una posición.
     * POR QUE: Separar la lógica de parsing de coordenadas y manejo de errores.
     */
    private static void handleWhat(Tablero tablero, String position) {
        try {
            String[] parts = position.split(",");
            if (parts.length != 2) {
                System.out.println("Error: Formato invalido. Use: <fila>,<columna>");
                return;
            }

            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());

            // --- REFACTORIZADO ---
            // Llama al método de instancia de tablero
            String result = tablero.getVehicleAt(row, col);
            System.out.println(result);

        } catch (NumberFormatException e) {
            System.out.println("Error: Las coordenadas deben ser numeros");
        }
    }
}