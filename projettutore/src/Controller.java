package interfaceJavaFx;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    	
    	if(list !=null) {
    		for(File file : list) {
    			Desktop desktop = Desktop.getDesktop();
    			try {
    				desktop.open(file);
    			}catch (IOException ex) {
    				Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
    			}
    			
    		}
    	}
    	
    	
    }
    
   
    @FXML
    void RemplirEssai(ActionEvent event) {

    }
    
    
    
    @FXML
    void TraiterBD(ActionEvent event) {

    }

    @FXML
    void TraiterXML(ActionEvent event) {

    }


}