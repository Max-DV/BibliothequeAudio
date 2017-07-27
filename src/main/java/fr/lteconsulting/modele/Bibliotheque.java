package fr.lteconsulting.modele;

import java.util.ArrayList;
import java.util.List;

import fr.lteconsulting.dao.DisqueDAO;

public class Bibliotheque
{
	private DisqueDAO disqueDao;

	public Bibliotheque( DisqueDAO disqueDao )
	{
		this.disqueDao = disqueDao;
	}

	public void ajouterDisque( Disque disque )
	{
		disqueDao.add( disque );
	}

	public List<Disque> getDisques()
	{
		return disqueDao.findAll();
	}

	public Disque rechercherDisqueParCodeBarre( String codeBarre )
	{
		return disqueDao.findById( codeBarre );
	}

	public List<Disque> rechercherDisqueParNom( String recherche )
	{
		recherche = recherche.toLowerCase();

		List<Disque> resultat = new ArrayList<>();

		// TODO pour optimiser la recherche, nous devrions la faire faire à MySQL (ajouter une méthode au DAO)
		for( Disque disque : disqueDao.findAll() )
		{
			if( disque.getNom().toLowerCase().contains( recherche ) )
				resultat.add( disque );
		}

		return resultat;
	}

	public List<Disque> rechercherDisqueParNom( List<String> termes )
	{
		List<Disque> resultat = new ArrayList<>();

		// TODO pour optimiser la recherche, nous devrions la faire faire à MySQL (ajouter une méthode au DAO)
		for( Disque disque : disqueDao.findAll() )
		{
			boolean estValide = true;
			for( String terme : termes )
			{
				if( !disque.getNom().toLowerCase().contains( terme.toLowerCase() ) )
				{
					estValide = false;
					break;
				}
			}

			if( estValide )
				resultat.add( disque );
		}

		return resultat;
	}

	public void afficher()
	{
		List<Disque> disques = disqueDao.findAll();

		System.out.println( "BIBLIOTHEQUE avec " + disques.size() + " disques" );
		for( Disque disque : disques )
			disque.afficher();
	}
}
