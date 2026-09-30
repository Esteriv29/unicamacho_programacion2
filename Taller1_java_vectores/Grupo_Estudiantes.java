package Taller1_java_vectores;

import java.util.Scanner;

public class Grupo_Estudiantes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el numero de estudiantes: ");
        int n = scanner.nextInt();

        System.out.println("Ingrese el numero de materias: ");
        int m = scanner.nextInt();

        double [][] notas = new double[n][m];

        // Capturar todas las calificaciones
        System.out.println("\n--- REGISTRO DE CALIFICACIONES ---");
        for (int i = 0; i < n; i++) {
            System.out.println("Estudiante " + (i + 1) + ":");
            for (int j = 0; j < m; j++) {
                System.out.print("  Nota materia " + (j + 1) + ": ");
                notas[i][j] = scanner.nextDouble();
            }
        }

        // b) Calcular e imprimir el promedio de cada estudiante
        System.out.println("\n--- b) PROMEDIO POR ESTUDIANTE ---");
        for (int i = 0; i < n; i++) {
            double sumaEstudiante = 0;
            for (int j = 0; j < m; j++) {
                sumaEstudiante += notas[i][j];
            }
            double promedioEstudiante = sumaEstudiante / m;
            System.out.printf("Estudiante %d: %.2f\n", (i + 1), promedioEstudiante);
        }

        // c) Determinar la materia con el promedio más alto del grupo
        double mayorPromedioMateria = -1;
        int materiaMasAlta = -1;

        for (int j = 0; j < m; j++) {
            double sumaMateria = 0;
            for (int i = 0; i < n; i++) {
                sumaMateria += notas[i][j];
            }
            double promedioMateria = sumaMateria / n;

            if (promedioMateria > mayorPromedioMateria) {
                mayorPromedioMateria = promedioMateria;
                materiaMasAlta = j;
            }
        }

        System.out.println("\n--- c) MATERIA CON MAYOR PROMEDIO ---");
        System.out.printf("Materia %d con un promedio de %.2f\n", (materiaMasAlta + 1), mayorPromedioMateria);

        // d) Determinar la calificación más alta registrada y su ubicación
        double notaMayor = notas[0][0];
        int estudianteNotaMayor = 0;
        int materiaNotaMayor = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (notas[i][j] > notaMayor) {
                    notaMayor = notas[i][j];
                    estudianteNotaMayor = i;
                    materiaNotaMayor = j;
                }
            }
        }

        System.out.println("\n--- d) NOTA MÁS ALTA REGISTRADA ---");
        System.out.println("Calificación: " + notaMayor);
        System.out.println("Corresponde al Estudiante " + (estudianteNotaMayor + 1) + " en la Materia " + (materiaNotaMayor + 1));

        scanner.close();
    }
}
