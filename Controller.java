
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller {

	
	@FXML
    private Button BouttonQuitter;

	
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
    	JFileChooser dialogue=new JFileChooser();
    	dialogue.showOpenDialog(null);
    	File fichier = dialogue.getSelectedFile();
    	boolean aFonctionne=Main.remplir(fichier);
    	
    	if(aFonctionne) {
    		this.ListeFichiers.appendText("Le fichier séléctionné à bien été traité.");
    	}
    	else {
    		this.ListeFichiers.appendText("Le fichier n'a pas été traité, verifier votre fichier.");
    	}
    	
    	
    }
    
    

   
   
    @FXML
    void TraiterBD(ActionEvent event) {

    }

    @FXML
    void TraiterXML(ActionEvent event) {

    }


}