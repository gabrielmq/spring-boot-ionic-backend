package br.com.cursomc.sbinc.domain.enums;

public enum EstadoPagamento {
	
	PENDENTE(1, "Pendente"),
	PAGO(2, "Pago"),
	CANCELADO(3, "Cancelado");
	
	private int cod;
	private String descr;
	
	private EstadoPagamento(int cod, String descr) {
		this.cod = cod;
		this.descr = descr;
	}

	public int getCod() {
		return cod;
	}

	public String getDescr() {
		return descr;
	}
	
	public static EstadoPagamento toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (EstadoPagamento t : EstadoPagamento.values()) {
			if (codigo.equals(t.getCod())) {
				return t;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: "+codigo);
	}
}
