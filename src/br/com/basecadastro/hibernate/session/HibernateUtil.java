package br.com.basecadastro.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * Responsavel por estabelecer a conexão do hibernate
 * 
 */
@ApplicationScoped
public class HibernateUtil implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = Logger.getLogger(HibernateUtil.class);

	private static final String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasouce";

	private static SessionFactory sessionFactory = buildSessionFactory();

	/**
	 * Responsavel por ler o arquivo de configuracao hibernate.cfg.xml
	 * 
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory() {
		try {
			if (sessionFactory == null) {
				sessionFactory = (new Configuration()).configure().buildSessionFactory();
			}
			return sessionFactory;

		} catch (Exception e) {
			logger.info("Erro ao criar conexao SessionFactory", e);
			throw new ExceptionInInitializerError("Erro ao criar conex�o SessionFactory");
		}
	}

	/**
	 * Retorna o sessionFactory corrente.
	 * 
	 * @return sessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Retonar a sess�o do SessionFactory
	 * 
	 * @return Session
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * Abre uma nova sessao no SessionFactory
	 * 
	 * @return Session
	 */
	public static Session openSession() {
		if (sessionFactory == null)
			buildSessionFactory();
		return sessionFactory.openSession();
	}

	/**
	 * Obtem a conection do provedor de conexoes configurado
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnectionProvider() throws SQLException {
		return ((SessionFactoryImplementor) sessionFactory).getConnectionProvider().getConnection();
	}

	/**
	 * 
	 * @return Connection no InitialContext java:/comp/env/jdbc/datasouce
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		return ds.getConnection();
	}


}
