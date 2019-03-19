import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
	private int id;
	
	public Match(Connection connect, Document doc) {
		super();
		this.connect = connect;
		this.doc=doc;
		this.trouverDonnees();
		try{
			this.integrerDansBD();
			this.obtenirId();
			this.insererCommentaires();
			this.nbCartonsJaunes();
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
				this.resume=new Commentaire(e.getValue().toString(),"-1",connect);
			}
		}
	}
	
	public void nbCartonsJaunes() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requeteDom,requeteExt;
		List<Element> nbJaunes=doc.getRootElement().getChild("cartonsJaunes").getChildren();
		List<Element> nbRouges=doc.getRootElement().getChild("cartonsRouges").getChildren();
		for(Element e:nbJaunes){
			if(e.getName().equals("domicile")){
				requeteDom="insert into projettutore.joueDomicile values ("+this.equipeDomicile.getId()+","+this.id+","+Integer.parseInt(e.getValue())+")";
				System.out.println("REQUETE DOMICILE"+requeteDom);
				state.executeUpdate(requeteDom);
			}
			
			if(e.getName().equals("exterieur")){
				requeteExt="insert into projettutore.joueExterieur values ("+this.equipeExterieure.getId()+","+this.id+","+Integer.parseInt(e.getValue())+")";
				System.out.println("REQUETE EXTERIEUR"+requeteExt);
				state.executeUpdate(requeteExt);
			}
		}
		
		for(Element e:nbRouges){
			if(e.getName().equals("domicile")){
				requeteDom="UPDATE projettutore.joueDomicile set cartonRougeDom="+Integer.parseInt(e.getValue())+" where idmatch="+this.id+" and idequipe="+this.equipeDomicile.getId();
				System.out.println("REQUETE DOMICILE"+requeteDom);
				state.executeUpdate(requeteDom);
			}
			
			if(e.getName().equals("exterieur")){
				requeteExt="UPDATE projettutore.joueExterieur set cartonrougeext="+Integer.parseInt(e.getValue())+" where idmatch="+this.id+" and idequipe="+this.equipeExterieure.getId();
				System.out.println("REQUETE EXTERIEUR"+requeteExt);
				state.executeUpdate(requeteExt);
			}
		}
		
	}
	
	public void insererCommentaires(){
		String minute="";
		String texte="";
		List<Element> faitJeu=doc.getRootElement().getChild("compteRendu").getChildren();
		
		for(Element e: faitJeu){
			minute=e.getChildText("minute");
			texte=e.getChildText("commentaire");
				Commentaire com=new Commentaire(texte,minute,this.connect);
				try{	
					com.ajouterABD(this.id);
				}catch (SQLException err){
					System.out.println("Erreur lors de l'insertion des commentaires");
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
		
	}
	
	public void integrerDansBD() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requete="insert into projettutore.match values (DEFAULT,NULL,'"+this.scoreMT+"','"+this.scoreFT+"',"+this.nbSpectateur+",'"+this.stade+"','"+this.resume.getTexte()+"', NULL,"+this.compet.getId()+")";
		state.executeUpdate(requete);
		
	}
	
	public void obtenirId() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requete="select idmatch from projettutore.match where resume='"+this.resume.getTexte()+"'"; 
		ResultSet result= state.executeQuery(requete);
		ResultSetMetaData resultMeta = result.getMetaData();
		System.out.println(result.next());
			this.id=Integer.parseInt(result.getObject(1).toString());		
	}
}
