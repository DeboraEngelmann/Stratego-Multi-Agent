package my_stratego;

public class Celula {
	public String identificador;
	public String time;
	public String tipo;
	public int forca;
	
	
	public Celula(String nome, String time, String tipo, int forca) {
		super();
		this.identificador = nome;
		this.time = time;
		this.tipo = tipo;
		this.forca = forca;
	}
	
	public Celula() {
		super();
		this.identificador = "livre";
		this.time = "livre";
		this.tipo = "livre";
		this.forca = 0;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getForca() {
		return forca;
	}
	public void setForca(int forca) {
		this.forca = forca;
	}
}
