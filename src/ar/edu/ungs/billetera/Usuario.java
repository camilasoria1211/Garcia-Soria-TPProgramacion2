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
	
	public boolean aliasEnUso(String alias) {
		for (Cuenta cuenta: this.cuentas.values()) {
			if (cuenta.aliasEnUso(alias)) {
				return true;
			}
		}
		return false;
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
	
	public ArrayList<String> actividadCuenta(String cvu){
		ArrayList<String> lista= new ArrayList<>();
		Cuenta c= cuentas.get(cvu);
		for (Actividad a: c.getHistorial()) {
			lista.add(a.toString());
		}
		return lista;
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
	
	public boolean tieneCuenta(String cvu) {
		return this.cuentas.containsKey(cvu);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Cliente: ").append(this.nombre).append(" (DNI: ").append(this.dni).append(")\n");
		sb.append("Total Invertido: $").append(this.totalInvertido).append("\n");
		sb.append("Cuentas asociadas:\n");
		
		if (this.cuentas.isEmpty()) {
			sb.append("No hay cuentas registradas");
		} 
		else {
			for (Cuenta cuenta : this.cuentas.values()) {
				sb.append(cuenta.toString()).append("\n");
			}
		}
		return sb.toString();
	}
	
	public boolean perteneceCvu (String cvu) {
		return this.cuentas.containsKey(cvu);
	}
	
	public boolean fondosSuficientes (double monto, String cvu) {
		if (perteneceCvu (cvu) ==false) {
			throw new RuntimeException ("La cuenta no pertenece al usuario");
		}
		return cuentas.get(cvu).fondosSuficientes(monto);		
	}
	
	public void registrarActividad (String cvu, Actividad a) {
		if (perteneceCvu(cvu)==false) {
			throw new RuntimeException ("La cuenta no pertenece al usuario");
		}
		Cuenta c=cuentas.get(cvu);
		c.registrarActividad(a);
	}
	
	public void nuevaInversion (Inversion i, int id, Actividad a, String cvu, double monto) {
		if (perteneceCvu(cvu)==false) {
			throw new RuntimeException ("La cuenta no pertenece al usuario");
		}
		actualizarTotalInvertido(monto);
		Cuenta c= cuentas.get(cvu);
		c.registrarActividad(a);
		c.registrarInversion(id, i);
		c.DebitarMonto(monto);
	}
	
	public void precancelarInversion(String cvu, int id) {
		if (perteneceCvu(cvu)==false) {
			throw new RuntimeException ("La cuenta no pertenece al usuario");
		}
		Cuenta c= cuentas.get(cvu);
		double montoInvertido= c.precancelarInversion(id);
		descontarTotalInvertido(montoInvertido);
	}
	
	public boolean puedeRecibirMonto(String cvuDestino, double monto, RegistroTransferencia actividad) {
		Cuenta cuentaDestino = this.cuentas.get(cvuDestino);
		if (!cuentaDestino.puedeRecibirMonto(monto)) {
			cuentaDestino.registrarActividad(actividad);
			return false;
		}
		return true;
	}
	
	public void acreditarACuenta(String cvuDestino, double monto, RegistroTransferencia actividad) {	
		Cuenta cuentaDestino = this.cuentas.get(cvuDestino);
		cuentaDestino.acreditarMonto(monto);
		cuentaDestino.registrarActividad(actividad);
	}
	
	public void realizarTransferencia(String cvuOrigen, Usuario usuarioDestino, String cvuDestino, double monto) {
		RegistroTransferencia actividad = new RegistroTransferencia(this.dni, cvuOrigen, "No disponible", cvuDestino, monto, "rechazada");
		Cuenta cuentaOrigen = this.cuentas.get(cvuOrigen);
		if (!cuentaOrigen.fondosSuficientes(monto)) {
			cuentaOrigen.registrarActividad(actividad);
			usuarioDestino.registrarActividad(cvuDestino, actividad);
	        throw new RuntimeException("No hay suficiente saldo.");
	    }
		if (!usuarioDestino.puedeRecibirMonto(cvuDestino, monto, actividad)) {
			cuentaOrigen.registrarActividad(actividad);
	        throw new IllegalStateException("La cuenta no puede almacenar la suma total de saldo");
	    }
		cuentaOrigen.DebitarMonto(monto);
		RegistroTransferencia aprobada = new RegistroTransferencia(this.dni, cvuOrigen, "No disponible", cvuDestino, monto, "rechazada");
		cuentaOrigen.registrarActividad(aprobada);
		usuarioDestino.acreditarACuenta(cvuDestino, monto, aprobada);
	}
	
	public double obtenerSaldoDeCuenta(String cvu) {
		Cuenta cuenta = this.cuentas.get(cvu);
		return cuenta.getSaldo();
	}
	
	public String obtenerCvuPorAlias(String alias) {
		for (Cuenta cuenta : this.cuentas.values()) {
            if (cuenta.tieneAlias(alias)) {
                return cuenta.getCvu();
            }
		}
        return null;
	}

	public void realizarInversionRentaFija(String cvu, double monto, int plazoDias, int idInversion) {
		Cuenta cuenta = this.cuentas.get(cvu);
		if (cuenta == null) {
			throw new RuntimeException("la cuenta no pertenece al cliente");			
		}
		if (!cuenta.fondosSuficientes(monto)) {
			RegistroInversion inversionRechazada = new RegistroInversion(this.dni, cvu, monto, "rechazada", "Renta Fija", plazoDias);
			cuenta.registrarActividad(inversionRechazada);
			throw new RuntimeException("no se tienen fondos suficientes");
		}
		RentaFija inversion = new RentaFija(this.dni, cvu, monto, plazoDias);
		RegistroInversion inversionAceptada = new RegistroInversion(this.dni, cvu, monto, "aceptada", "Renta Fija", plazoDias);
		cuenta.DebitarMonto(monto);
		cuenta.registrarActividad(inversionAceptada);
		cuenta.registrarInversion(idInversion, inversion);
		actualizarTotalInvertido(monto);
	}

}
