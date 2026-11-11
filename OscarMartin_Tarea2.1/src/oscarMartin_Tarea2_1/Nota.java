package oscarMartin_Tarea2_1;

public class Nota {
	
	String asignatura;
	int trimestre;
	double nota;
	
	public Nota(String asignatura, int trimestre, double nota) {
		super();
		this.asignatura = asignatura;
		this.trimestre = trimestre;
		this.nota = nota;
	}
	
	public String getAsignatura() {
		return asignatura;
	}
	
	public void setAsignatura(String asignatura) {
		this.asignatura = asignatura;
	}
	
	public int getTrimestre() {
		return trimestre;
	}
	
	public void setTrimestre(int trimestre) {
		this.trimestre = trimestre;
	}
	
	public double getNota() {
		return nota;
	}
	
	public void setNota(double nota) {
		this.nota = nota;
	}

	@Override
	public String toString() {
		return "Nota [asignatura=" + asignatura + ", trimestre=" + trimestre + ", nota=" + nota + "]";
	}

}
