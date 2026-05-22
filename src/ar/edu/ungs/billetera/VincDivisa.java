package ar.edu.ungs.billetera;

public class VincDivisa extends Inversion{
	private String divisa;
	private double interes;
	private boolean preCancelada;
	
	public VincDivisa (int plazoDias, float monto, String cuentaOrigen, String divisa) {
		super (plazoDias, monto, cuentaOrigen);
		this.divisa=divisa;		
		this.preCancelada=false;

	}

	@Override
	public String toString() {
		return "La cuenta " + getCuentaOrigen()+ " realizo el dia "+ getFecha()+ 
		" una inversion vinculada a la divisa"+ getDivisa()+" por un monto de "+ getMonto() 
		+ " por "+ getPlazoDias()+ "dias";
	}
	
	@Override
	public double calcularRentabilidad() {
	    long diasPasados = Utilitarios.hoy().toEpochDay()- this.getFecha().toEpochDay();
	    double valorActualDivisa= Utilitarios.consultarCotizacion(divisa);
	    double precioEnDivisa= getMonto()/valorActualDivisa;
	    return precioEnDivisa * (this.interes / 365.0) * diasPasados;
	}
	
	public String getDivisa() {
		return divisa;
	}
}
