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
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("Cuenta Corporativa");
		sb.append("Alias: ").append(getAlias()).append("\n");
		sb.append("Alias: ").append(getAlias()).append("\n");
		sb.append("Saldo: $").append(getSaldo()).append("\n");
		sb.append("Cuit: ").append(this.cuit).append("\n");
		return sb.toString();
	}
}
