import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

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
	private HashSet<JoueurChamp> listeJoueur;


	public Match(Connection connect, Document doc) {
		super();
		this.connect = connect;
		this.doc=doc;
		this.listeJoueur=new HashSet<JoueurChamp>();
		this.trouverDonnees();
		try{
			if(!(this.matchExiste())){
				this.integrerDansBD();
				this.obtenirId();
				System.out.println("insertion des coms");
				this.insererCommentaires();
				this.equipeJouent();
				this.statistiquesJoueurs();

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
				if(e.getValue().toString().equals("")){
					this.nbSpectateur=0;
				}
				else{
					this.nbSpectateur=Integer.parseInt(e.getValue().toString());
				}
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


	public void insererCommentaires(){
		String minute="";
		String texte="";

		List<Element> faits=this.doc.getRootElement().getChild("compteRendu").getChildren();
		List<Element> faitsJeu=new ArrayList<>();
		List<Element> comm=new ArrayList<>();
		List<Element> joueurs=new ArrayList<>();
		List<Element> arbitre=new ArrayList<>();

		for(Element e:faits){
			Commentaire commentaire=new Commentaire(e.getValue().substring(10),e.getChildText("minute"),this.connect);
			try{
				commentaire.ajouterABD(this.id, e.getName().equals("faitArbitral"));
			}catch(SQLException err){
				System.out.println("erreur dans l'insertion du commentaire");
			}
			if(e.getName().equals("faitArbitral")) {

				List<Element> listeCom=e.getChildren("commentaire");
				for(Element element : listeCom){
					comm.add(element);
				}
			}
			else{
				faitsJeu.add(e.getChild("commentaire"));
			}
		}
		for(Element e:comm) {
			if(e.getValue().contains("Carton jaune")) {
				System.out.println("Cartonnnnn");
			}

			List<Element> listeJoueur=e.getChildren("joueur");
			for(Element element:listeJoueur){
				joueurs.add(element);
			}
			joueurs.add(e.getChild("gardien"));
			if(e.getChild("arbitre")!=null){
				arbitre.add(e.getChild("arbitre"));
			}

		}

		for(Element e:faitsJeu){
			if(e.getChild("arbitre")!=null){
				arbitre.add(e.getChild("arbitre"));
			}
		}

		try{
			insererJoueurs(joueurs);
			insererArbitre(arbitre);
		}catch(SQLException err){
			System.out.println("erreur lors de l'insertion de l'arbitre et des joueurs dans le match");
		}
	}

	public void insererJoueurs(List<Element> joueurs) throws SQLException{

		for(Element e:joueurs) {
			String nom="";
			String prenom="";
			if(e!=null) {
				JoueurChamp player;
				if(e.getValue().indexOf(" ")==-1){
					nom=e.getValue();
				}
				else{
					prenom=echapperString(e.getValue().substring(0, e.getValue().indexOf(" ")));
					nom=echapperString(e.getValue().substring(e.getValue().indexOf(" ")+1));
					
				}

				if(e.getName().equals("gardien")){
					player=new JoueurChamp(nom,prenom,"gardien",this.connect);
				}
				else if(e.getAttributeValue("poste")!=null){
					player=new JoueurChamp(nom,prenom,echapperString(e.getAttributeValue("poste")),this.connect);
				}
				else{
					player=new JoueurChamp(nom,prenom,null,this.connect);
				}
				this.listeJoueur.add(player);


			}
		}
		for(JoueurChamp j:listeJoueur){
			Statement state = this.connect.createStatement();
			String requete="INSERT INTO projettutore.aJoueMatch Values (0,0,0,0,"+j.getId()+","+this.id+")";
			state.executeUpdate(requete);
		}

	}

	public void insererArbitre(List<Element> arbitre) throws SQLException{
		int indice=0;
		boolean arbitreTrouve=false;
		System.out.println("\nL'arbitre du match est ");
		for(Element e:arbitre) {
			
			if(e!=null && !arbitreTrouve) {
				System.out.println(e.getValue());
				arbitreTrouve=true;
				String prenom=e.getValue().substring(0, e.getValue().indexOf(" "));
				String nom=e.getValue().substring(e.getValue().indexOf(" ")+1);
				this.arbitre=new Arbitre(nom,prenom,this.connect);
				Statement state = this.connect.createStatement();
				state.executeUpdate("UPDATE projettutore.match set idarbitre="+this.arbitre.getId()+" where idmatch="+this.id);
			}
		}
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
	
	public void statistiquesJoueurs() throws SQLException{
		
		List<Element> faits=this.doc.getRootElement().getChild("compteRendu").getChildren();
		List<Element> comm=new ArrayList<>();
		for(Element e:faits){
			if(e.getName().equals("faitArbitral")) {
				List<Element> listeCom=e.getChildren("commentaire");
				for(Element element : listeCom){
					comm.add(element);
				}
			}
		
		}
		for(Element e:comm){
			if(e.getValue().contains("Carton jaune") || e.getValue().contains("Carton rouge")){
				Statement state = this.connect.createStatement();
				String joueur=e.getChild("joueur").getValue();
				System.out.println("le joueur "+joueur);
				String nom="";
				String prenom="";
				String requete="";
				if(joueur.indexOf(" ")==-1){
					nom=e.getChild("joueur").getValue();
				}
				else{
					prenom=e.getChild("joueur").getValue().substring(0, e.getValue().indexOf(" "));
					nom=e.getChild("joueur").getValue().substring(e.getValue().indexOf(" ")+1);
				}
				JoueurChamp player=new JoueurChamp(nom,prenom,null,this.connect);
				if(e.getValue().contains("Carton jaune")){
					requete="UPDATE projettutore.ajouematch set cartonjaunepris=cartonjaunepris+1 where idjoueur="+player.getId()+"and idmatch="+this.id;
					state.executeQuery(requete);				
				}
				else{
					requete="UPDATE projettutore.ajouematch set cartonrougepris=cartonrougepris+1 where idjoueur="+player.getId()+"and idmatch="+this.id;
					state.executeQuery(requete);
				}
			}
		//	else if(e.getValue().contains("))
	
		}
		
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
	
	public static String echapperString(String chaine){
		
		while(chaine.indexOf("'")!=-1){
			chaine=chaine.substring(0, chaine.indexOf("'"))+"_"+chaine.substring(chaine.indexOf("'")+1);
		}
		return chaine;
	}
}
