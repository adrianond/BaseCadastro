package br.com.basecadastro.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public enum EstatusPersistencia {
	ERROR("Erro"), SUCESSO("Sucesso"),OBJETO_REFERENCIADO("Esse registro não pode ser apagado por possuir referências ao mesmo.");

	private String name;

}
