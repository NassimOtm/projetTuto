import java.sql.Connection;

public class Commentaire {

	private String texte;
	private Connection connect;
	
	public Commentaire(String texte, Connection connect) {
		super();
		this.texte = texte;
		this.connect = connect;
	}

	
}
