package ar.edu.ungs.billetera;

import java.util.ArrayList;

public class Empresa {
	private String cuit;
	private String nombreFantasia;
	private String telefono;
	private String email;
	private ArrayList<String> dniAutorizados;
	
	public Empresa(String cuit, String nombreFantasia, String telefono, String email) {
		this.cuit = cuit;
		this.nombreFantasia = nombreFantasia;
		this.telefono = telefono;
		this.email = email;
		this.dniAutorizados = new ArrayList<String>();
	}
	
	public boolean estaAutorizado(String dni) {
		return this.dniAutorizados.contains(dni);
	}
}
