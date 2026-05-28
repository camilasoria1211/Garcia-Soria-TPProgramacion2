package ar.edu.ungs.billetera;

public class CuentaPremium extends Cuenta {
	public static float saldoMin = 500000;

	public CuentaPremium(String alias, double depositoInicial) {
		super(alias, (float) depositoInicial);
	}
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("Cuenta Premium");
		sb.append("Alias: ").append(getAlias()).append("\n");
		sb.append("Saldo: $").append(getSaldo()).append("\n");		
		return sb.toString();
	}
}
