package dom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class main {
	public static void main(final String[] args) {
		Node prenom;
		Node nom;
		String mdd =""; //contient la chaîne de caractères correspondant à la balise md
		String organeRef=""; //code
		String debut="";
		String legislature ="";
		String fin ="";
		String pub ="";
		String libelle;
		String uidMandat="";
		List<String> mdPresident = new ArrayList<String>();//contiendra les différents mandats 
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\">";
		System.out.println(xmlStr);
		String doctype = "<!DOCTYPE nantais SYSTEM \"ex.dtd\">";
		System.out.println(doctype);
		String baliseNantais = "<nantais>";
		System.out.println(baliseNantais);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// on souhaite ignorer les éléments textes
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		try {
			factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			ErrorHandler errHandler = new SimpleErrorHandler();
			builder.setErrorHandler(errHandler);

			// chargement du fichier historique.xml
			Document document = builder.parse(new File("historique.xml"));

			Node racine = document.getDocumentElement();
			Node organes = racine.getFirstChild();
			Node acteurs = organes.getNextSibling();
			NodeList acteur = acteurs.getChildNodes();

			// affichage de la racine du document xml export
			// System.out.println("<" + racine.getNodeName() + ">");

			// parcours de chaque personne
			int nbActeur = acteur.getLength();
			for (int i = 0; i < nbActeur; i++) {
				// List<String> mdPresident = new ArrayList<String>(); //contiendra les
				// différents mandats
				Node etatCivil = acteur.item(i).getFirstChild().getNextSibling();
				Node infoNais = etatCivil.getFirstChild().getNextSibling();
				Node villeNais = infoNais.getFirstChild().getNextSibling();
				prenom = etatCivil.getFirstChild().getFirstChild().getNextSibling();
				nom = prenom.getNextSibling();
				
				// sélection de la ville de Nantes
				if (villeNais.getTextContent().equals("Nantes")) {
					NodeList mandat = acteur.item(i).getLastChild().getChildNodes(); // mandats de l'acteur
					
					// parcours de chaque mandat de la personne nantaise
					for (int m = 0; m < mandat.getLength(); m++) { // m = mandat
						// contient l'ensemble du contenu du noeud mandat
						NodeList contenuMandat = mandat.item(m).getChildNodes(); // contenu du mandat
						int nbContenuMandat = contenuMandat.getLength();
						
						// parcourt de l'ensemble des noeuds fils de mandat
						for (int c = 0; c < nbContenuMandat; c++) { // c = contenu
							// on sélectionne le noeud "infosQualite"
							if (contenuMandat.item(c).getNodeName().equals("infosQualite")
									&& contenuMandat.item(c).getNodeType() == Node.ELEMENT_NODE) {
								// on se positionne dans le noeud infosQualite
								Node infosQualite = contenuMandat.item(c);
								Node codeQualite = infosQualite.getFirstChild();
								uidMandat = contenuMandat.item(c).getFirstChild().getTextContent(); //on récupère l'id de chaque mandat
								
								// sélection des mandats en tant que président
								if (codeQualite.getTextContent().equals("Président")) {
									mdd="<md code='";
									for (int c2 = 0; c2 < nbContenuMandat; c2++) { // reparcours des noeuds de mandat
										boolean organeRefOk = false;
										// System.out.println(contenuMandat.item(c2).getNodeName()); // retourne le nom
										// des noeuds -> debug
										
										if (contenuMandat.item(c2).getNodeName().equals("dateDebut")) {
											debut = contenuMandat.item(c2).getTextContent();
										}
										
										if (contenuMandat.item(c2).getNodeName().equals("organes")) {
											organeRefOk = true;
											organeRef = contenuMandat.item(c2).getFirstChild().getTextContent();
											mdd+=organeRef+"' début='" + debut + " legislature=" + legislature;
											// on ajoute la date de fin si elle est présente
											if(fin != "") {
												mdd+=" fin=" + fin;
											}
											// on ajoute la date de publication si elle est présente
											if(pub != "") {
												mdd+=" pub=" + pub;
											}
											mdd+=">";
										}
										if (contenuMandat.item(c2).getNodeName().equals("legislature")) {
											legislature = contenuMandat.item(c2).getTextContent();
										}
										if (contenuMandat.item(c2).getNodeName().equals("dateFin")) {
											fin= contenuMandat.item(c2).getTextContent();
										}
										if (contenuMandat.item(c2).getNodeName().equals("datePublication")) {
											pub= contenuMandat.item(c2).getTextContent();
										}
										if(organeRefOk ) {
											mdPresident.add(mdd);
										}
									}
								}
							}
						}
					}
					//System.out.println("taille du tableau : k = " + mdPresident.size());
					if(mdPresident.size()!=0){
						System.out.println("<personne nom='" + prenom.getTextContent() + " " + nom.getTextContent() + "'>");
						for(int k = 0 ; k< mdPresident.size();k++)
						{
							System.out.println(mdPresident.get(k));
							
							System.out.println("uidMandat : " + uidMandat); //test valeur uid dans acteur ! 
							
							
							
							
							
							//System.out.println("ICI"); //afficher ici le libellé correspondant !
							NodeList organe = organes.getFirstChild().getChildNodes();
							
							// ne marche pas...
							// parcours des fils du noeud organe, on sélectionne le libellé !
							/*
							for (int o = 0; o < organe.getLength(); o++) { // o = organe
								if (organe.item(o).getNodeName().equals("libelle")){
									libelle = organe.item(o).getTextContent();
									System.out.println(libelle);
								}
							}*/
						}
						mdPresident.clear(); //on vide le tableau
					}
				}
			}
			System.out.println("</nantais>");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}