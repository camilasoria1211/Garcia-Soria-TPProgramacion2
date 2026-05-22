package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Inversion {
	private int plazoDias;
	private float monto;
	private String cuentaOrigen;
	private LocalDate fecha;
	
	public Inversion(int plazoDias, float monto, String cuentaOrigen){
		this.plazoDias=plazoDias;
		this.monto=monto;
		this.cuentaOrigen=cuentaOrigen;
		this.fecha=Utilitarios.hoy();
	}
	
	@Override 
	public abstract String toString();
	
	public abstract double calcularRentabilidad();
	
	public float getMonto() {
		return monto;
	}
	
	public int getPlazoDias() {
		return plazoDias;
	}
	
	public String getCuentaOrigen() {
		return cuentaOrigen;
	}
	
	public LocalDate getFecha() {
		return fecha;
	}

}
