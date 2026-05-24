package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.RuntimeErrorException;

public class Billetera implements IBilletera {
	private HashMap<String, Usuario> usuarios;
	private HashMap<Integer, Inversion> inversiones;
	private ArrayList<Actividad> historialGlobal;
	private HashMap<String, Cuenta> cuentasGlobales;
	private HashMap<String, Empresa> empresas;
	
	public Billetera() {
		this.usuarios = new HashMap<String, Usuario>();
		this.inversiones = new HashMap<Integer, Inversion>();
		this.historialGlobal = new ArrayList<Actividad>();
		this.cuentasGlobales = new HashMap<String, Cuenta>();
	}
	
	private void aliasNoUnico(String alias) {
		for (Cuenta cuenta: this.cuentasGlobales.values()) {
			if (cuenta.getAlias().equals(alias)) {
				throw new RuntimeException("El alias ya está registrado.");
			}
		}
	}

	@Override
	public void registrarEmpresa(String cuit, String nombreFantasia, String telefono, String email,
			String nombreContacto) {
		if (this.empresas.containsKey(cuit)) {
			throw new RuntimeException("Ya existe una empresa registrada con ese cuit.");
		} else if (nombreFantasia.length() < 3 || telefono.length() != 10 || email.length() < 7) {
			throw new RuntimeException("Algún campo es inválido");
		}
		Empresa empresa = new Empresa(cuit, nombreFantasia, telefono, email);
		this.empresas.put(cuit, empresa);
	}

	@Override
	public void agregarPersonaAutorizada(String cuitEmpresa, String dniAutorizado) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registrarUsuario(String dni, String nombre, String telefono, String email) {
		if(dni.length() != 8 || nombre.length() < 3 || telefono.length() < 10 || email.length() < 7) {
			throw new RuntimeException("Algún campo es inválido");
		}
		if (this.usuarios.containsKey(dni)) {
			throw new RuntimeException("Ya existe un usuario registrado con ese dni.");
		}
		Usuario nuevoUsuario = new Usuario(nombre, dni, email, telefono);
		this.usuarios.put(dni, nuevoUsuario);
	}

	@Override
	public String crearCuentaRegular(String dniUsuario, String alias) {
		aliasNoUnico(alias);
		Usuario usuario = this.usuarios.get(dniUsuario);
		if (usuario == null) {
			throw new RuntimeException("No existe ningún usuario con ese dni.");
		}
		CuentaRegular cuenta = new CuentaRegular(alias);
		usuario.registrarCuenta(cuenta);
		this.cuentasGlobales.put(cuenta.getCvu(), cuenta);
	
		return cuenta.getCvu();
	}

	@Override
	public String crearCuentaPremium(String dniUsuario, String alias, double depositoInicial) {
		aliasNoUnico(alias);
		Usuario usuario = this.usuarios.get(dniUsuario);
		if (usuario == null) {
			throw new RuntimeException("No existe ningún usuario con ese dni.");
		} else if (depositoInicial < CuentaPremium.saldoMin) {
			throw new RuntimeException("El monto inicial no es suficiente.");
		}
		CuentaPremium cuenta = new CuentaPremium(alias, depositoInicial);
		usuario.registrarCuenta(cuenta);
		this.cuentasGlobales.put(cuenta.getCvu(), cuenta);
	
		return cuenta.getCvu();
	}

	@Override
	public String crearCuentaCorporativa(String dniUsuario, String alias, String cuitEmpresa) {
		if (!this.usuarios.containsKey(dniUsuario)) {
			throw new RuntimeException("No existe ningún usuario registrado con ese dni.");
		} else if (!this.empresas.containsKey(cuitEmpresa)) {
			throw new RuntimeException("No existe ninguna empresa registrada con ese cuit.");
		}
		aliasNoUnico(alias);
		Empresa empresa = this.empresas.get(cuitEmpresa);
		if (!empresa.estaAutorizado(dniUsuario)) {
			throw new RuntimeException("El usuario no está autorizado a operar en nombre de esta empresa.");
		}
		Usuario usuario = this.usuarios.get(dniUsuario);
		CuentaCorporativa cuenta = new CuentaCorporativa(alias);
		
		usuario.registrarCuenta(cuenta);
		this.cuentasGlobales.put(cuenta.getCvu(), cuenta);
		return cuenta.getCvu();
	}

	@Override
	public List<String> obtenerCuentas(String dniUsuario) {
		Usuario usuario = this.usuarios.get(dniUsuario);
		if (usuario == null) {
			throw new RuntimeException("No existe ningún usuario con ese dni.");
		}
		return usuario.getCuentas();
	}

	@Override
	public double obtenerSaldoDisponible(String cvu) {
		Cuenta cuenta = this.cuentasGlobales.get(cvu);
		if (cuenta == null) {
			throw new RuntimeException("No se encontro ninguna cuenta con ese cvu.");
		}
		return cuenta.getSaldo();
	}

	@Override
	public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
		// TODO Auto-generated method stub

	}

	@Override
	public int realizarInversionRentaFija(String dni, String cvu, double monto, int plazoDias) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int realizarInversionDivisa(String dni, String cvu, double monto, int plazoDias, String divisa,
			double tasa) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int realizarInversionLiquidez(String dni, String cvu, double monto, int plazoDias) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void precancelarInversion(String dni, String cvu, int idInversion) {
		// TODO Auto-generated method stub

	}

	@Override
	public String consultarCvu(String alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> consultarHistorialGlobal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> consultarHistorialCuenta(String cvu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> consultarHistorialUsuario(String dniUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double obtenerTotalInvertido(String dniUsuario) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> cuentasConMayorVolumen(int cantidadTop) {
		// TODO Auto-generated method stub
		return null;
	}

}
