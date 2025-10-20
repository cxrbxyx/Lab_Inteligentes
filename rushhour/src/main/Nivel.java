package main;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nivel {
    private static String level;

    public Nivel(String level) {
        Nivel.level = level;
    }

    public static String getLevel() {
        return level;
    }

    public static void setLevel(String level) {
        Nivel.level = level;
    }

    /**
     * QUE: Convierte el string del nivel en un array de caracteres.
     * POR QUE: Facilita el procesamiento casilla por casilla del tablero.
     */
    public static char[] getArrayLevel() {
        return level.toCharArray();
    }

    /**
     * QUE: Valida que un nivel cumpla todas las reglas del juego Rush Hour.
     * POR QUE: Asegurar que el nivel sea jugable antes de crear el tablero.
     */
    public static int verify_level(char[] casillas) {
        
        String[] vehicles = searchVehicles(casillas);

        if (!isLevelValid(level)) {
            return -1;
        }

        if (!hasCorrectLength(level)) {
            return 1;
        }

        if (!hasValidCharacters(level)) {
            return 2;
        }

        if (!hasRedCar(level)) {
            return 3;
        }

        if (!hasExactlyTwoRedCarCells(casillas)) {
            return 3;
        }

        if (!isRedCarInThirdRow(casillas)) {
            return 4;
        }

        if (!isRedCarHorizontal(casillas)) {
            return 5;
        }

        if (!areAllVehiclesContinuous(casillas, vehicles)) {
            return 7;
        }

        if (!haveValidVehicleLengths(casillas, vehicles)) {
            return 6;
        }

        return 0;
    }

    /**
     * QUE: Verifica que el nivel no sea null ni vacío.
     * POR QUE: Prevenir errores de procesamiento con niveles inválidos.
     */
    private static boolean isLevelValid(String level) {
        return level != null && !level.isEmpty();
    }

    /**
     * QUE: Verifica que el nivel tenga exactamente 36 caracteres.
     * POR QUE: Un tablero 6x6 requiere exactamente 36 casillas.
     */
    private static boolean hasCorrectLength(String level) {
        return level.length() == 36;
    }

    /**
     * QUE: Verifica que el nivel solo contenga letras mayúsculas y 'o'.
     * POR QUE: Los vehículos se representan con letras y las casillas vacías con 'o'.
     */
    private static boolean hasValidCharacters(String level) {
        String regex = "^[A-Zo]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(level);
        return matcher.matches();
    }

    /**
     * QUE: Verifica que exista al menos una letra 'A' en el nivel.
     * POR QUE: 'A' representa el coche rojo que es el objetivo del juego.
     */
    private static boolean hasRedCar(String level) {
        return level.contains("A");
    }

    /**
     * QUE: Cuenta que haya exactamente 2 casillas con el carácter 'A'.
     * POR QUE: El coche rojo debe ocupar exactamente 2 casillas.
     */
    private static boolean hasExactlyTwoRedCarCells(char[] casillas) {
        int count = 0;
        for (char c : casillas) {
            if (c == 'A') {
                count++;
            }
        }
        return count == 2;
    }

    /**
     * QUE: Verifica que el coche rojo esté en la tercera fila.
     * POR QUE: El coche rojo debe estar en la fila donde está la salida.
     */
    private static boolean isRedCarInThirdRow(char[] casillas) {
        for (int i = 12; i < 18; i++) {
            if (casillas[i] == 'A') {
                return true;
            }
        }
        return false;
    }

    /**
     * QUE: Verifica que las dos casillas del coche rojo estén en horizontal.
     * POR QUE: El coche rojo debe poder moverse horizontalmente hacia la salida.
     */
    private static boolean isRedCarHorizontal(char[] casillas) {
        for (int i = 12; i < 17; i++) {
            if (casillas[i] == 'A' && casillas[i + 1] == 'A') {
                return true;
            }
        }
        return false;
    }

    /**
     * QUE: Verifica que todos los vehículos sean continuos sin huecos.
     * POR QUE: Los vehículos no pueden tener espacios vacíos entre sus casillas.
     */
    private static boolean areAllVehiclesContinuous(char[] casillas, String[] vehicles) {
        for (String vehicle : vehicles) {
            if (!isVehicleContinuous(casillas, vehicle.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    /**
     * QUE: Verifica que todos los vehículos tengan longitud válida de 2 o 3.
     * POR QUE: Solo existen coches de 2 casillas y camiones de 3 casillas.
     */
    private static boolean haveValidVehicleLengths(char[] casillas, String[] vehicles) {
        for (String vehicle : vehicles) {
            int count = countVehicleOccurrences(casillas, vehicle);
            if (count < 2 || count > 3) {
                return false;
            }
        }
        return true;
    }

    /**
     * QUE: Cuenta cuántas veces aparece un vehículo específico en el tablero.
     * POR QUE: Necesario para validar que cada vehículo tenga longitud válida.
     */
    private static int countVehicleOccurrences(char[] casillas, String vehicle) {
        int count = 0;
        for (char c : casillas) {
            if (String.valueOf(c).equals(vehicle)) {
                count++;
            }
        }
        return count;
    }

    /**
     * QUE: Verifica que un vehículo específico ocupe casillas adyacentes.
     * POR QUE: Los vehículos deben ser continuos horizontal o verticalmente.
     */
    private static boolean isVehicleContinuous(char[] casillas, char vehicleChar) {
        int[] positions = new int[3];
        int posCount = 0;

        for (int i = 0; i < casillas.length; i++) {
            if (casillas[i] == vehicleChar && posCount < 3) {
                positions[posCount++] = i;
            }
        }

        if (posCount == 2) {
            int pos1 = positions[0];
            int pos2 = positions[1];

            if (pos2 - pos1 == 1 && pos1 / 6 == pos2 / 6) {
                return true;
            }
            if (pos2 - pos1 == 6) {
                return true;
            }
            return false;

        } else if (posCount == 3) {
            int pos1 = positions[0];
            int pos2 = positions[1];
            int pos3 = positions[2];

            if (pos2 - pos1 == 1 && pos3 - pos2 == 1 && pos1 / 6 == pos2 / 6 && pos2 / 6 == pos3 / 6) {
                return true;
            }
            if (pos2 - pos1 == 6 && pos3 - pos2 == 6) {
                return true;
            }
            return false;
        }

        return true;
    }

    /**
     * QUE: Extrae todos los identificadores únicos de vehículos del tablero.
     * POR QUE: Necesario para iterar y validar cada vehículo individualmente.
     */
    public static String[] searchVehicles(char[] casillas) {
        String vehicles = "";
        for (char c : casillas) {
            if (c != 'o' && !vehicles.contains(String.valueOf(c))) {
                vehicles += c;
            }
        }
        String[] arrayVehicles = vehicles.split("");
        return arrayVehicles;
    }

}
