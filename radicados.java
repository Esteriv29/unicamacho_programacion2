public class radicados {
    public static void main(String[] args) {


    int pendientes = 6;
    int ciclos = 0;
    int MAX_CICLOS = 1000; // Salvaguarda

        while (pendientes != 1 && ciclos < MAX_CICLOS) {
        if (pendientes % 2 == 0) {
            pendientes = pendientes / 2;
        } else {
            pendientes = 3 * pendientes + 1;
        }
        ciclos++;
    }
        if (ciclos >= MAX_CICLOS) {
        System.out.println("Proceso detenido: se excedió el número máximo de iteraciones.");
    } else {
        System.out.println(ciclos);
    }
}
}
