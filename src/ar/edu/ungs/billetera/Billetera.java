package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Billetera implements IBilletera {
	private HashMap<String, Usuario> usuarios;
	private HashMap<Integer, Inversion> inversiones;
	private HashMap<String, Empresa> empresas;
	private int contadorInversiones;
	
	public Billetera() {
		this.usuarios = new HashMap<String, Usuario>();
		this.inversiones = new HashMap<Integer, Inversion>();
		this.empresas = new HashMap<String, Empresa>();
		this.contadorInversiones=1;

	}
	
	private void aliasNoUnico(String alias) {
		for (Usuario usuario: this.usuarios.values()) {
			if (usuario.aliasEnUso(alias)) {
				throw new RuntimeException("El alias ya se encuentra en uso");
			}
		}
	}

	@Override
	public void registrarEmpresa(String cuit, String nombreFantasia, String telefono, String email,
			String nombreContacto) {
		if (this.empresas.containsKey(cuit)) {
			throw new RuntimeException("Ya existe una empresa registrada con ese cuit.");
		} else if (nombreFantasia == null || telefono == null || email == null) {
			throw new RuntimeException("Algún campo es inválido");
		}
		Empresa empresa = new Empresa(cuit, nombreFantasia, telefono, email);
		this.empresas.put(cuit, empresa);
	}

	@Override
	public void agregarPersonaAutorizada(String cuitEmpresa, String dniAutorizado) {
		if (!this.empresas.containsKey(cuitEmpresa)) {
			throw new RuntimeException("No existe ninguna empresa registrada con ese cuit.");
		}
		Empresa empresa = this.empresas.get(cuitEmpresa);
		if (empresa.estaAutorizado(dniAutorizado) ) { 
			throw new RuntimeException("El dni ya se encuentra autorizado.");
		}
		empresa.agregarAutorizado(dniAutorizado);
	}

	@Override
	public void registrarUsuario(String dni, String nombre, String telefono, String email) {
		if(dni == null || nombre == null || telefono == null || email == null) {
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
			throw new IllegalArgumentException("El monto inicial no es suficiente.");
		}
		CuentaPremium cuenta = new CuentaPremium(alias, depositoInicial);
		usuario.registrarCuenta(cuenta);
		this.cuentasGlobales.put(cuenta.getCvu(), cuenta);
	
		return cuenta.getCvu();
	}

	@Override
	public String crearCuentaCorporativa(String dniUsuario, String alias, String cuitEmpresa) {
		if (!this.empresas.containsKey(cuitEmpresa)) {
			throw new RuntimeException("No existe ninguna empresa registrada con ese cuit.");
		}
		aliasNoUnico(alias);
		Empresa empresa = this.empresas.get(cuitEmpresa);
		if (!empresa.estaAutorizado(dniUsuario)) {
			throw new RuntimeException("El usuario no está autorizado a operar en nombre de esta empresa.");
		}
		CuentaCorporativa cuenta = new CuentaCorporativa(alias);
		this.cuentasGlobales.put(cuenta.getCvu(), cuenta);
		empresa.registrarCuenta(cuenta);
		return cuenta.getCvu();
	}

	@Override
	public List<String> obtenerCuentas(String dniUsuario) {
		ArrayList<String> cuentas = new ArrayList<>();
		if (this.usuarios.containsKey(dniUsuario)) {
			Usuario usuario = this.usuarios.get(dniUsuario);
			cuentas.addAll(usuario.getCuentas());
		}	
		for (Empresa empresa : this.empresas.values()) {
			if (empresa.estaAutorizado(dniUsuario)) {
				cuentas.addAll(empresa.getCuentas());
			}
		}
		if (cuentas.isEmpty()) {
			throw new RuntimeException("No existe ningún usuario con ese dni.");
		}
		return cuentas;
	}

	@Override
	public double obtenerSaldoDisponible(String cvu) {
		Cuenta cuenta = this.cuentasGlobales.get(cvu);
		if (cuenta == null) {
			throw new RuntimeException("No se encontro ninguna cuenta con ese cvu.");
		}
		return cuenta.getSaldo();
	}
	
	private String obtenerDNICuenta(String cvu) {
		String dniCuenta=null;
		for (Usuario usuario : this.usuarios.values()) {
	        if (usuario.tieneCuenta(cvu)) {
	            dniCuenta = usuario.getDni(); 	           
	        }
	    }
		return dniCuenta;
	}

	/*
	@Override
	public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
		if (!this.cuentasGlobales.containsKey(cvuOrigen)) {
			throw new RuntimeException("No existe ninguna cuenta con el cvu de origen dado.");
		} else if (!this.cuentasGlobales.containsKey(cvuDestino)) {
			throw new RuntimeException("No existe ninguna cuenta con el cvu de destino dado.");
		}
		Cuenta cuentaOrigen = this.cuentasGlobales.get(cvuOrigen);
		String dniOrigen = obtenerDNICuenta(cvuOrigen);
		Cuenta cuentaDestino = this.cuentasGlobales.get(cvuDestino);	
		String dniDestino = obtenerDNICuenta(cvuDestino);
		if (cuentaDestino instanceof CuentaRegular) {
			if (monto + cuentaDestino.getSaldo()>5000000) {
				RegistroTransferencia nuevaActividad= new RegistroTransferencia (dniOrigen, cvuOrigen, dniDestino, cvuDestino, monto, "rechazada");	
				historialGlobal.add(nuevaActividad);
				cuentaOrigen.getHistorial().add(nuevaActividad);
				cuentaDestino.getHistorial().add(nuevaActividad);
				throw new IllegalStateException ("La cuenta no puede almacenar la suma total de saldo");
			}
		}
		if (cuentaOrigen.getSaldo() < monto) {
			RegistroTransferencia nuevaActividad= new RegistroTransferencia (dniOrigen, cvuOrigen, dniDestino, cvuDestino, monto, "rechazada");	
			historialGlobal.add(nuevaActividad);
			cuentaOrigen.getHistorial().add(nuevaActividad);
			cuentaDestino.getHistorial().add(nuevaActividad);
			throw new RuntimeException("No hay suficiente saldo.");
		} else {
			cuentaOrigen.DebitarMonto(monto);
			cuentaDestino.acreditarMonto(monto);	
			RegistroTransferencia nuevaActividad= new RegistroTransferencia (dniOrigen, cvuOrigen, 
					dniDestino, cvuDestino, monto, "aceptada");	
			historialGlobal.add(nuevaActividad);
			cuentaOrigen.getHistorial().add(nuevaActividad);
			cuentaDestino.getHistorial().add(nuevaActividad);
		}
	}
	*/
	
	public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
		Usuario usuarioOrigen = null;
		Usuario usuarioDestino = null;
		for (Usuario usuario : this.usuarios.values()) {
	        if (usuario.tieneCuenta(cvuOrigen)) {
	            usuarioOrigen = usuario;
	        }
	    	if (usuarioOrigen == null) {
	    		throw new RuntimeException("No existe ninguna cuenta con el cvu de origen dado.");
	    	}
	        if (usuario.tieneCuenta(cvuDestino)) {
	            usuarioDestino = usuario;
	        }
	        if (usuarioDestino == null) {
	        	throw new RuntimeException("No existe ninguna cuenta con el cvu de destino dado.");
	        }
	    }
		usuarioOrigen.realizarTransferencia(cvuOrigen, usuarioDestino, cvuDestino, monto);
	}

	@Override
	public int realizarInversionRentaFija(String dni, String cvu, double monto, int plazoDias) {
		if (dni==null || cvu == null || monto <= 0 || plazoDias<=0) {
			throw new RuntimeException ("Algun campo es invalido");
		}
		else if (!this.usuarios.containsKey(dni)) {
			throw new RuntimeException ("No existe ningun cliente con ese DNI");
		}
		Usuario usuario= this.usuarios.get(dni);
		if (usuario.perteneceCvu(cvu)==false) {
			throw new RuntimeException ("la cuenta no pertenece al cliente");			
		}
		if (usuario.fondosSuficientes(monto, cvu)==false) {
			RegistroInversion nuevaInversion = new RegistroInversion (dni, cvu, monto, "rechazado","Renta Fija",  plazoDias);
			usuario.registrarActividad (cvu, nuevaInversion);
			throw new RuntimeException ("no se tienen fondos suficientes");
		}
		RentaFija inversion= new RentaFija(dni, cvu, monto, plazoDias);
		RegistroInversion nuevaInversion = new RegistroInversion (dni, cvu, monto, "aceptado","Renta Fija",  plazoDias);
		int idInversion=this.contadorInversiones;
		usuario.nuevaInversion(inversion, idInversion, nuevaInversion, cvu, monto);
		this.contadorInversiones++;
		return idInversion;
	}

	@Override
	public int realizarInversionDivisa(String dni, String cvu, double monto, int plazoDias, String divisa,
			double tasa) {
		if (dni==null || cvu==null || monto<=0 || plazoDias<=0 || divisa==null || tasa<=0) {
			throw new RuntimeException ("Algun campo es invalido");			
		}
		else if (!this.usuarios.containsKey(dni)) {
			throw new RuntimeException ("No existe ningun cliente con ese DNI");
		}
		Usuario usuario= this.usuarios.get(dni);

		if(usuario.perteneceCvu(cvu)==false) {
			throw new RuntimeException ("Esa cuenta no corresponde al usuario");
		}
		if (usuario.fondosSuficientes(monto, cvu)==false) {
			RegistroInversion nuevaInversion = new RegistroInversion (dni, cvu, monto, "rechazado","Inversion en Divisa",  plazoDias);
			usuario.registrarActividad(cvu, nuevaInversion);
			throw new RuntimeException ("La cuenta no tiene saldo suficiente para realizar esta inversion");
		}
		VincDivisa inversion= new VincDivisa (dni, cvu, monto, plazoDias, divisa, tasa);
		int idInversion=this.contadorInversiones;
		RegistroInversion nuevaInversion = new RegistroInversion (dni, cvu, monto, "aprobado","Renta Fija",  plazoDias);
		usuario.nuevaInversion(inversion, idInversion, nuevaInversion, cvu, monto);
		usuario.registrarActividad(cvu, nuevaInversion);
		this.contadorInversiones++;
		return idInversion;
	}

	@Override
	public int realizarInversionLiquidez(String dni, String cvu, double monto, int plazoDias) {
		if (dni==null || cvu == null || monto <= 0 || plazoDias<=0) {
			throw new RuntimeException ("Algun campo es invalido");
		}
		else if (!this.usuarios.containsKey(dni)) {
			throw new RuntimeException ("No existe ningun cliente con ese DNI");
		}

		Usuario usuario= this.usuarios.get(dni);
		Cuenta cuentaOperacion= this.cuentasGlobales.get(cvu);
		if (!(cuentaOperacion instanceof CuentaCorporativa)) {
			throw new IllegalArgumentException ("El tipo de cuenta no esta autorizado a hacer esta operacion");
		}
		if (usuario.fondosSuficientes(monto, cvu)==false) {
			RegistroInversion nuevaInversion = new RegistroInversion (dni, cvu, monto, "rechazado","Inversion de Liquidez",  plazoDias);
			usuario.registrarActividad(cvu, nuevaInversion);
			throw new RuntimeException ("La cuenta no tiene saldo suficiente para realizar esta inversion");			
		}
		LiquidezEmpr inversion= new LiquidezEmpr(dni, cvu, monto, plazoDias);
		int idInversion=this.contadorInversiones;	
		RegistroInversion nuevaInversion = new RegistroInversion (dni, cvu, monto, "aceptado","Inversion de Liquidez",  plazoDias);
		usuario.nuevaInversion(inversion, idInversion, nuevaInversion, cvu, monto);
		usuario.registrarActividad(cvu, nuevaInversion);
		this.contadorInversiones++;
		return idInversion;
	}

	@Override
	public void precancelarInversion(String dni, String cvu, int idInversion) {
		if (dni==null || cvu==null) {
			throw new RuntimeException ("Un parametro esta incompleto");			
		}
		if(idInversion<=0) {
			throw new IllegalArgumentException ("El ID no es valido");
		}
		if (!this.usuarios.containsKey(dni)) {
			throw new RuntimeException ("No existe ningun cliente con ese DNI");
		}
		Usuario usuario= this.usuarios.get(dni);
		if (usuario.perteneceCvu(cvu)==false) {
			throw new RuntimeException ("La cuenta no pertenece al usuario");
		}
		usuario.precancelarInversion(cvu, idInversion);
	}

	@Override
	public String consultarCvu(String alias) {
		if (alias == null) {
			throw new IllegalArgumentException("El alias es inválido.");
		}
		for (Cuenta cuenta : this.cuentasGlobales.values()) {
			if (cuenta != null && alias.equals(cuenta.getAlias())) {
				return cuenta.getCvu();
			}
		}
		throw new IllegalArgumentException("No existe ninguna cuenta asociada a ese alias.");
	}

	@Override
	public List<String> consultarHistorialGlobal() {
		List <String> consultarHistorialGlobal= new ArrayList<String>();
		if (this.historialGlobal ==  null) {
			return consultarHistorialGlobal;
		}
		for (Usuario u: this.usuarios.values()) {
			consultarHistorialGlobal.add(u.getActividades().toString());
		}
		return consultarHistorialGlobal;
	}

	@Override
	public List<String> consultarHistorialCuenta(String cvu) {
		for (Usuario u: this.usuarios.values()) {
			if (u.perteneceCvu(cvu)) {
				return u.actividadCuenta(cvu);
			}
		}
		throw new RuntimeException ("no existe cvu en el sistema");
	}

	@Override
	public List<String> consultarHistorialUsuario(String dniUsuario) {
		if (!this.usuarios.containsKey(dniUsuario)) {
			throw new RuntimeException("No existe ningún usuario registrado con ese dni.");
		}
		Usuario usuario= this.usuarios.get(dniUsuario);
		List <String> historialUsuario= new ArrayList<String>();
		if (usuario.getActividades()==null) {
			return historialUsuario;
		}
		for (Actividad a: usuario.getActividades()) {
			historialUsuario.add(a.toString());
		}
		
		return historialUsuario;
	}

	@Override
	public double obtenerTotalInvertido(String dniUsuario) {
		if (!this.usuarios.containsKey(dniUsuario)) {
			throw new RuntimeException("No existe ningún usuario registrado con ese dni.");
		}
		Usuario usuario = this.usuarios.get(dniUsuario);
		return usuario.getTotalInvertido();
	}

	@Override
	public List<String> cuentasConMayorVolumen(int cantidadTop) {
		HashMap<String,Integer> volumenCuentas= new HashMap<>();
		for (Cuenta c: this.cuentasGlobales.values()) {
			volumenCuentas.put(c.getCvu(), c.getHistorial().size());
		}
		List<Map.Entry<String, Integer>> listaAux = new ArrayList<>(volumenCuentas.entrySet());
		List<String> mayorVolumen = new ArrayList<>();
		for (int i = 0; i < cantidadTop; i++) {
			String mejorCvu = quitarMaximaVol(listaAux);
			
			if (mejorCvu != null) {
				mayorVolumen.add(mejorCvu);
			}
		}
		return mayorVolumen;
		}
	private String quitarMaximaVol (List<Map.Entry<String, Integer>> lista) {
		if (lista.isEmpty()) {
			return null;
		}
		int maximo = 0;

		for (int i = 1; i < lista.size(); i++) {
			if (lista.get(i).getValue() > lista.get(maximo).getValue()) {
				maximo = i;
			}
		}
		return lista.remove(maximo).getKey();
	}
	
	@Override 
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("\n-------------------------------------------------\n");
		sb.append("      ESTADO BILLETERA      \n");
		sb.append("\n-------------------------------------------------\n");
		sb.append("Cantidad usuarios registrados: ").append(this.usuarios.size()).append("\n");
		sb.append("Cantidad cuentas registradas: ").append(cuentasGlobales.size()).append("\n");
		sb.append("Actividad global total: ").append(this.historialGlobal.size()).append("\n");
		sb.append("\n-------------------------------------------------\n");
		sb.append("USUARIOS: \n");
		for (Usuario u: this.usuarios.values()) {
			sb.append(u.toString()).append("\n");
			sb.append("\n-------------------------------------------------\n");
		}
		return sb.toString();	
		
	}

}
