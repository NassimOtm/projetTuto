import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

public class Match {

	private Equipe equipeDomicile;
	private Equipe equipeExterieure;
	private String stade;
	private int nbSpectateur;
	private String resume;
	private String scoreMT;
	private String scoreFT;
	private Connection connect;
	private Document doc;
	
	public Match(Connection connect, Document doc) {
		super();
		this.connect = connect;
		this.doc=doc;
		this.trouverDonnees();
	}
	
	public void trouverEquipes(){		
		List<Element> listerEquipes = doc.getRootElement().getChild("equipes").getChildren();
		for(Element e : listerEquipes){
			if(e.getName().equals("domicile")){
				this.equipeDomicile=new Equipe(e.getValue().toString(),connect);
			}				
			if(e.getName().equals("exterieur")){
				this.equipeExterieure=new Equipe(e.getValue().toString(),connect);
			}
		}		
	}
	
	public void trouverStade(){
		List<Element> stade=doc.getRootElement().getChildren();
		for(Element e : stade){
			if(e.getName().equals("stade")){
				this.stade=e.getValue().toString();
			}
		}
	}
	
	public void trouverDonnees(){
		
		this.trouverEquipes();
		this.trouverStade();
		
	}
	
	
}
