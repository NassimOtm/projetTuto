import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Commentaire {

	private String texte;
	private int minute;
	private Connection connect;
	
	public Commentaire(String texte,int minute, Connection connect) {
		super();
		this.texte = texte;
		this.minute=minute;
		this.echapperSymboles();
		this.connect = connect;
	}
	
	public void echapperSymboles(){
		String caractere="'";
		char carac=caractere.charAt(0);
		int longueurTexte=this.texte.length();
		
		for(int i=0;i<this.texte.length();i++){
			if(this.texte.charAt(i)==carac){
				this.texte=this.texte.substring(0, i)+'_'+this.texte.substring(i+1,longueurTexte);
			}
		}
	}
	
	public boolean cartonJaune(){		
		String carton="Carton jaune";
		return this.texte.contains(carton);		
	}
	
	public void ajouterABD(int idMatch) throws SQLException{
		Statement state = this.connect.createStatement();
		String requete="insert into projettutore.commentaire values (DEFAULT,"+this.texte+","+this.minute+","+idMatch+")";
		state.executeUpdate(requete);
	}

	public String getTexte() {
		return texte;
	}	
}
