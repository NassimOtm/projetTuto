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
	private Arbitre arbitre;
	private String stade;
	private int nbSpectateur;
	private Competition compet;
	private Commentaire resume;
	private Connection connect;
	private Document doc;
	private int id;
	private String sport;
	private int point=1;
	private int pointBonnifie=1;
	private int pointFauteTechnique=1;
	
	
	public Match(Connection connect, Document doc) {
		super();
		this.connect = connect;
		this.doc=doc;
		this.trouverDonnees();
		try{
			if(!(this.matchExiste())){
			this.integrerDansBD();
			this.obtenirId();
			this.insererCommentaires();
			//this.equipeJouent();
			//this.nbCartonsEquipes();
			}
		}catch(SQLException e){
			System.out.println("Erreur lors de l'insertion dans la base de donnees");
		}
		
	}
	
	public boolean matchExiste() throws SQLException{
		Statement state = this.connect.createStatement();
		ResultSet result= state.executeQuery("select * from projettutore.match WHERE resume='"+this.resume.getTexte()+"'");
		ResultSetMetaData resultMeta = result.getMetaData();
		return result.next();
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
	
	public void trouverArbitre(){
		String nom="";
		String prenom="";
		String arbitre="";
		int indice=0;
		while(indice<arbitre.length() && arbitre.charAt(indice)!=' '){
			prenom+=arbitre.charAt(indice);
			indice++;
		}
		indice++;
		while(indice<arbitre.length()){
			nom+=arbitre.charAt(indice);
			indice++;
		}
		
		this.arbitre=new Arbitre(nom,prenom,this.connect);
		
	}
	
	public void nbCartonsEquipes() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requeteDom,requeteExt;
		List<Element> nbJaunes=doc.getRootElement().getChild("cartonsJaunes").getChildren();
		List<Element> nbRouges=doc.getRootElement().getChild("cartonsRouges").getChildren();
		for(Element e:nbJaunes){
			if(e.getName().equals("domicile")){
				requeteDom="insert into projettutore.joueDomicile values ("+this.equipeDomicile.getId()+","+this.id+","+Integer.parseInt(e.getValue())+")";
				
				state.executeUpdate(requeteDom);
			}
			
			if(e.getName().equals("exterieur")){
				requeteExt="insert into projettutore.joueExterieur values ("+this.equipeExterieure.getId()+","+this.id+","+Integer.parseInt(e.getValue())+")";
				
				state.executeUpdate(requeteExt);
			}
		}
		
		for(Element e:nbRouges){
			if(e.getName().equals("domicile")){
				requeteDom="UPDATE projettutore.joueDomicile set cartonRougeDom="+Integer.parseInt(e.getValue())+" where idmatch="+this.id+" and idequipe="+this.equipeDomicile.getId();
				
				state.executeUpdate(requeteDom);
			}
			
			if(e.getName().equals("exterieur")){
				requeteExt="UPDATE projettutore.joueExterieur set cartonrougeext="+Integer.parseInt(e.getValue())+" where idmatch="+this.id+" and idequipe="+this.equipeExterieure.getId();
				
				state.executeUpdate(requeteExt);
			}
		}
		
	}
	
	public void insererJoueur(Element e) throws SQLException{
		String joueur=e.getChildText("joueur");
		String nom="";
		String prenom="";
		
		Statement state = this.connect.createStatement();
		int indice=0;
		while(indice<joueur.length() && joueur.charAt(indice)!=' '){
			prenom+=joueur.charAt(indice);
			indice++;
		}
		indice++;
		while(indice<joueur.length()){
			nom+=joueur.charAt(indice);
			indice++;
		}
		String poste=e.getChildTextTrim("joueur").getAttribute("poste").toString();
		System.out.println(poste);
		JoueurChamp player=new JoueurChamp(nom,prenom,null,this.connect); 
		int idjoueur=player.getId();
		String requete="Insert into projettutore.aJoueMatch Values (0,0,0,0,"+idjoueur+","+this.id+")";
		System.out.println(requete);
		state.executeUpdate(requete);
	}
	
	public void insererCommentaires(){
		String minute="";
		String texte="";
		List<Element> faits=doc.getRootElement().getChild("compteRendu").getChildren();
		
		for(Element e: faits){				
			minute=e.getChildText("minute");
			texte=e.getValue().substring(10);
				Commentaire com=new Commentaire(texte,minute,this.connect);
				try{
				//	if(e.getChildText("joueur")!=null){
					//	this.insererJoueur(e);
					//}
					com.ajouterABD(this.id,e.getName().equals("faitArbitral"));
					
				}catch (SQLException err){
					System.out.println("Erreur lors de l'insertion des commentaires");
				}	
				try{
				if(com.parleBut()){
					this.insererJoueur(e.getChild("commentaire"));
				}
				}catch(SQLException excep){
					System.out.println("erreur lors de l'insertion ds buteurs");
				}
		}
	}
	
	public void insererButeur(Element e) throws SQLException{
		
		String nom="";
		String prenom="";
		String buteur=e.getChildText("joueur");
		System.out.println(buteur);
		Statement state = this.connect.createStatement();
		int indice=0;
		while(indice<buteur.length() && buteur.charAt(indice)!=' '){
			prenom+=buteur.charAt(indice);
			indice++;
		}
		indice++;
		while(indice<buteur.length()){
			nom+=buteur.charAt(indice);
			indice++;
		}
		
		JoueurChamp player=new JoueurChamp(nom,prenom,null,this.connect); 
		int idjoueur=player.getId();
		/*String requete="Insert into projettutore.aJoueMatch Values ("+this.id+","+idjoueur+",null,null,null,null,1)";
		System.out.println(requete);
		state.executeUpdate(requete);*/
	}
	
	public void equipeJouent() throws SQLException{
		
		List<Element> scoreMi=doc.getRootElement().getChild("scoreMitemps").getChildren();
		List<Element> scoreFin=doc.getRootElement().getChild("scoreFinal").getChildren();
		Statement state = this.connect.createStatement();
		
		int miTempsDomicile=0;
		int miTempsExterieur=0;
		int finMatchDomicile=0;
		int finMatchExterieur=0;
		
		for(Element e:scoreMi){
			if(e.getName().equals("domicile")){
				miTempsDomicile=Integer.parseInt(e.getValue());
			}
			else{
				miTempsExterieur=Integer.parseInt(e.getValue());
			}
		}
		
		for(Element e:scoreFin){
			if(e.getName().equals("domicile")){
				finMatchDomicile=Integer.parseInt(e.getValue());
			}
			else{
				finMatchExterieur=Integer.parseInt(e.getValue());
			}
		}
		String requeteDom="insert into projettutore.joueDomicile values ("+this.equipeDomicile.getId()+","+this.id+","+miTempsDomicile+","+finMatchDomicile+")";
		System.out.println(requeteDom);
		String requeteExt="insert into projettutore.joueExterieur values ("+this.equipeExterieure.getId()+","+this.id+","+miTempsExterieur+","+finMatchExterieur+")";
		System.out.println(requeteExt);
		state.executeUpdate(requeteDom);
		state.executeUpdate(requeteExt);
	}
	
	public void trouverDonnees(){
		this.trouverEquipes();
		this.trouverStade();
		this.trouverCompetition();
		this.trouverNbSpectateur();
		this.trouverResume();

	}
	
	public void integrerDansBD() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requete="insert into projettutore.match values (DEFAULT,'"+this.stade+"','"+this.resume.getTexte()+"',"+this.nbSpectateur+",NULL,"+this.compet.getId()+",NULL,"+this.equipeDomicile.getId()+","+this.equipeExterieure.getId()+")";
		state.executeUpdate(requete);
		
	}
	
	public void obtenirId() throws SQLException{		
		Statement state = this.connect.createStatement();
		String requete="select idmatch from projettutore.match where resume='"+this.resume.getTexte()+"'"; 
		ResultSet result= state.executeQuery(requete);
		ResultSetMetaData resultMeta = result.getMetaData();
		if(result.next())
			this.id=Integer.parseInt(result.getObject(1).toString());		
	}
}
