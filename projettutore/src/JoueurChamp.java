import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JoueurChamp {

	private String nom;
	private String prenom;
	private String poste;
	private int id;
	private Connection connect;
	
	public JoueurChamp(String nom, String prenom, String poste, Connection connect) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.poste = poste;
		this.connect = connect;
		try{
			this.obtenirId();
		}catch(SQLException e){
			System.out.println("Erreur lors de l'obtention de l'id du joueur "+this.nom+" "+this.prenom);
		}
	}
	
	public void obtenirId() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requete="";
		if(this.nom.length()!=0){
			requete="select idjoueur from projettutore.joueur where (nom='"+this.nom+"' and prenom='"+this.prenom+"') OR (nom='"+this.prenom+"' and prenom='"+this.nom+"')";
		}
		else{
			requete="select idjoueur from projettutore.joueur where nom='"+this.prenom+"' or prenom='"+this.prenom+"'";
			
		}
		ResultSet result= state.executeQuery(requete);
		ResultSetMetaData resultMeta = result.getMetaData();
		if(result.next()){
			this.id=Integer.parseInt(result.getObject(1).toString());	
		}	
		else{
			requete="INSERT INTO projettutore.joueur values (DEFAULT,'"+this.nom+"','"+this.prenom+"',NULL,NULL)";
			state.executeUpdate(requete);
			this.obtenirId();
		}
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getPoste() {
		return poste;
	}

	public int getId() {
		return id;
	}

	public Connection getConnect() {
		return connect;
	}
	
}
