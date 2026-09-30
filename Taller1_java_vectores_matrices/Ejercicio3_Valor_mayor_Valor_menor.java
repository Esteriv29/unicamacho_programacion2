import java.util.Scanner;

public class Ejercicio3_Valor_mayor_Valor_menor {
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


        int mayor = vector[0];
        int menor = vector[0];


        for (int i = 1; i < n; i++) {
            if (vector[i] > mayor) {
                mayor = vector[i];
            }
            if (vector[i] < menor) {
                menor = vector[i];
            }
        }


        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Valor mayor: " + mayor);
        System.out.println("Valor menor: " + menor);

        scanner.close();
    }
}

