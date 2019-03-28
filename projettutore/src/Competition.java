import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Competition {

	private Connection connect;
	private String nom;
	private int id;
	
	public Competition(String nom,Connection connect) {
		super();
		this.nom = nom;
		this.connect = connect;
		try{
			this.trouverId();
		}catch(SQLException e){
			System.out.println("Erreur lors de la recherche de l'id de la competition");
		}
	}
	
	public int getId() {
		return id;
	}

	public void trouverId() throws SQLException{
		
		String requete;
		
		Statement state = this.connect.createStatement();
		requete="select idcompetition from projettutore.Competition WHERE nomcompetition='"+this.nom+"'";
		ResultSet result= state.executeQuery(requete);
		ResultSetMetaData resultMeta = result.getMetaData();
		if(result.next()){
			this.id=Integer.parseInt(result.getObject(1).toString());
		}
		else{
			requete="INSERT INTO projettutore.competition VALUES (DEFAULT,'"+this.nom+"')";
			state.executeUpdate(requete);
			this.trouverId();
		}
}
	
	


}
