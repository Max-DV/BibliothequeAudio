package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import fr.lteconsulting.modele.Chanson;

public class ChansonDAO
{
	private MySQLDatabaseConnection connection;

	public ChansonDAO( MySQLDatabaseConnection connection )
	{
		this.connection = connection;
	}

	public Chanson findById( String id )
	{
		return connection.runInTransaction( new InTransactionExecution<Chanson>()
		{
			@Override
			public Chanson execute( Connection connection ) throws Exception
			{
				String sql = "SELECT * FROM `chansons` WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement( sql );
				statement.setString( 1, id );
				ResultSet resultSet = statement.executeQuery();
				if( resultSet.next() )
					return createChansonFromResultSet( resultSet );
				else
					return null;
			}
		} );
	}

	public List<Chanson> findAll()
	{
		return connection.runInTransaction( new InTransactionExecution<List<Chanson>>()
		{
			@Override
			public List<Chanson> execute( Connection connection ) throws Exception
			{
				List<Chanson> chansons = new ArrayList<>();

				String sql = "SELECT * FROM `chansons`";
				PreparedStatement statement = connection.prepareStatement( sql );
				ResultSet resultSet = statement.executeQuery();
				while( resultSet.next() )
					chansons.add( createChansonFromResultSet( resultSet ) );

				return chansons;
			}
		} );
	}

	public List<Chanson> findByDisqueId( String disqueId )
	{
		return connection.runInTransaction( new InTransactionExecution<List<Chanson>>()
		{
			@Override
			public List<Chanson> execute( Connection connection ) throws Exception
			{
				List<Chanson> chansons = new ArrayList<>();

				String sql = "SELECT * FROM `chansons` WHERE `disque_id` = ?";
				PreparedStatement statement = connection.prepareStatement( sql );
				statement.setString( 1, disqueId );
				ResultSet resultSet = statement.executeQuery();
				while( resultSet.next() )
					chansons.add( createChansonFromResultSet( resultSet ) );

				return chansons;
			}
		} );
	}

	public Chanson add( Chanson chanson )
	{
		if( chanson.getDisqueId() == null )
			throw new RuntimeException( "Impossible d'ajouter une chanson sans connaître son disque !" );

		return connection.runInTransaction( new InTransactionExecution<Chanson>()
		{

			@Override
			public Chanson execute( Connection connection ) throws Exception
			{
				String sqlQuery = "INSERT INTO chansons (`disque_id`, `nom`, `duree`) VALUES (?, ?, ?)";

				PreparedStatement statement = connection.prepareStatement( sqlQuery, Statement.RETURN_GENERATED_KEYS );
				statement.setString( 1, chanson.getDisqueId() );
				statement.setString( 2, chanson.getNom() );
				statement.setInt( 3, chanson.getDureeEnSecondes() );

				int nbEnregistrementInseres = statement.executeUpdate();
				if( nbEnregistrementInseres == 0 )
					throw new RuntimeException( "Aucune chanson insérée" );

				ResultSet createdIds = statement.getGeneratedKeys();
				if( createdIds.next() )
				{
					chanson.setId( createdIds.getInt( 1 ) );
					return chanson;
				}

				throw new RuntimeException( "Aucun chanson ajoutée" );
			}
		} );
	}

	public void update( Chanson chanson )
	{
		if( chanson.getDisqueId() == null )
			throw new RuntimeException( "Impossible de modifier une chanson sans connaître son disque !" );

		connection.runInTransaction( new InTransactionExecution<Void>()
		{
			@Override
			public Void execute( Connection connection ) throws Exception
			{
				String sqlQuery = "UPDATE chansons SET `disque_id` = ?, `nom` = ?, `duree` = ? WHERE id = ?";

				PreparedStatement statement = connection.prepareStatement( sqlQuery );
				statement.setString( 1, chanson.getDisqueId() );
				statement.setString( 2, chanson.getNom() );
				statement.setInt( 3, chanson.getDureeEnSecondes() );
				statement.setInt( 4, chanson.getId() );

				statement.executeUpdate();

				return null;
			}
		} );
	}

	public void delete( int id )
	{
		connection.runInTransaction( new InTransactionExecution<Void>()
		{
			@Override
			public Void execute( Connection connection ) throws Exception
			{
				String sqlQuery = "DELETE FROM chansons WHERE id = ?";

				PreparedStatement statement = connection.prepareStatement( sqlQuery );
				statement.setInt( 1, id );

				statement.executeUpdate();

				return null;
			}
		} );
	}

	public void deleteByDisqueId( String disqueId )
	{
		connection.runInTransaction( new InTransactionExecution<Void>()
		{
			@Override
			public Void execute( Connection connection ) throws Exception
			{
				String sqlQuery = "DELETE FROM chansons WHERE disque_id = ?";

				PreparedStatement statement = connection.prepareStatement( sqlQuery );
				statement.setString( 1, disqueId );

				statement.executeUpdate();

				return null;
			}
		} );
	}

	private Chanson createChansonFromResultSet( ResultSet resultSet ) throws SQLException
	{
		int id = resultSet.getInt( "id" );
		String disqueId = resultSet.getString( "disque_id" );
		String nom = resultSet.getString( "nom" );
		int duree = resultSet.getInt( "duree" );

		Chanson chanson = new Chanson();

		chanson.setId( id );
		chanson.setNom( nom );
		chanson.setDureeEnSecondes( duree );
		chanson.setDisqueId( disqueId );

		return chanson;
	}
}
