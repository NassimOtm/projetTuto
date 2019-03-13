import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

public class Match {

	private Equipe equipeDomicile;
	private Equipe equipeExterieure;
	private String stade;
	private int nbSpectateur;
	private Competition compet;
	private Commentaire resume;
	private String scoreMT;
	private String scoreFT;
	private Connection connect;
	private Document doc;
	
	public Match(Connection connect, Document doc) {
		super();
		this.connect = connect;
		this.doc=doc;
		this.trouverDonnees();
		try{
			this.integrerDansBD();
		}catch(SQLException e){
			System.out.println("Erreur lors de l'insertion dans la base de donnees");
		}
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
	
	public void trouverScoreMT(){
		String score="";
		List<Element> listerEquipes = doc.getRootElement().getChild("scoreMitemps").getChildren();
		for(Element e : listerEquipes){
			if(e.getName().equals("domicile")){
				score=score+e.getValue().toString()+"-";
			}				
			if(e.getName().equals("exterieur")){
				score=score+e.getValue().toString();
			}
		}
		this.scoreMT=score;
	}
	
	public void trouverScoreFT(){
		String score="";
		List<Element> listerEquipes = doc.getRootElement().getChild("scoreFinal").getChildren();
		for(Element e : listerEquipes){
			if(e.getName().equals("domicile")){
				score=score+e.getValue().toString()+"-";
			}				
			if(e.getName().equals("exterieur")){
				score=score+e.getValue().toString();
			}
		}
		this.scoreFT=score;
	}
	
	public void trouverCompetition(){
		List<Element> compet=doc.getRootElement().getChildren();
		for(Element e : compet){
			if(e.getName().equals("competition")){
				System.out.println(e.getValue().toString());
				this.compet=new Competition(e.getValue().toString(),this.connect);
			}
		}
	}
	
	public void trouverNbSpectateur(){
		List<Element> spectateur=doc.getRootElement().getChildren();
		for(Element e : spectateur){
			if(e.getName().equals("spectateurs")){
				this.nbSpectateur=Integer.parseInt(e.getValue().toString());
			}
		}
	}
	
	public void trouverResume(){
		List<Element> resume=doc.getRootElement().getChildren();
		for(Element e : resume){
			if(e.getName().equals("resume")){
				this.resume=new Commentaire(e.getValue().toString(),connect);
			}
		}
	}
	
	public void trouverDonnees(){
		this.trouverEquipes();
		this.trouverStade();
		this.trouverScoreMT();
		this.trouverScoreFT();
		this.trouverCompetition();
		this.trouverNbSpectateur();
		this.trouverResume();
		
		System.out.println(this.stade);
		System.out.println(this.scoreMT);
		System.out.println(this.scoreFT);
	}
	
	public void integrerDansBD() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requete="insert into projettutore.match values (DEFAULT,NULL,'"+this.scoreMT+"','"+this.scoreFT+"',"+this.nbSpectateur+",'"+this.stade+"','"+this.resume.getTexte()+"', NULL,"+this.compet.getId()+")";
		System.out.println(requete);
		state.executeUpdate(requete);
		
	}
}
