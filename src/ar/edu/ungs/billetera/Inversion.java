package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Inversion {
	private String dni;
	private int plazoDias;
	private double monto;
	private String cvu;
	private LocalDate fecha;
	private boolean estado;
	
	public Inversion(String dni, String cvu, double monto, int plazoDias){
		this.dni=dni;
		this.plazoDias=plazoDias;
		this.monto=monto;
		this.cvu=cvu;
		this.fecha=Utilitarios.hoy();
		this.estado=true;

	}
	
	@Override 
	public abstract String toString();
	
	public abstract double calcularRentabilidad();
	
	public abstract double rentabilidadActual( int diasPasados);
	
	public abstract double totalPrecancelada( int diasPasados);
	
	public void precancelar() {
		this.estado=false;
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public int getPlazoDias() {
		return plazoDias;
	}
	
	public String getCvu() {
		return cvu;
	}
	
	public LocalDate getFecha() {
		return fecha;
	}

}
