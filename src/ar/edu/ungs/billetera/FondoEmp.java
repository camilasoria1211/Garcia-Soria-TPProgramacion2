package ar.edu.ungs.billetera;

public class FondoEmp extends Inversion {
	private double interes;
	
	public FondoEmp(int plazoDias, float monto, String cuentaOrigen, String divisa) {
		super (plazoDias, monto, cuentaOrigen);
		this.interes=0.8;
	}

	@Override
	public String toString() {
		return "La cuenta " + getCuentaOrigen()+ " realizo el dia "+ getFecha()+ 
				" una inversion de fondo empresarial por un monto de "+ getMonto() 
				+ " por "+ getPlazoDias()+ "dias";
	}

	@Override
	public double calcularRentabilidad() {
		long diasPasados = Utilitarios.hoy().toEpochDay()- this.getFecha().toEpochDay();
		return getMonto()*(this.interes/365.0)* diasPasados;
	}
}
