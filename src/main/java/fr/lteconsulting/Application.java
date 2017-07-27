package fr.lteconsulting;

import fr.lteconsulting.dao.ChansonDAO;
import fr.lteconsulting.dao.DisqueDAO;
import fr.lteconsulting.dao.MySQLDatabaseConnection;
import fr.lteconsulting.modele.Bibliotheque;

public class Application
{
	public static void main( String[] args )
	{
		MySQLDatabaseConnection databaseConnection = new MySQLDatabaseConnection();
		ChansonDAO chansonDao = new ChansonDAO( databaseConnection );
		DisqueDAO disqueDao = new DisqueDAO( databaseConnection, chansonDao );

		Bibliotheque b = new Bibliotheque( disqueDao );

		InferfaceUtilisateur ui = new InferfaceUtilisateur( b );

		ui.execute();
	}
}
