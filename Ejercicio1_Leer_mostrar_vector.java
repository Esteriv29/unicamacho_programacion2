import java.util.Scanner;

public class Ejercicio1_Leer_Mostrar_vector {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Ingrese el tamaño del vector (N): ");
        int n = scanner.nextInt();


        int[] vector = new int[n];


        System.out.println("Ingrese los " + n + " elementos del vector:");
        for (int i = 0; i < n; i++) {
            System.out.print("Elemento [" + i + "]: ");
            vector[i] = scanner.nextInt();
        }

        System.out.println("\nElementos del vector:");
        for (int i = 0; i < n; i++) {
            System.out.print(vector[i] + " ");
        }
        System.out.println();

        scanner.close();

    }
}
