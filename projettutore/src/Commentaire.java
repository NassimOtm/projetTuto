import java.sql.Connection;

public class Commentaire {

	private String texte;
	private Connection connect;
	
	public Commentaire(String texte, Connection connect) {
		super();
		this.texte = texte;
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

	public String getTexte() {
		return texte;
	}
	
	

	
}
