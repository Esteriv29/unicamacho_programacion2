public class Padron_beneficiarios {
    public static void main(String[] args) {
        // Datos de prueba (padrón de beneficiarios)
        long[] padron = {1015234890L, 1020456789L, 1032165498L, 1020456789L, 1054321678L};

        boolean duplicadoEncontrado = false;
        long documentoRepetido = -1;
        int posPrimera = -1;
        int posSegunda = -1;

        // Búsqueda del primer duplicado usando ciclos anidados y bandera
        for (int i = 0; i < padron.length - 1 && !duplicadoEncontrado; i++) {
            for (int j = i + 1; j < padron.length; j++) {
                if (padron[i] == padron[j]) {
                    duplicadoEncontrado = true;
                    documentoRepetido = padron[i];
                    posPrimera = i;
                    posSegunda = j;
                    break;
                }
            }
        }

        // Resultados
        if (duplicadoEncontrado) {
            System.out.println("Se detectó un documento repetido: " + documentoRepetido);
            System.out.println("Aparece en las posiciones: " + posPrimera + " y " + posSegunda);
        } else {
            System.out.println("No se encontraron documentos duplicados en el padrón.");
        }
    }
}
