package ar.edu.ungs.billetera;

public class CuentaPremium extends Cuenta {
	public static float saldoMin = 500000;

	public CuentaPremium(String alias, double depositoInicial) {
		super(alias, (float) depositoInicial);
	}
	
}
