package ar.edu.ungs.billetera;

public class RentaFija extends Inversion {
	private double interes;
	private boolean precancelada;
	
	public RentaFija (int plazoDias, float monto, String cuentaOrigen) {
		super(plazoDias, monto, cuentaOrigen);
		this.interes=0.20;
		this.precancelada=false;
	}
	
	@Override
	public String toString() {
		return "La cuenta " + getCuentaOrigen()+ " realizo el dia "+getFecha()+ 
		" una inversion de renta fija de"+ getMonto() + " por "+ getPlazoDias()+ "dias";
	}
	
	@Override
	public double calcularRentabilidad(){
		long diasPasados = Utilitarios.hoy().toEpochDay()- this.getFecha().toEpochDay();
		return  getMonto() * (interes / 365) * diasPasados;

	}
	
	public boolean getPrecancelada () {
		return precancelada;
	}

}
