import java.util.Scanner;

public class Ejercicio2_Suma_Promedio {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Ingrese el tamaño del vector (N): ");
        int n = scanner.nextInt();

        // Crear el vector de tamaño N
        int[] vector = new int[n];
        int suma = 0;


        System.out.println("Ingrese los " + n + " elementos del vector:");
        for (int i = 0; i < n; i++) {
            System.out.print("Elemento [" + i + "]: ");
            vector[i] = scanner.nextInt();
            suma += vector[i];
        }


        double promedio = (double) suma / n;

        // Resultados
        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Suma total: " + suma);
        System.out.println("Promedio: " + promedio);

        scanner.close();
    }
}
