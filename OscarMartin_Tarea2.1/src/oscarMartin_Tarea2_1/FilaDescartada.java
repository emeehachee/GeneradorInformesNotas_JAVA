package oscarMartin_Tarea2_1;

public class FilaDescartada {
	
	private int numeroLinea;
	private String motivo;
	private String contenido;
	
	public FilaDescartada(int numeroLinea, String motivo, String contenido) {
		this.numeroLinea = numeroLinea;
		this.motivo = motivo;
		this.contenido = contenido;
	}

	public int getNumeroLinea() {
		return numeroLinea;
	}

	public void setNumeroLinea(int numeroLinea) {
		this.numeroLinea = numeroLinea;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Override
	public String toString() {
		return "FilaDescartada [numeroLinea=" + numeroLinea + ", motivo=" + motivo + ", contenido=" + contenido + "]";
	}	

}
