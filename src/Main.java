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
private static void registrarEntrada() {
    int posicion = celdaDisponible();
    if (posicion == -1) {
        imprimir("El parqueadero esta lleno. No se puede registrar el ingreso.");
        return;
    }

    imprimir("1. Carro");
    imprimir("2. Moto");
    int opcionTipo = leerEntero("Tipo de vehiculo: ", 1, 2);
    String tipo = TIPO_MOTO;
    if (opcionTipo == 1) {
        tipo = TIPO_CARRO;
    }

    LocalDateTime ingreso = leerFechaHora("Fecha y hora de ingreso (yyyy-MM-dd HH:mm): ");

    tipoVehiculo[posicion] = tipo;
    fechaIngreso[posicion] = ingreso;
    celdasOcupadas++;
    celdasLibres--;

    imprimir("Ingreso registrado. Celda asignada: " + (posicion + 1));
}

static int leerEntero(String mensaje, int minimo, int maximo) {
    while (true) {
        System.out.print(mensaje);
        if (!scanner.hasNextInt()) {
            imprimir("Error: ingrese un numero entero.");
            scanner.nextLine(); // descarta la linea incorrecta
            continue;
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // consume el salto de linea pendiente
        if (valor < minimo || valor > maximo) {
            imprimir("Error: el valor debe estar entre " + minimo + " y " + maximo + ".");
            continue;
        }
        return valor;
    }
    private static void registrarSalida() {
        if (celdasOcupadas == 0) {
            imprimir("No hay vehiculos en el parqueadero.");
            return;
        }

        int celda = leerEntero("Numero de celda (1-" + CAPACIDAD_TOTAL + "): ", 1, CAPACIDAD_TOTAL);
        int posicion = celda - 1;

        if (tipoVehiculo[posicion] == null) {
            imprimir("La celda " + celda + " esta libre. No hay vehiculo para liquidar.");
            return;
        }

        LocalDateTime salida;
        while (true) {
            salida = leerFechaHora("Fecha y hora de salida (yyyy-MM-dd HH:mm): ");
            if (!salida.isBefore(fechaIngreso[posicion])) {
                break;
            }
            imprimir("Error: la salida no puede ser anterior al ingreso ("
                    + fechaIngreso[posicion].format(FORMATO_FECHA_HORA) + ").");
        }

        String tipo = tipoVehiculo[posicion];
        long total = liquidar(posicion, salida);

        if (tipo.equals(TIPO_CARRO)) {
            recaudoCarros += total;
        } else {
            recaudoMotos += total;
        }

        tipoVehiculo[posicion] = null;
        fechaIngreso[posicion] = null;
        celdasOcupadas--;
        celdasLibres++;
    }

    static long contarDiasCompletos(LocalDateTime ingreso, LocalDateTime salida) {
        return ChronoUnit.DAYS.between(ingreso, salida);
    }

    static long calcularHorasRestantes(LocalDateTime cursor, LocalDateTime salida) {
        long minutosRestantes = Duration.between (cursor, salida).toMinutes();
        return (minutosRestantes + 59) / 60;
    }

    static long liquidar(int posicion, LocalDateTime salida) {
        String tipo = tipoVehiculo[posicion];
        LocalDateTime ingreso = fechaIngreso[posicion];

        long diasACobrar = contarDiasCompletos(ingreso, salida);
        LocalDateTime cursor = ingreso.plusDays(diasACobrar);
        long horasACobrar = calcularHorasRestantes(cursor, salida);

        if (horasACobrar >= MINIMO_HORAS_DIAS) {
            diasACobrar++;
            horasACobrar = 0;
        }

        long tarifaDia = obtenerTarifaDia(tipo);
        long tarifaHora = obtenerTarifaHora(tipo);
        long subtotalDias = diasACobrar * tarifaDia;
        long subtotalHoras = horasACobrar * tarifaHora;
        long total = subtotalDias + subtotalHoras;

        imprimir("");
}
    imprimir("Celda liberada: " + (posicion + 1));
    imprimir("Tipo de vehiculo: " + tipo);
    imprimir("Ingreso: " + ingreso.format(FORMATO_FECHA_HORA));
    imprimir("Salida:  " + salida.format(FORMATO_FECHA_HORA));
    if (diasACobrar > 0) {
        imprimir(diasACobrar + " dia(s) x $" + tarifaDia + " = $" + subtotalDias);
    }
    if (horasACobrar > 0) {
        imprimir(horasACobrar + " hora(s) x $" + tarifaHora + " = $" + subtotalHoras);
    }
    imprimir("TOTAL A PAGAR: $" + total);
    imprimir("=============================");

    return total;
}




