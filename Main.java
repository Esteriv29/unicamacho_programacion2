import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Adivina mi color favorito: ");
        String bandera = scan.nextLine();

        while (!bandera.equals("celeste")) {
          System.out.println("Ese no es... vuelve intentarlo");
          bandera = scan.nextLine();            
        }
        System.out.println("Muy bien.... adivinaste");
    }
}
