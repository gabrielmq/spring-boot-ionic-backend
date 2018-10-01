package br.com.cursomc.sbinc.domain.enums;

import java.util.Optional;

public enum TipoCliente {

	PESSOAFISICA(1, "Pessoa Física"),
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private int cod;
	private String descr;
	
	private TipoCliente(int cod, String descr) {
		this.cod = cod;
		this.descr = descr;
	}

	public int getCod() {
		return cod;
	}

	public String getDescr() {
		return descr;
	}
	
	public static TipoCliente toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (TipoCliente t : TipoCliente.values()) {
			if (codigo.equals(t.getCod())) {
				return t;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: "+codigo);
	}
}
