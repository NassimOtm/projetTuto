import java.sql.Connection;

public class Entraineur {

	private String nom;
	private String prenom;
	private String pays;
	private Connection connect;
	
	public Entraineur(String nom, String prenom, String pays, Connection connect) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.pays = pays;
		this.connect = connect;
	}
	
	
	
}
