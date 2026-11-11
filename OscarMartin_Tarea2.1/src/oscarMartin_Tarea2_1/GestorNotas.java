package oscarMartin_Tarea2_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class GestorNotas {
	
	//lista con los alumnos que tiene como minimo una nota valida
	private static ArrayList<Alumno> listaAlumnos = new ArrayList<>();
	
	//lista donde guardamos las filas erroneas
	private static ArrayList<FilaDescartada> filasDescartadas = new ArrayList<>();
	
	/**
	 * Metodo para cargar y validar el fichero pasado como parametro
	 * @param f , archivo File con la ruta al archivo
	 */
	public static void cargarValidar (File f) {
		
		if(f==null || !f.exists()) {
			System.out.println("El archivo no existe/ sin acceso");
		}else if(f.length()==0) {
			System.out.println("Archivo vacio");
		}else {
			int lineaValida=0;
			
			//StandardCharsets.ISO_8859_1 sirve para indicar la codificacion de caracteres que Java utilizara para leer el archivo
			try(BufferedReader in = new BufferedReader(new FileReader(f,StandardCharsets.ISO_8859_1))){
				String linea;
				boolean cabecera=true;
				int numeroLinea=0;
				
				while((linea=in.readLine())!=null) {
					
					numeroLinea++;//la primera vez sera la cabecera, luego las filas normales
					
					//la primera vez que entre aqui sera la cabecera
					if(cabecera) {
						cabecera=false;
					}else {
						if(validarLinea(linea, numeroLinea)) {
							lineaValida++;
						}
																	
					}																	
				}
				
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
			

			int filasNoValidas=filasDescartadas.size();
			
			System.out.println("Filas validas: "+ lineaValida);
			System.out.println("Filas no validas: "+ filasNoValidas);		
		}
	}
	
	
	/**
	 * Metodo para validar una linea
	 * @param linea  a validar
	 * @param numeroLinea , el numero de la linea
	 * @return True si es valida, en caso contrario False
	 */
	public static boolean validarLinea(String linea, int numeroLinea) {
		
		//Variable para ver si la linea es valida
		//Esta si es True nos permite ir avanzando en sus comprobaciones
		boolean esValida=true;
		
		
		if (linea.trim().isEmpty()) {
		       añadirFilaDescartada(numeroLinea, "LINEA_VACIA", linea);
		       esValida=false;
		   }
		
		//";" delimitado
		//-1 -> para conservar los campos vacíos finales
		//si el ultimo campo esta pero sin datos si ni pongo -1 no lo coge
		String[] partes = linea.split(";", -1);

	    if (partes.length != 6 && esValida) {
	        añadirFilaDescartada(numeroLinea, "COLUMNAS_INCORRECTAS", linea);
	        esValida=false;
	    }
	    
	    if(esValida) {
	    	int id = 0;	//Variable para almacenar el id y poder luego hacer una verificacion de el

		    try {
		        id = Integer.parseInt(partes[0].trim()); //Paso el String a int
		    } catch (NumberFormatException e) {
		    	añadirFilaDescartada(numeroLinea, "ID_NO_VALIDO", linea);
		    	esValida=false;
		    }

		    if (id <= 0 && esValida) {
		        añadirFilaDescartada(numeroLinea, "ID_NO_VALIDO", linea);
		        esValida=false;
		    }
		    
		    if(esValida) {
		    	String nombre = partes[1].trim();
			    String apellidos = partes[2].trim();
			    String asignatura = partes[3].trim();

			    String trimestreString = partes[4].trim();
			    
			    int trimestre = 0;//Variable para almacenar el trimestre y poder luego hacer una verificacion de el

			    try {
			        trimestre = Integer.parseInt(trimestreString); //Paso el String a int
			    } catch (NumberFormatException e) {
			        añadirFilaDescartada(numeroLinea, "TRIMESTRE_NO_VALIDO", linea);
			        esValida=false;
			    }

			    if ((trimestre < 1 || trimestre > 3) && esValida) {
			        añadirFilaDescartada(numeroLinea, "TRIMESTRE_NO_VALIDO", linea);
			        esValida=false;
			    }
			    
			    if(esValida) {
			    	String notaTexto = partes[5].trim();

				    if (notaTexto.isEmpty()) {
				        añadirFilaDescartada(numeroLinea, "NOTA_VACIA", linea);
				        esValida=false;
				    }
				    
				    if(esValida) {
				    	double nota = 0;//Variable para almacenar la nota y poder luego hacer una verificacion de el

					    try {
					        nota = Double.parseDouble(notaTexto.replace(",", "."));//Paso el String a double y le cambio las "," a "."
					        
					    } catch (NumberFormatException e) {
					        añadirFilaDescartada(numeroLinea, "NOTA_NO_NUMERICA", linea);
					        esValida=false;
					    }
					    
				        if ((nota < 0 || nota > 10) && esValida) {
				            añadirFilaDescartada(numeroLinea, "NOTA_FUERA_DE_RANGO", linea);
					        esValida=false;
				        }
				        
				        if(esValida) {
				        	//Metodo para añadir la nota al alumno
					        boolean aniadido= aniadirNotaAlumno(id, nombre, apellidos, asignatura,trimestre, nota, numeroLinea, linea);
					        
					        if(!aniadido) {
					        	esValida=false;
					        }
				        }
				    }
			    }

		    }
		    
	    }
	    
		return esValida;		
	}
	
	
	/**
	 * Metodo para añadir la nota al alumno
	 * @param id  del alumno
	 * @param nombre del alumno
	 * @param apellidos del alumno
	 * @param asignatura correspondiente
	 * @param trimestre de la nota
	 * @param nota del alumno
	 * @param numeroLinea , numero de la linea del alumno
	 * @param linea , la linea entera del alumno
	 * @return True si se ha podido añadir el alumno, de lo contrario False
	 */
	public static boolean aniadirNotaAlumno(int id, String nombre, String apellidos, String asignatura, int trimestre, double nota, int numeroLinea, String linea) {
		
		boolean alumnoAniadido=true;
		
		//Creo el objeto nota del alumno
		Nota notaAlumno = new Nota(asignatura, trimestre, nota);

	    // Creo un objeto alumno para poder ver si este va a tener mas de una nota
	    Alumno alumnoEncontrado = null;

	    boolean lineaDuplicada = false;
	    

	    // Comparo el id para ver si el alumno se ha encontrado
	    for (Alumno a : listaAlumnos) {
	        if (a.getId() == id) {
	            alumnoEncontrado = a;
	            
	            //Recorro las notas que ya tiene el alumno
	            for (Nota n : a.getNotas()) {
	            	// Una nota esta duplicada si coincide la asignatura y si tambien el trimestre del alumno
	                if (n.getAsignatura().equals(asignatura) && n.getTrimestre() == trimestre) {
	                    lineaDuplicada = true;
	                }
	            }
	        }
	    }
	    
	    //Si se ha encontrado una nota duplicada, se descarta esa fila
	    if (lineaDuplicada) {
	        añadirFilaDescartada(numeroLinea, "FILA_DUPLICADA", linea);
	        alumnoAniadido=false;
	    } else {
	    	//Si el alumno ya existe, se añade su nueva nota a su lista de notas
	        if (alumnoEncontrado != null) {
	            alumnoEncontrado.getNotas().add(notaAlumno);

	        } else {
	            // Si no existe, le creo su lista de notas y creo el alumno
	            ArrayList<Nota> notasAlumno = new ArrayList();
	            notasAlumno.add(notaAlumno);

	            Alumno alumn = new Alumno(id, nombre, apellidos, notasAlumno);

	            listaAlumnos.add(alumn);
	        }
	    }

	    return alumnoAniadido;
	}
	
	
	/**
	 * Metodo para geneara los boletines
	 */
	public static void generarBoletines() {
		
		if(listaAlumnos.isEmpty()) {
			System.out.println("Sin alumnos cargados para generar los boletines");
		}else {
			
			//Vaciar carpeta
			String ruta="boletines";
	
			File f = new File(ruta);
			
			//Por si la carpeta no existe
			if (!f.exists()) {
			    f.mkdirs();
			}
			
			//Obtengo los archivos que hay dentro de la carpeta
			File[] archivos = f.listFiles();
			
			//Elimino los archivos siempre y cuando no sean nulos
			if(archivos!=null) {
				for(File archivo: archivos) {
					archivo.delete();
				}
			}
			
			//Recorrer cada alumno para crear su boletin
			for(Alumno a : listaAlumnos) {
				generarBoletinAlumno(a, f);
			}
			
			System.out.println("Boletines creados correctamente");
		}			
	}
	
	
	/**
	 * Metodo para generar el boletin de cada alumno
	 * @param a, objeto alumno para crear su boletin
	 * @param f, carpeta donde se guardaran los boletines
	 */
	public static void generarBoletinAlumno(Alumno a , File f) {
		
		int id = a.getId();
	    String nombre = a.getNombre();
	    String apellido = a.getApellidos();
	    
	    ArrayList<Nota> notasAlumno = a.getNotas();
	    
	    //Estructura para que dentro de boletines , con el formato id.txt
	    File archivo_boletin = new File(f, id + ".txt");

	    try (BufferedWriter out = new BufferedWriter(new FileWriter(archivo_boletin, StandardCharsets.UTF_8))) {

	        out.write("BOLETIN DE NOTAS");
	        out.newLine();
	        out.write("Alumno: " + apellido + ", " + nombre + " (ID " + id + ")");
	        out.newLine();
	        out.write("------------------------------------------");
	        out.newLine();

	        //Saco las asignaturas unicas
	        ArrayList<String> asignaturasUnicas = new ArrayList<>();
	        
	        //Recorro las notas del alumno
	        for (Nota n : notasAlumno) {
	        	//Compruebo que la asignatura no esta en la lista
	            if (!asignaturasUnicas.contains(n.getAsignatura())) {
	            	//Si no esta, la añado
	                asignaturasUnicas.add(n.getAsignatura());
	            }
	        }
	        
	        //Ordenamos las asignaturas
	        Collections.sort(asignaturasUnicas);

	        out.write("Asignatura;T1;T2;T3;Media");
	        out.newLine();
	        
	        //Suma de las medias de las asignaturas
	        double sumaMediaAsign = 0;
	        
	        //Numero de asignaturas
	        int contadorAsign = 0;
	        
	        //Recorro las asignaturas unicas, para calcular sus notas y la media
	        for (String asignatura : asignaturasUnicas) {
	        	
	        	//Nota 1 trimestre
	            double notaT1 = 0;
	            boolean T1TieneNota = false;
	            
	            //Nota 2 trimestre
	            double notaT2 = 0;
	            boolean T2TieneNota = false;
	            
	            //Nota 3 trimestre
	            double notaT3 = 0;
	            boolean T3TieneNota = false;
	            
	            //Recorro las notas del alumno
	            for (Nota n : notasAlumno) {
	                int trimestre = n.getTrimestre();

	                if (n.getAsignatura().equals(asignatura)) {
	                    if (trimestre == 1) {
	                        notaT1 = n.getNota();
	                        T1TieneNota = true;
	                    } else if (trimestre == 2) {
	                        notaT2 = n.getNota();
	                        T2TieneNota = true;
	                    } else if (trimestre == 3) {
	                        notaT3 = n.getNota();
	                        T3TieneNota = true;
	                    }
	                }
	            }
	            
	            //Si tiene una nota el trimestre que podamos formatear el formato de salida
	            String sNotaT1 = "";
	            String sNotaT2 = "";
	            String sNotaT3 = "";
	            
	            //Suma de las notas
	            double sumaNotas = 0;
	            
	            //Suma de los trimestres con nota
	            int numeroTrimestresNota = 0;

	            if (T1TieneNota) {
	                sumaNotas = sumaNotas + notaT1;
	                numeroTrimestresNota++;
	                //Gracias a que es un String lo podemos formatear su salida
	                //Lo hacemos aqui por si acaso no tiene nota y muestra el "-" y daria fallo
	                sNotaT1 = String.format(Locale.GERMAN, "%.2f", notaT1);
	            } else {
	                sNotaT1 = "-";
	            }

	            if (T2TieneNota) {
	                sumaNotas = sumaNotas + notaT2;
	                numeroTrimestresNota++;
	                sNotaT2 = String.format(Locale.GERMAN, "%.2f", notaT2);
	            } else {
	                sNotaT2 = "-";
	            }

	            if (T3TieneNota) {
	                sumaNotas = sumaNotas + notaT3;
	                numeroTrimestresNota++;
	                sNotaT3 = String.format(Locale.GERMAN, "%.2f", notaT3);
	            } else {
	                sNotaT3 = "-";
	            }

	            double media = 0;

	            if (numeroTrimestresNota > 0) {
	                media = sumaNotas / numeroTrimestresNota;
	            }
	            
	            //Suma de las media de las asignaturas
	            sumaMediaAsign = sumaMediaAsign + media;
	            contadorAsign++;

	            out.write(asignatura + ";" + sNotaT1 + ";" + sNotaT2 + ";"+ sNotaT3 + ";" + String.format(Locale.GERMAN, "%.2f", media));
	            out.newLine();
	        }

	        double mediaTotal = 0;

	        if (contadorAsign > 0) {
	            mediaTotal = sumaMediaAsign / contadorAsign;
	        }

	        out.write("------------------------------------------");
	        out.newLine();
	        out.write("Media global: " + String.format(Locale.GERMAN, "%.2f", mediaTotal));
	        out.newLine();

	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }		
	}

	
	/**
	 * Metodo para generar un archivos con las estadisticas por asignatura
	 */
	public static void estadisticasAsignatura() {
		
		if(listaAlumnos.isEmpty()) {
			System.out.println("Sin alumnos cargados para generar los boletines");
		}else {
			
			String ruta="estadisticas.txt";
			
			File f = new File(ruta);
			
			//Guardo las asignaturas unicas
			ArrayList<String> asignaturasUnicas = cogerAsignaturasUnicas();
				
			try(BufferedWriter out = new BufferedWriter(new FileWriter(f,StandardCharsets.UTF_8))){
					
				out.write("Asignatura;Notas;Media;Aprobadas;Suspensas;Maxima;Minima");
				out.newLine();
				
				//Voy cogiendo cada asignatura
				for(String asign : asignaturasUnicas) {
					int totalNotas=0;
					double sumaNotas=0;
					int aprobadas=0;
					int suspensas=0;
					double maxima=0;
					double minima=Double.MAX_VALUE;
					
					//Por cada alumno saco sus datos
					for(Alumno a : listaAlumnos) {
						
						for(Nota n : a.getNotas()) {
							
							//Solo sumo si la nota es de esta asignatura
							if(n.getAsignatura().equals(asign)) {
								double nota= n.getNota();
								totalNotas++;
								sumaNotas=sumaNotas+nota;
								
								if(nota>=5) {
									aprobadas++;
								}else {
									suspensas++;
								}
								
								if(nota>maxima) {
									maxima=nota;
								}
								
								if(nota<minima) {
									minima=nota;
								}
							}
						}
					}
					
					//Variable para la media de las notas
					double mediaNotas = sumaNotas/totalNotas;
					
					out.write(asign+";"+totalNotas+";"+String.format(Locale.GERMAN,"%.2f", mediaNotas)+";"+ aprobadas+";"+suspensas+";"+String.format(Locale.GERMAN,"%.2f", maxima)+";"+String.format(Locale.GERMAN,"%.2f", minima));
					out.newLine();
				}
				
				System.out.println("Fichero de estadisticas.txt creado correctamente");
				
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}	
	}
	
	
	/**
	 * Metodo para crear un archivo con las filasDescartadas
	 */
	public static void filasDescartas() {
		
		if(filasDescartadas.isEmpty()) {
			System.out.println("Sin filas descartadas");
		}else {
			String ruta="descartadas.txt";
			
			File f = new File(ruta);
			
			try(BufferedWriter out = new BufferedWriter(new FileWriter(f,StandardCharsets.UTF_8))){
				
				for(FilaDescartada fd : filasDescartadas) {
					out.write("Linea "+ fd.getNumeroLinea() + " | " + fd.getMotivo()+ " | " + fd.getContenido());
					out.newLine();
				}
				
				System.out.println("Fichero de filas descartas.txt creado correctamente");
				
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}	
	}
	
	
	/**
	 * Metodo para coger solo las asignaturas unicas en total
	 * @return la lista con las asignaturas unicas
	 */
	public static ArrayList<String> cogerAsignaturasUnicas(){
		
		ArrayList<String> asignaturasUnicas = new ArrayList();
		
		//Recorremos todos los alumnos
		for(Alumno a : listaAlumnos) {
			
			//Recorremos las notas de cada asignatura
			for(Nota n : a.getNotas()) {
				//Comprobamos si la asignatura no esta en la lista
                if(!asignaturasUnicas.contains(n.getAsignatura())) {
                	//Si no esta , la añadimos
                    asignaturasUnicas.add(n.getAsignatura());
                        
                }
            }
		}
		
		//Ordeno las asignaturas
		Collections.sort(asignaturasUnicas);
		
		return asignaturasUnicas;	
	}
	
	
	/**
	 * Metodo para añadir a la lista de filasDescartas un fila descartada
	 * @param numeroLinea erronea a añadir
	 * @param motivo erronea a añadir
	 * @param linea erronea a añadir
	 */
	public static void añadirFilaDescartada(int numeroLinea, String motivo, String linea) {
		//Añado el objeto filaDescarta a la lista
	    filasDescartadas.add(new FilaDescartada(numeroLinea, motivo, linea));
	}

}
