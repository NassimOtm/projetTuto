import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Equipe {

	private int id;
	private String nom;
	private String pays;
	private ArrayList<JoueurChamp> effectif;
	private Entraineur entraineur;
	private Connection connect;
	
	public Equipe(String nom,Connection connect) {
		this.nom = nom;
		this.connect = connect;
		this.entraineur=null;
		this.effectif=new ArrayList<>();
		try{
		this.trouverDonnees();
		}catch(SQLException e){
			System.out.println("Erreur lors de la creation de l'equipe");
		}
	}	
	
	public void trouverDonnees() throws SQLException{
			Statement state = this.connect.createStatement();
			ResultSet result= state.executeQuery("select idequipe,paysequipe from projettutore.equipe WHERE nomequipe='"+this.nom+"'");
			ResultSetMetaData resultMeta = result.getMetaData();
			if(result.next()){
				System.out.println("equipe trouver");
				System.out.println("l'id"+Integer.parseInt(result.getObject(1).toString()));
				this.id=Integer.parseInt(result.getObject(1).toString());
				if(result.getObject(2)!=null)
				this.pays=result.getObject(2).toString();
			}
			else{
				System.out.println("insertion equipe");
				System.out.println("INSERT INTO projettutore.equipe VALUES ('"+this.nom+"',NULL,DEFAULT)");
				state.executeUpdate("INSERT INTO projettutore.equipe VALUES ('"+this.nom+"',NULL,DEFAULT)");
				this.trouverDonnees();
			}
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getPays() {
		return pays;
	}

	public ArrayList<JoueurChamp> getEffectif() {
		return effectif;
	}

	public Entraineur getEntraineur() {
		return entraineur;
	}

	public Connection getConnect() {
		return connect;
	}
}
