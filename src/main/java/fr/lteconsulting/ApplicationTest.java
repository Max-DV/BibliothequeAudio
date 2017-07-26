package fr.lteconsulting;

import java.util.UUID;

import fr.lteconsulting.dao.DisqueDAO;
import fr.lteconsulting.modele.Disque;

public class ApplicationTest
{
	public static void main( String[] args )
	{
		DisqueDAO dao = new DisqueDAO();

		chercherEtAfficherDisque( dao, "pptt" );
		chercherEtAfficherDisque( dao, "ppttdddd" );

		System.out.println( "AJOUT DE DISQUES" );
		for( int i = 0; i < 10; i++ )
		{
			Disque ajout = dao.add( new Disque( "Toto" ) );
			ajout.afficher();
		}

		Disque disque = dao.findById( "pptt" );
		disque.setNom( "DISQUE " + UUID.randomUUID().toString() );
		dao.update( disque );

		System.out.println( "BIBLIOTHEQUE COMPLETE" );
		for( Disque d : dao.findAll() )
			d.afficher();
	}

	private static void chercherEtAfficherDisque( DisqueDAO dao, String id )
	{
		Disque disque = dao.findById( id );
		if( disque != null )
		{
			System.out.println( "Le disque " + id + " a été trouvé :" );
			disque.afficher();
		}
		else
		{
			System.out.println( "Le disque " + id + "n'existe pas" );
		}
	}
}
