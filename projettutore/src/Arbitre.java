import java.sql.Connection;

public class Arbitre {

	private String nom;
	private String prenom;
	private Connection connect;
	
	public Arbitre(String nom, String prenom, Connection connect) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.connect = connect;
	}
	
	
	
}
