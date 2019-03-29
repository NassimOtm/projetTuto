


import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

	public class MainInterface extends Application {


		@Override
		public void start(Stage primaryStage) {
			try {

			FXMLLoader loader = new FXMLLoader();
			URL url = new File("src/Interface.fxml").
			toURI().toURL();
			loader.setLocation(url);
			System.out.println(loader.getLocation());
			Pane root = new Pane(); 
			root=loader.load();
	     	Scene scene = new Scene(root,600,600);
			primaryStage.setScene(scene);
			primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void main(String[] args) {
			launch(args);
		}
	}
