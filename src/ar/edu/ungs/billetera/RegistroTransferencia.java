package ar.edu.ungs.billetera;

public class RegistroTransferencia extends Actividad {
	private String cuentaDestino;
	private String usuarioDestino;
	
	public RegistroTransferencia(String usuario, String cuentaOrigen, String usuarioDestino, String cuentaDestino, double monto,  String estado) {
		super (usuario, cuentaOrigen, monto, estado);
		this.usuarioDestino=usuarioDestino;
		this.cuentaDestino=cuentaDestino;
	}

	@Override
	public String toString() {
		return "Transferencia\n"+
				"Fecha: " + getFecha() + 
				"\nOrigen: "+"cvu: [" +getCuentaOrigen()+"] DNI[" + getUsuario()+"] "+
				"\nDestino:  "+ "cvu: [" +this.cuentaDestino + "] DNI[" + this.usuarioDestino +"] "+
				"\nMonto:" + getMonto() +
				"\nEstado: "+getEstado(); 
	}
}
