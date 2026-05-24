package ar.edu.ungs.billetera;

public class CuentaRegular extends Cuenta {
	public static float saldoMax = 5000000;

	public CuentaRegular(String alias) {
		super(alias);
		this.saldoMax = 5000000;
	}

}
