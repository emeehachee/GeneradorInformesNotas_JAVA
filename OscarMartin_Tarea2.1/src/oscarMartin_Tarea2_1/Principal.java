package oscarMartin_Tarea2_1;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String ruta="notas_export.csv";
		File f = new File(ruta);
		
		int opcion=-1;
		while(opcion!=5) {
			System.out.println();
			System.out.println("=== MENU ===");
			System.out.println("1. Cargar y validar el fichero");
			System.out.println("2. Generar boletines");
			System.out.println("3. Generar estadisticas por asignatura");
			System.out.println("4. Generar informes de filas descartadas");
			System.out.println("5. Salir");
			System.out.println();
			System.out.println("Elige una opcion: ");
			
			//Si no se ingresa un numero, saltara la excepcion
			try {
				opcion= sc.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("ERROR, debes introducir un numero");
				sc.nextLine();	//Limpiar el buffer
				continue;	//para poder saltar lo que queda de la vuelta del bucle y volver otra vez al principio del while
			}
			
			switch(opcion){
				case 1:			
					GestorNotas.cargarValidar(f);
					break;
				case 2:
					GestorNotas.generarBoletines();
					break;
				case 3:
					GestorNotas.estadisticasAsignatura();
					break;
				case 4:
					GestorNotas.filasDescartas();
					break;
				case 5:
					opcion=5;
					break;
				default:
					System.out.println("Opcion no valida!!");
			}		
		}
		
		System.out.println("Has salido!");

	}

}
