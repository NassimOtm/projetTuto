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
		System.out.println("creation de "+nom+" "+prenom);
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
			requete="select idjoueur from projettutore.joueur where nom='"+this.nom+"' or prenom='"+this.nom+"'";
			
		}
		ResultSet result= state.executeQuery(requete);
		ResultSetMetaData resultMeta = result.getMetaData();
		if(result.next()){
			this.id=Integer.parseInt(result.getObject(1).toString());	
		}	
		else{
			requete="INSERT INTO projettutore.joueur values (DEFAULT,'"+this.nom+"','"+this.prenom+"','"+this.poste+"',NULL)";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JoueurChamp other = (JoueurChamp) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
