package fr.lteconsulting.commandes;

import fr.lteconsulting.Commande;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.outils.Saisie;

public class ModifierDisque implements Commande
{
	private Bibliotheque bibliotheque;

	public ModifierDisque( Bibliotheque bibliotheque )
	{
		this.bibliotheque = bibliotheque;
	}

	@Override
	public String getNom()
	{
		return "Modifier un disque";
	}

	@Override
	public void executer()
	{
		String codeBarre = Saisie.saisie( "Code barre du disque à supprimer" );
		String nouveauNom = Saisie.saisie( "Nouveau nom pour le test" );

		bibliotheque.modifierDisque( codeBarre, nouveauNom );
	}
}
