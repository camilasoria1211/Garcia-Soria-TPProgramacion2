package ar.edu.ungs.billetera;

import java.util.ArrayList;

public class Empresa {
	private String cuit;
	private String nombreFantasia;
	private String telefono;
	private String email;
	private ArrayList<String> dniAutorizados;
	private ArrayList<Cuenta> cuentasCorporativas;
	
	public Empresa(String cuit, String nombreFantasia, String telefono, String email) {
		this.cuit = cuit;
		this.nombreFantasia = nombreFantasia;
		this.telefono = telefono;
		this.email = email;
		this.dniAutorizados = new ArrayList<String>();
		this.cuentasCorporativas = new ArrayList<Cuenta>();
	}
	
	public boolean estaAutorizado(String dni) {
		return this.dniAutorizados.contains(dni);
	}
	
	public void agregarAutorizado(String dni) {
		dniAutorizados.add(dni);
	}
	
	public void registrarCuenta(Cuenta cuenta) {
		this.cuentasCorporativas.add(cuenta);
	}
	
	public ArrayList<String> getCuentas() {
		ArrayList<String> listaCuentas = new ArrayList<String>();
		
		for (Cuenta cuenta : this.cuentasCorporativas) {
			String alias = cuenta.getAlias();
			String cvu = cuenta.getCvu();
			
			StringBuilder infoCuenta = new StringBuilder("");
			infoCuenta.append("Corporativa: ").append(alias).append(" (").append(cvu).append(").");
			listaCuentas.add(infoCuenta.toString());
		}
		return listaCuentas;
	}
}
