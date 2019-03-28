import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class Main {

	public static void main(String[] args) {

		String url,user,mdp;
		Parametres param=null;
		Connection connect=null;
		boolean erreurConnect;
		boolean erreurRequete;
		
		url="jdbc:postgresql://database-etudiants.iut.univ-paris8.fr/";
		do{
			try{
				param=new Parametres(url);
				connect=DriverManager.getConnection(param.getUrl(),param.getUser(),param.getPassword());
				erreurConnect=false;
			}catch(SQLException e){
				erreurConnect=true;
				System.out.println("Connexion impossible!!!!\n Reesayez\n");
			}
		}while(erreurConnect);	
		System.out.println("Connexion reussi");
		
		//File fichier = new File("src/ressources/parisSG_Caen_versionASADERA_joueur.xml");
		File fichier=new File ("src/ressources/italieFrance_tournoiFeminin6nations.xml");
		Document doc=lireXML(fichier);
		Match match=new Match(connect,doc);
	
	}
	
	
	public static Document lireXML(File fichier) {
		
		SAXBuilder sax = new SAXBuilder();
		Document document = null;
		
		try {
			
			document = sax.build(fichier);
			
		} catch(JDOMException e) {
			
			e.printStackTrace();
			
		} catch(IOException e) {
			
			e.printStackTrace();
		}
		return document;
	}
	
	public static void afficher(Document document) {
		try {
			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, System.out);
			
		} catch(java.io.IOException e) {}
	}

}