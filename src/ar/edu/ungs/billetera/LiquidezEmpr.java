package ar.edu.ungs.billetera;

public class LiquidezEmpr extends Inversion {
	private double interes;
	private double montoMinimo=20000000;
	
	public LiquidezEmpr(String dni, String cvu, double monto, int plazoDias) {
		super(dni, cvu, monto, plazoDias);
		this.interes=0.8;
		if (monto<montoMinimo) {
			throw new RuntimeException ("monto no valido");
		}
	}

	@Override
	public String toString() {
		return "La cuenta " + getCvu()+ " realizo el dia "+ getFecha()+ 
				" una inversion de fondo empresarial por un monto de "+ getMonto() 
				+ " por "+ getPlazoDias()+ "dias";
	}

	@Override
	public double calcularRentabilidad() {
		return getMonto()+(getMonto()*(this.interes/365.0)* getPlazoDias());
	}
	
	@Override
	public void precancelar() {
		throw new RuntimeException ("Las inversiones de Liquidez Empresarial no se precancelan");
	}

	@Override
	public double rentabilidadActual() {
		return getMonto()+ getMonto()*(this.interes/365.0)* diasPasados();
	}

	@Override
	public double totalPrecancelada() {
		throw new RuntimeException ("Las inversiones de Liquidez Empresarial no se precancelan");
	}
}
