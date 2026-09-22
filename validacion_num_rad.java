import java.util.Scanner;

public class validacion_num_rad {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el número de radicado: ");
        long radicado = scanner.nextLong();

        // Manejo del caso especial cuando el usuario ingresa 0
        if (radicado == 0) {
            System.out.println("Cantidad de dígitos: 1");
            System.out.println("Suma de dígitos: 0");
            System.out.println("Dígito mayor: 0");
            System.out.println("Es capicúa: Sí");
            return;
        }

        long original = radicado;
        long aux = radicado;
        int digitos = 0;
        int suma = 0;
        int digitoMayor = 0;
        long invertido = 0;

        // Procesamiento mediante ciclo while
        while (aux > 0) {
            int digito = (int) (aux % 10); // Extrae el último dígito
            digitos++;
            suma += digito;

            if (digito > digitoMayor) {
                digitoMayor = digito;
            }

            invertido = (invertido * 10) + digito; // Construye el número invertido
            aux /= 10; // Elimina el último dígito
        }

        boolean esCapicua = (original == invertido);

        // Impresión de resultados
        System.out.println("Cantidad de dígitos: " + digitos);
        System.out.println("Suma de dígitos: " + suma);
        System.out.println("Dígito mayor: " + digitoMayor);
        System.out.println("Es capicúa: " + (esCapicua ? "Sí" : "No"));
    }
}