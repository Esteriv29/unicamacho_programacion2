import java.util.Scanner;

public class Ejercicio5_Busqueda_1Valor {
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


        System.out.print("\nIngrese el valor que desea buscar: ");
        int valorBuscado = scanner.nextInt();


        boolean encontrado = false;
        int posicion = -1;


        for (int i = 0; i < n; i++) {
            if (vector[i] == valorBuscado) {
                encontrado = true;
                posicion = i;
                break;
            }
        }


        System.out.println("\n--- RESULTADOS ---");
        if (encontrado) {
            System.out.println("El valor " + valorBuscado + " SÍ existe en el vector.");
            System.out.println("Se encuentra en la posición (índice): " + posicion);
        } else {
            System.out.println("El valor " + valorBuscado + " NO se encuentra en el vector.");
        }

        scanner.close();
    }
}

