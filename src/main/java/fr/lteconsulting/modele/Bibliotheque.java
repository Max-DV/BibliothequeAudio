package fr.lteconsulting.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void supprimerDisque( String id )
	{
		disqueDao.delete( id );
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
		return disqueDao.findByName( recherche );
	}

	public List<Disque> rechercherDisqueParNom( List<String> termes )
	{
		Map<String, Disque> validDisques = new HashMap<>();

		for( String terme : termes )
		{
			for( Disque disque : disqueDao.findByName( terme ) )
				validDisques.put( disque.getCodeBarre(), disque );
		}

		return new ArrayList<>( validDisques.values() );
	}

	public void afficher()
	{
		List<Disque> disques = disqueDao.findAll();

		System.out.println( "BIBLIOTHEQUE avec " + disques.size() + " disques" );
		for( Disque disque : disques )
			disque.afficher();
	}
}
