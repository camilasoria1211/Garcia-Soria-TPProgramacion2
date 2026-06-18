package ar.edu.ungs.billetera;

public class CuentaRegular extends Cuenta {
	public static float saldoMax = 5000000;

	public CuentaRegular(String alias) {
		super(alias);
		this.saldoMax = 5000000;
	}
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("Cuenta Regular");
		sb.append("Alias: ").append(getAlias()).append("\n");
		sb.append("Saldo: $").append(getSaldo()).append("\n");		
		return sb.toString();
	}
	
	@Override
	public boolean puedeRecibirMonto(double monto) {
	    return (monto + this.getSaldo()) <= saldoMax;
	}

}
