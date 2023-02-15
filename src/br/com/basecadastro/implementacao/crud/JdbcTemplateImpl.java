package br.com.basecadastro.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional (rollbackFor=Exception.class)
public class JdbcTemplateImpl extends JdbcTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	public JdbcTemplateImpl(DataSource dataSource) {
		super(dataSource);
	}

}
