package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Cuenta {
	private ArrayList<Actividad> historial;
	private HashMap<Integer, Inversion> inversiones;
	private String cvu;
	private String alias;
	private double saldo; //ANTES FLOAT
	
	public Cuenta(String alias) {
		this.historial = new ArrayList<>();
		this.inversiones = new HashMap<>();
		
		this.cvu = Utilitarios.generarSiguienteCvu();
		this.alias = alias;
		this.saldo = 0;
	}
	
	public Cuenta(String alias, float depositoInicial) {
		this.historial = new ArrayList<>();
		this.inversiones = new HashMap<>();
		
		this.cvu = Utilitarios.generarSiguienteCvu();
		this.alias = alias;
		this.saldo = depositoInicial;
	}
	
	public boolean aliasEnUso(String alias) {
		return this.alias.equals(alias);
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public String getAlias() {
		return this.alias;
	}
	
	public String getCvu() {
		return this.cvu;
	}
	
	public ArrayList<Actividad> getHistorial(){
		return this.historial;
	}
	
	public int cantidadHistorial() {
		return this.historial.size();
	}
	
	public void acreditarMonto(double monto) {
		this.saldo += monto;
	}
	
	public void DebitarMonto(double monto) {
		this.saldo -=  monto;
	}	

	public HashMap<Integer,Inversion> getInversiones(){
		return this.inversiones;
	}
	
	public boolean fondosSuficientes (double monto) {
		return this.saldo>=monto;
	}
	
	public void registrarActividad (Actividad a) {
		this.historial.add(a);
	}
	
	public void registrarInversion(int id, Inversion i) {
		this.inversiones.put (id, i);
	}
	
	public double precancelarInversion (int id) {
		if (!this.inversiones.containsKey(id)) {
	        throw new RuntimeException("La inversión no existe en esta cuenta");
	    }
		Inversion i = inversiones.get(id);
		i.precancelar();
		acreditarMonto(i.totalPrecancelada());
		double montoInvertido = i.getMonto();
		return montoInvertido;
	}
	
	public boolean puedeRecibirMonto(double monto) {
	    return true;
	}
	
	public boolean tieneAlias(String alias) {
        return this.alias.equals(alias);
	}
}
