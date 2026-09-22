public class promedio_calificacion {
    public static void main(String[] args) {


        int[] calificaciones = {4, 8, 15, 16, 23, 42};
        int suma = 0;
        int i = 0;

        // Corrección 1 y 2: condición corregida con < e incremento i++
        while (i < calificaciones.length) {
            suma += calificaciones[i];
            i++;
        }

        // Corrección 3: a double para obtener división real con decimales
        double promedio = (double) suma / calificaciones.length;
        System.out.println("Satisfaccion promedio: " + promedio);
    }
}