package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;

public class DisqueDAO
{
	private Connection connection;
	private ChansonDAO chansonDAO;

	public DisqueDAO( MySQLDatabaseConnection connection, ChansonDAO chansonDAO )
	{
		this.connection = connection.getConnection();
		this.chansonDAO = chansonDAO;
	}

	public Disque findById( String id )
	{
		try
		{
			String sql = "SELECT * FROM `disques` WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement( sql );
			statement.setString( 1, id );
			ResultSet resultSet = statement.executeQuery();
			if( resultSet.next() )
				return createDisqueFromResultSet( resultSet );
			else
				return null;
		}
		catch( SQLException e )
		{
			throw new RuntimeException( "Impossible de réaliser l(es) opération(s)", e );
		}
	}

	public List<Disque> findAll()
	{
		try
		{
			List<Disque> disques = new ArrayList<>();

			String sql = "SELECT * FROM `disques`";
			PreparedStatement statement = connection.prepareStatement( sql );
			ResultSet resultSet = statement.executeQuery();
			while( resultSet.next() )
			{
				Disque disque = createDisqueFromResultSet( resultSet );
				disques.add( disque );
			}

			return disques;
		}
		catch( SQLException e )
		{
			throw new RuntimeException( "Impossible de réaliser l(es) opération(s)", e );
		}
	}

	public Disque add( Disque disque )
	{
		try
		{
			// génération d'un identifiant si nécessaire
			String id = disque.getCodeBarre();
			if( id == null )
				id = UUID.randomUUID().toString();

			String sqlQuery = "INSERT INTO disques (`id`, `nom`) VALUES (?, ?)";

			PreparedStatement statement = connection.prepareStatement( sqlQuery );
			statement.setString( 1, id );
			statement.setString( 2, disque.getNom() );

			int nbEnregistrementInseres = statement.executeUpdate();
			if( nbEnregistrementInseres == 0 )
				throw new RuntimeException( "Aucun disque inséré" );

			// met à jour l'objet disque avec l'id généré
			// pour mettre l'appelant au courant de l'id généré
			disque.setCodeBarre( id );

			// insértion en base des chansons du disque
			for( Chanson chanson : disque.getChansons() )
			{
				chanson.setDisqueId( id );
				chansonDAO.add( chanson );
			}

			return disque;
		}
		catch( SQLException e )
		{
			throw new RuntimeException( "Impossible d'ajouter le disque", e );
		}
	}

	public void update( Disque disque )
	{
		try
		{
			String sqlQuery = "UPDATE disques SET `nom` = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement( sqlQuery );
			statement.setString( 1, disque.getNom() );
			statement.setString( 2, disque.getCodeBarre() );

			statement.executeUpdate();

			// TODO mettre a jour en base les chansons du disque => supprimées et ajoutées à gérer
		}
		catch( SQLException e )
		{
			throw new RuntimeException( "Impossible de mettre à jour le disque", e );
		}
	}

	public void delete( String id )
	{
		try
		{
			// effacer toutes les chansons du disque puisqu'on efface le disque
			chansonDAO.deleteByDisqueId( id );

			String sqlQuery = "DELETE FROM disques WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement( sqlQuery );
			statement.setString( 1, id );

			statement.executeUpdate();
		}
		catch( SQLException e )
		{
			throw new RuntimeException( "Impossible de retirer le disque", e );
		}
	}

	private Disque createDisqueFromResultSet( ResultSet resultSet ) throws SQLException
	{
		String id = resultSet.getString( "id" );
		String nom = resultSet.getString( "nom" );

		Disque disque = new Disque();
		disque.setCodeBarre( id );
		disque.setNom( nom );

		List<Chanson> chansons = chansonDAO.findByDisqueId( id );
		for( Chanson chanson : chansons )
			disque.addChanson( chanson );

		return disque;
	}
}
