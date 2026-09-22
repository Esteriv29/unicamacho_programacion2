public class principal {
    public static void main(String[] args) {
        int totalEntregados = 0;

        for (int jornada = 1; jornada <= 5; jornada++) {
            if (jornada % 2 == 0) {
                continue; // Jornada sin atención al público
            }
            for (int lote = jornada; lote > 0; lote--) {
                if (lote == 3) {
                    break; // Lote retenido por control de calidad
                }
                totalEntregados += lote;
            }
        }

        System.out.println(totalEntregados);
    }
}