import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static final int CAPACIDAD_TOTAL = 50;
    private static final long TARIFA_HORA_CARRO = 4_000;
    private static final long TARIFA_HORA_MOTO = 2_000;
    private static final long TARIFA_DIA_CARRO = 30_000;
    private static final long TARIFA_DIA_MOTO = 15_000;
    private static final int HORA_APERTURA = 8;
    private static final int HORA_CIERRE = 22;
    private static final int MINIMO_HORAS_DIAS = 8;
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final String TIPO_CARRO = "Carro";
    private static final String TIPO_MOTO = "Moto";

    private static void imprimir(String mensaje) {
        System.out.println(mensaje);
    }

    private static LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (formatoFechaHoraValido(texto)) {
                LocalDateTime fecha = LocalDateTime.parse(texto, FORMATO_FECHA_HORA);

                if (!fecha.format(FORMATO_FECHA_HORA).equals(texto)) {
                    imprimir("La fecha no existe en el calendario.");
                    continue;
                }
                return fecha;
            } else {
                imprimir("Fecha u hora inválidas. Use yyyy-MM-dd HH:mm y el horario 08:00-22:00.");
            }

        }
    }

    private static boolean formatoFechaHoraValido(String texto) {
        if (texto.length() != 16
                || texto.charAt(4) != '-'
                || texto.charAt(7) != '-'
                || texto.charAt(10) != ' '
                || texto.charAt(13) != ':') {
            return false;
        }

        int anio = leerDigitos(texto, 0, 4);
        int mes = leerDigitos(texto, 5, 2);
        int dia = leerDigitos(texto, 8, 2);
        int hora = leerDigitos(texto, 11, 2);
        int minuto = leerDigitos(texto, 14, 2);

        return anio >= 1 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31
                && hora >= HORA_APERTURA && hora <= HORA_CIERRE
                && (hora != HORA_CIERRE || minuto <= 0)
                && minuto >= 0 && minuto <= 59;
    }

    private static int leerDigitos(String texto, int inicio, int cantidad) {
        int valor = 0;
        for (int i = inicio; i < inicio + cantidad; i++) {
            char caracter = texto.charAt(i);
            if (caracter < '0' || caracter > '9') {
                return -1;
            }
            valor = valor * 10 + caracter - '0';
        }
        return valor;
    }

    private static String[] tipoVehiculo = new String[CAPACIDAD_TOTAL];
    private static LocalDateTime[] fechaIngreso = new LocalDateTime[CAPACIDAD_TOTAL];
    static int celdasOcupadas;
    static int celdasLibres;
    static long recaudoCarros;
    static long recaudoMotos;

}
private static void main(String[] args) {
    inicializarDatos();
    int opcion;
    do {
        mostrarMenu();
        opcion = leerEntero("Seleccione una opcion: ", 1, 5);
        switch (opcion) {
            case 1:
                registrarEntrada();
                break;
            case 2:
                registrarSalida();
                break;
            case 3:
                reporteOcupacion();
                break;
            case 4:
                reporteFinanciero();
                break;
            case 5:
                imprimir("Gracias por usar el sistema. Hasta pronto.");
                break;
        }
    } while (opcion != 5);

    scanner.close();
}

private static void inicializarDatos() {
    for (int posicion = 0; posicion < CAPACIDAD_TOTAL; posicion++) {
        tipoVehiculo[posicion] = null;
        fechaIngreso[posicion] = null;
    }
    celdasOcupadas = 0;
    celdasLibres = CAPACIDAD_TOTAL;
    recaudoCarros = 0;
    recaudoMotos = 0;
}

private static void mostrarMenu() {
    imprimir("");
    imprimir("1. Registrar entrada");
    imprimir("2. Registrar salida");
    imprimir("3. Reporte de ocupacion");
    imprimir("4. Reporte financiero");
    imprimir("5. Salir");
}

private static long obtenerTarifaHora(String tipo) {
    if (tipo.equals(TIPO_CARRO)) {
        return TARIFA_HORA_CARRO;
    }
    return TARIFA_HORA_MOTO;
}

private static long obtenerTarifaDia(String tipo) {
    if (tipo.equals(TIPO_CARRO)) {
        return TARIFA_DIA_CARRO;
    }
    return TARIFA_DIA_MOTO;
}



private   static int celdaDisponible() {
    for (int posicion = 0; posicion < CAPACIDAD_TOTAL; posicion++) {
        if (tipoVehiculo[posicion] == null) {
            return posicion;
        }
    }
    return -1;
}


