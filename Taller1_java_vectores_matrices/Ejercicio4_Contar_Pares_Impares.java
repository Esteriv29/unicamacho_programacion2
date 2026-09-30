import java.util.Scanner;

public class Ejercicio4_Contar_Pares_Impares {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Ingrese el tamaño del vector (N): ");
        int n = scanner.nextInt();


        int[] vector = new int[n];
        int pares = 0;
        int impares = 0;


        System.out.println("Ingrese los " + n + " elementos del vector:");
        for (int i = 0; i < n; i++) {
            System.out.print("Elemento [" + i + "]: ");
            vector[i] = scanner.nextInt();
        }


        for (int i = 0; i < n; i++) {
            if (vector[i] % 2 == 0) {
                pares++;
            } else {
                impares++;
            }
        }


        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Cantidad de números pares: " + pares);
        System.out.println("Cantidad de números impares: " + impares);

        scanner.close();

    }
}
