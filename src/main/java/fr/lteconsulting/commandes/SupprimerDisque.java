package fr.lteconsulting.commandes;

import fr.lteconsulting.Commande;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Disque;
import fr.lteconsulting.outils.Saisie;

public class SupprimerDisque implements Commande
{
	private Bibliotheque bibliotheque;

	public SupprimerDisque( Bibliotheque bibliotheque )
	{
		this.bibliotheque = bibliotheque;
	}

	@Override
	public String getNom()
	{
		return "Supprimer un disque";
	}

	@Override
	public void executer()
	{
		String codeBarre = Saisie.saisie( "Code barre du disque à supprimer" );

		Disque disque = bibliotheque.rechercherDisqueParCodeBarre( codeBarre );
		if( disque != null )
		{
			System.out.println( "Suppression du disque" );
			disque.afficher( false );
			bibliotheque.supprimerDisque( disque.getCodeBarre() );

			System.out.println( "Disque supprimé" );
		}
		else
		{
			System.out.println( "Le disque n'existe pas, impossible de le supprimer" );
		}
	}
}
