package br.com.basecadastro.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
/**
 * Classe responsavel por carregar o id do usuario que esta efetuando alguma operacao (tabela de auditoria)
 * @author 55119
 *
 */
public class UtilFramework implements Serializable {

	private static final long serialVersionUID = 1L;

	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

	public synchronized static ThreadLocal<Long> getThreadLocal() {
		return threadLocal;
	}

}
