import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Arbitre {

	private String nom;
	private String prenom;
	private int id;
	private Connection connect;
	
	public Arbitre(String nom, String prenom, Connection connect) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.connect = connect;
		try{
			this.obtenirId();
		}catch(SQLException err){
			System.out.println("erreur lors de l'obtention de l'id de l'arbitre");
		}
	}
	
	public void obtenirId() throws SQLException{
		
		Statement state = this.connect.createStatement();
		String requete="select idjoueur from projettutore.arbitre where (nom='"+this.nom+"' and prenom='"+this.prenom+"') OR (nom='"+this.prenom+"' and prenom='"+this.nom+"')";	
		ResultSet result= state.executeQuery(requete);
		ResultSetMetaData resultMeta = result.getMetaData();
		if(result.next()){
			this.id=Integer.parseInt(result.getObject(1).toString());		
		}
		else{
			state.executeUpdate("INSERT INTO projettutore.arbitre VALUES (DEFAULT,'"+this.nom+"','"+this.prenom+")");
			this.obtenirId();
		}
	}
}
