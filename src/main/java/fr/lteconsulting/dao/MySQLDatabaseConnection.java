package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnection
{
	private Connection connection;

	public MySQLDatabaseConnection()
	{
		try
		{
			Class.forName( "com.mysql.jdbc.Driver" );
			connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/bibliotheque_audio", "root", "" );

			connection.setAutoCommit( false );
		}
		catch( ClassNotFoundException e )
		{
			throw new RuntimeException( "Chargement driver failure", e );
		}
		catch( SQLException e )
		{
			throw new RuntimeException( "Impossible d'établir une connection avec le SGBD", e );
		}
	}

	public Connection getConnection()
	{
		return connection;
	}

	public <T> T runInTransaction( InTransactionExecution<T> txProc )
	{
		try
		{
			T result = txProc.execute( connection );
			connection.commit();

			return result;
		}
		catch( Exception e )
		{
			System.out.println( "Une exception s'est produite durant la transaction. On ANNULE la transaction." );

			try
			{
				connection.rollback();
			}
			catch( SQLException rollbackException )
			{
				System.out.println( "ROLLBACK Impossible" );
				rollbackException.printStackTrace();
			}

			throw new RuntimeException( "La transaction a été annulée !", e );
		}
	}
}
