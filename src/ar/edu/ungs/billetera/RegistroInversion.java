package ar.edu.ungs.billetera;

public class RegistroInversion extends Actividad {
	private String tipoInversion;
	private int plazo;
	
	public RegistroInversion (String usuario, String cuentaOrigen, double monto, String estado, String tipoInversion, int plazo) {
		super (usuario, cuentaOrigen, monto, estado);
		this.tipoInversion=tipoInversion;
		this.plazo=plazo;
	}
	
	@Override
	public String toString() {
		return "Inversion:\n"+
				"Fecha: " + getFecha() + 
				"\nOrigen: [" +getCuentaOrigen() +"] ["+ getUsuario()+"] "+
				"\nTipo: "+ this.tipoInversion+
				"\nMonto:" + getMonto() +
				"\nPlazo Dias: "+ this.plazo+
				"\nEstado: "+ getEstado();
	}
}
