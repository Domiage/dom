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
	public static void main(final String[] args) throws SAXException, IOException {
		Node prenom;
		Node nom;
		String mdd ="";
		String organeRef="";
		String debut="";
		String legislature ="";
		String fin ="";
		String pub ="";
		List<String> mdPresident = new ArrayList<String>();
		Map <String,List<String>> mesMandats = new HashMap<String,List<String>>();
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\">";
		System.out.println(xmlStr);
		String doctype = "<!DOCTYPE nantais SYSTEM \"ex.dtd\">";
		System.out.println(doctype);
		String baliseNantais = "<nantais>";
		System.out.println(baliseNantais);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// on souhaite ignorer les �l�ments textes
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		try {
			factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			ErrorHandler errHandler = new SimpleErrorHandler();
			builder.setErrorHandler(errHandler);

			Document document = builder.parse(new File("historique.xml"));

			Node racine = document.getDocumentElement();
			Node organes = racine.getFirstChild();
			Node acteurs = organes.getNextSibling();
			NodeList acteur = acteurs.getChildNodes();

			// affichage de la racine du document xml export
			// System.out.println("<" + racine.getNodeName() + ">");

			int nbActeur = acteur.getLength();
			for (int i = 0; i < nbActeur; i++) {
				// List<String> mdPresident = new ArrayList<String>(); //contiendra les
				// diff�rents mandats
				Node etatCivil = acteur.item(i).getFirstChild().getNextSibling();
				Node infoNais = etatCivil.getFirstChild().getNextSibling();
				Node villeNais = infoNais.getFirstChild().getNextSibling();
				prenom = etatCivil.getFirstChild().getFirstChild().getNextSibling();
				nom = prenom.getNextSibling();
				// s�lection de la ville de Nantes
				if (villeNais.getTextContent().equals("Nantes")) {
					NodeList mandat = acteur.item(i).getLastChild().getChildNodes(); // mandats de l'acteur
					for (int m = 0; m < mandat.getLength(); m++) { // m = mandat
						// contient l'ensemble du contenu du noeud mandat
						NodeList contenuMandat = mandat.item(m).getChildNodes(); // contenu du mandat
						int nbContenuMandat = contenuMandat.getLength();
						// parcourt des noeuds fils de mandat
						for (int c = 0; c < nbContenuMandat; c++) { // c = contenu
							// on s�lectionne le noeud "infosQualite"
							if (contenuMandat.item(c).getNodeName().equals("infosQualite")
									&& contenuMandat.item(c).getNodeType() == Node.ELEMENT_NODE) {
								// on se positionne dans le noeud infosQualite
								Node infosQualite = contenuMandat.item(c);
								Node codeQualite = infosQualite.getFirstChild();
								if (codeQualite.getTextContent().equals("Pr�sident")) {
									for (int c2 = 0; c2 < nbContenuMandat; c2++) { // reparcours des noeuds de mandat
										// System.out.println(contenuMandat.item(c2).getNodeName()); // retourne le nom
										// des noeuds -> debug
										if (contenuMandat.item(c2).getNodeName().equals("organes")) {
											organeRef = contenuMandat.item(c2).getFirstChild().getTextContent();
										}
										if (contenuMandat.item(c2).getNodeName().equals("dateDebut")) {
											debut = contenuMandat.item(c2).getTextContent();
										}
										if (contenuMandat.item(c2).getNodeName().equals("legislature")) {
											legislature = contenuMandat.item(c2).getTextContent();
										}
										if (contenuMandat.item(c2).getNodeName().equals("datePublication")) {
											pub= contenuMandat.item(c2).getTextContent();
										}
										mdd="<md code='"+organeRef+"'>"; // � poursuivre !
										mdPresident.add(mdd);
									}
								}
							}

						}
					}
					if(mdPresident.size()!=0)
					{
						System.out.println("<personne nom='" + prenom.getTextContent() + " " + nom.getTextContent() + "'>");
						for(int k = 2 ; k< mdPresident.size();k++)
						{
							System.out.println(mdPresident.get(k));
							mdPresident.clear();

						}

					}

					// affichage dans le for des acteurs !!!
					/*
					 * if (nantais && president) { Node identite = etatCivil.getFirstChild(); Node
					 * prenom = identite.getFirstChild().getNextSibling(); Node nom =
					 * prenom.getNextSibling(); System.out.println("<personne nom='" +
					 * prenom.getTextContent() + " " + nom.getTextContent() + "'>");
					 * 
					 * // parcours des mandats de la personne for (int md=0; md<mdPresident.size();
					 * md++) { System.out.println("<md..."); } }
					 */

					// on r�utilise le i de la boucle acteur
					// NodeList mandatss = acteur.item(i).getLastChild().getChildNodes();
					// System.out.println("mandats : " + mandatss);

					/*
					 * if(president == true) { Node identPersonneSelectionnee =
					 * etatCivilPersonneSelectionnee.getFirstChild(); Node prenom =
					 * identPersonneSelectionnee.getFirstChild().getNextSibling(); Node nom =
					 * prenom.getNextSibling();
					 * 
					 * System.out.println("<personne nom='" + prenom.getTextContent() + " " +
					 * nom.getTextContent() + "'>"); // affichage du contenu de l'arraylist }
					 */

				}
				/*
				 * if (nantais && mdPresident.size()>0) { Node identite =
				 * etatCivil.getFirstChild(); Node prenom =
				 * identite.getFirstChild().getNextSibling(); Node nom =
				 * prenom.getNextSibling(); System.out.println("<personne nom='" +
				 * prenom.getTextContent() + " " + nom.getTextContent() + "'>");
				 * 
				 * // parcours des mandats de la personne for (int md=0; md<mdPresident.size();
				 * md++) { System.out.println("<md..."); } }
				 */
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