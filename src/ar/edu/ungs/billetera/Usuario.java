package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.HashMap;

public class Usuario {
	
	private HashMap<String, Cuenta> cuentas;
	private String nombre;
	private String dni;
	private String mail;
	private String telefono;
	private float totalInvertido;

	public Usuario(String nombre, String dni, String mail, String telefono) {
		this.cuentas = new HashMap<>();
		
		this.dni = dni;
		this.nombre = nombre;
		this.telefono = telefono;
		this.mail = mail;
		this.totalInvertido = 0;
	}
	
	public void registrarCuenta(Cuenta cuenta) {
		if (cuenta == null) {
			throw new RuntimeException("Cuenta no válida.");
		} 
		this.cuentas.put(cuenta.getCvu(), cuenta);
	}
	
	// "[Tipo]: [Alias] ([CVU])"
	public ArrayList<String> getCuentas(){
		ArrayList<String> listaCuentas = new ArrayList<String>();
		for (Cuenta cuenta: this.cuentas.values()) {
			String tipo = null;
			if(cuenta instanceof CuentaPremium) {
				tipo = "Premium: ";
			} else if (cuenta instanceof CuentaRegular) {
				tipo = "Regular: ";
			} else if (cuenta instanceof CuentaCorporativa) {
				tipo = "Corporativa: ";
			}	
			String alias = cuenta.getAlias();
			String cvu = cuenta.getCvu();
			
			StringBuilder infoCuenta = new StringBuilder("");
			infoCuenta.append(tipo).append(alias).append(" (").append(cvu).append(").");
			listaCuentas.add(infoCuenta.toString());
		}
		return listaCuentas;
	}
	
	public ArrayList<Actividad> getActividades(){
		
		ArrayList<Actividad> actividadTotal= new ArrayList<>();
		for (Cuenta c: cuentas.values()) {
			actividadTotal.addAll(c.getHistorial());
		}
		
		return actividadTotal;
	}
	
	public float getTotalInvertido() {
		return this.totalInvertido;
	}
	
	public void actualizarTotalInvertido (double monto) {
		this.totalInvertido+=(float) monto;
	}
	
	public void descontarTotalInvertido (double monto) {
		this.totalInvertido-=(float) monto;
	}
	
	public String getDni() {
		return this.dni;
	}
	
	public HashMap<String, Cuenta> getCuenta(){
		return this.cuentas;
	}
}
