package fr.lteconsulting;

import fr.lteconsulting.dao.ChansonDAO;
import fr.lteconsulting.dao.DisqueDAO;
import fr.lteconsulting.dao.MySQLDatabaseConnection;
import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;

public class ApplicationTest
{
	public static void main( String[] args )
	{
		MySQLDatabaseConnection databaseConnection = new MySQLDatabaseConnection();

		ChansonDAO chansonDao = new ChansonDAO( databaseConnection );
		DisqueDAO disqueDao = new DisqueDAO( databaseConnection, chansonDao );

		chercherEtAfficherDisque( disqueDao, "pptt" );
		chercherEtAfficherDisque( disqueDao, "ppttdddd" );

		System.out.println( "AJOUT DE DISQUES" );
		for( int i = 0; i < 10; i++ )
		{
			Disque disque = new Disque( "Toto" );
			disque.addChanson( new Chanson( "Salut !", 35 ) );
			disque.addChanson( new Chanson( "Ciao.", 366 ) );

			disqueDao.add( disque );
			disque.afficher();
		}

		Disque disque = disqueDao.findById( "pptt" );
		disque.setNom( "Far Beyond Driven" );
		disqueDao.update( disque );

		System.out.println( "BIBLIOTHEQUE COMPLETE" );
		for( Disque d : disqueDao.findAll() )
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
			System.out.println( "Le disque " + id + " n'existe pas" );
		}
	}
}
