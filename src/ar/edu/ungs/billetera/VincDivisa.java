package ar.edu.ungs.billetera;

public class VincDivisa extends Inversion{
	private String divisa;
	private double tasa;
	private double cotizacionInicial; 
	
	public VincDivisa (String dni, String cvu, double monto, int plazoDias, String divisa, double tasa) {
		super(dni, cvu, monto, plazoDias);
		this.divisa=divisa;		
		this.tasa=tasa;
		this.cotizacionInicial=Utilitarios.consultarCotizacion(divisa);

	}

	@Override
	public String toString() {
		return "La cuenta " + getCvu()+ " realizo el dia "+ getFecha()+ 
		" una inversion vinculada a la divisa"+ getDivisa()+" por un monto de "+ getMonto() 
		+ " por "+ getPlazoDias()+ "dias";
	}
	
	@Override
	public double calcularRentabilidad() {
	    double divisasEquivalente = this.getMonto() / this.cotizacionInicial;
	    
	    double interesFinal = divisasEquivalente * (this.tasa / 365.0) * this.getPlazoDias();
	    
	    double valorActualDivisa = Utilitarios.consultarCotizacion(this.divisa);
	    return (divisasEquivalente + interesFinal) * valorActualDivisa;
	}
	
	public String getDivisa() {
		return divisa;
	}
	
	@Override
	public double totalPrecancelada() {
		double valorActualDivisa = Utilitarios.consultarCotizacion(this.divisa);
		 double montoHoyEnPesos= (this.getMonto() / this.cotizacionInicial) * valorActualDivisa;
		return montoHoyEnPesos + (rentabilidadActual()/2);
	}
//	@Override
	public double rentabilidadActual() {
	    double divisasEquivalentes = this.getMonto() / this.cotizacionInicial;;
	    double interesActual=  divisasEquivalentes * (this.tasa / 365.0) * diasPasados();
	    double valorActualDivisa = Utilitarios.consultarCotizacion(this.divisa);
	    return interesActual*valorActualDivisa;
	}
}
