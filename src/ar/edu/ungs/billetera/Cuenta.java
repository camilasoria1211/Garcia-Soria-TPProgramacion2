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
	
	public void acreditarMonto(double monto) {
		this.saldo += monto;
	}
	
	public void DebitarMonto(double monto) {
		this.saldo -=  monto;
	}	
	/*
	public ArrayList<Inversion> getInversion(){
		
	}*/
	public HashMap<Integer,Inversion> getInversiones(){
		return this.inversiones;
	}
	
}
