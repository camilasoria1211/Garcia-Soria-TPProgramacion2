package ar.edu.ungs.billetera;

public class RentaFija extends Inversion {
	private double interes;
	private boolean precancelada;
	
	public RentaFija (String dni, String cvu, double monto, int plazoDias) {
		super(dni, cvu, monto, plazoDias);
		this.interes=0.20;
		this.precancelada=false;
	}
	
	@Override
	public String toString() {
		return "La cuenta " + getCvu()+ " realizo el dia "+getFecha()+ 
		" una inversion de renta fija de"+ getMonto() + " por "+ getPlazoDias()+ "dias";
	}
	
	@Override
	public double calcularRentabilidad() {
		return  getMonto()+(getMonto() * (interes/ 365.0) * getPlazoDias());

	}
	
	public boolean getPrecancelada () {
		return precancelada;
	}

	public double rentabilidadActual() {
		return getMonto() * (interes / 365.0) * diasPasados();
	}

	@Override
	public double totalPrecancelada() {
		return getMonto()+(rentabilidadActual()/2.0);
	}
}
