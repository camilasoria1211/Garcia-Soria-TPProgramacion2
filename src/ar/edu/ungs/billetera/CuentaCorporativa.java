package ar.edu.ungs.billetera;

public class CuentaCorporativa extends Cuenta {
	String cuit;

	public CuentaCorporativa(String alias) {
		super(alias);
		this.cuit = cuit;
	}
	
	public String getCuit() {
		return this.cuit;
	}
}
