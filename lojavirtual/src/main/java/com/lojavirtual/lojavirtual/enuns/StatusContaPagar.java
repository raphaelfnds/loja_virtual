package com.lojavirtual.lojavirtual.enuns;

public enum StatusContaPagar {

	COBRANCA("Pagar"), VENCIDA("Vencida"), ABERTA("Aberta"), QUITADA("Quitada"), RENEGOCIADA("renegociada");

	private String descricao;

	private StatusContaPagar(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return this.toString();
	}
}
