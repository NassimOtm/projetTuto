package interfaceJavaFx;



import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	@FXML
	private Button essai;

	
    @FXML
    private Button BoutonXml;

    @FXML
    private Button BoutonBd;

    @FXML
    private Button BoutonChoix;

    @FXML
    private TextArea ListeFichiers;

    @FXML
    private Label titrePage;

    @FXML
    void ChoisirFichier(ActionEvent event) {
    	FileChooser fileChoose = new FileChooser();
    	fileChoose.setTitle("Choose files");
    	Stage stage = (Stage)BoutonChoix.getScene().getWindow();
    	
    	List<File> list = fileChoose.showOpenMultipleDialog(stage);
    	ArrayList<File> selection = new ArrayList<>();
    	
    	
    	
    	if(list !=null) {
    		for(File file : list) {
    			Desktop desktop = Desktop.getDesktop();
    			try {
    				desktop.open(file);
    			}catch (IOException ex) {
    				Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
    			}
    			selection.add(file);
    			
    		}
    	}
    	for (int i=0; i<selection.size(); i++) {
    	//this.ListeFichiers.appendText(selection[i].toString);
    	
    	}
    	
    }
    
   
  /*  @FXML
    void RemplirEssai(ActionEvent event) {
    	String url,user,mdp;
		Parametres param=null;
		Connection connect=null;
		boolean erreurConnect;
		boolean erreurRequete;
		
		url="jdbc:postgresql://database-etudiants.iut.univ-paris8.fr/";
		do{
			try{
				param=new Parametres(url);
				connect=DriverManager.getConnection(param.getUrl(),param.getUser(),param.getPassword());
				erreurConnect=false;
			}catch(SQLException e){
				erreurConnect=true;
				System.out.println("Connexion impossible!!!!\n Reesayez\n");
			}
		}while(erreurConnect);	
		System.out.println("Connexion reussi");
		
		File fichier = new File("src/ressources/parisSG_Caen_versionASADERA_joueur.xml");
		//File fichier=new File ("src/ressources/italieFrance_tournoiFeminin6nations.xml");
		Document doc=lireXML(fichier);
		Match match=new Match(connect,doc);
    }
    
    
    */
    @FXML
    void TraiterBD(ActionEvent event) {

    }

    @FXML
    void TraiterXML(ActionEvent event) {

    }


}