package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Actividad {
	private LocalDate fecha;
	private String usuario;
	private String cuentaOrigen;
	private double monto;
	private String estado;
	
	public Actividad (String usuario, String cuentaOrigen, double monto, String estado) {
		this.usuario=usuario;
		this.fecha=Utilitarios.hoy();
		this.cuentaOrigen=cuentaOrigen;
		this.monto=monto;
		this.estado=estado;
	}
	
	public abstract String toString();
	
	public String getUsuario() {
		return this.usuario;
	}
	
	public LocalDate getFecha() {
		return this.fecha;
	}
	
	public String getCuentaOrigen() {
		return this.cuentaOrigen;
	}
	
	public double getMonto() {
		return this.monto;
	}
	
	public String getEstado() {
		return this.estado;
	}
}

