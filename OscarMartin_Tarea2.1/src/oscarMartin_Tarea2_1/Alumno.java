package oscarMartin_Tarea2_1;

import java.util.ArrayList;

public class Alumno {
	
	int id;
	String nombre;
	String apellidos;
	private ArrayList<Nota> notas; //Cada alumno tiene una lista de notas
	
	public Alumno(int id, String nombre, String apellidos, ArrayList<Nota> notas) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.notas = notas;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public ArrayList<Nota> getNotas() {
		return notas;
	}
	
	public void setNotas(ArrayList<Nota> notas) {
		this.notas = notas;
	}

	@Override
	public String toString() {
		return "Alumno [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", notas=" + notas + "]";
	}	
	
}
