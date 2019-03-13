import java.sql.Connection;
import java.util.ArrayList;

public class Competition {

	private String nom;
	private ArrayList<Match> listeMatch;
	private Connection connect;
	
	public Competition(String nom, ArrayList<Match> listeMatch, Connection connect) {
		super();
		this.nom = nom;
		this.listeMatch = listeMatch;
		this.connect = connect;
	}
	
	


}
