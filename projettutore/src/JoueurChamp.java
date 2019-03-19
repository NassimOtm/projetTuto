import java.sql.Connection;

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
	}
	
}
