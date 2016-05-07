/**
 * File: GUIMain
 * Last Update: August 2013
 */
package cpusim;

import cpusim.gui.desktop.DesktopController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * A class to start the GUI. This is used every time 
 * the application is used unless it is run in command line mode.
 */
public class GUIMain extends Application {
	
	/**
	 * Starts the GUI.
	 */
	@Override
	public void start(Stage stage)
    {
        List<String> params = getParameters().getRaw();
		
        //netbeans has an issue with running CPUSim from the command line
        // so if you use netbeans, you may need to set the the variables
        // to empty strings instead of using params.get(0) and params.get(1).
        String machineFileName = params.get(0);
		String textFileName = params.get(1);

        // Set up the Scene with the Desktop and add it to the stage
		Mediator mediator = new Mediator(stage);
		DesktopController deskController = new DesktopController(mediator, stage);
		Pane mainPane = null;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"gui/desktop/desktop.fxml"));
		fxmlLoader.setController(deskController);
        try {
            mainPane = fxmlLoader.load();
        } catch (Exception e) {
            // should never happen
            assert false : "Unable to load file: gui/desktop/desktop.fxml";
        }
        Scene mainScene = new Scene(mainPane);
        stage.setScene(mainScene);

        // Load in the CPU Sim icon image
        URL iconURL = getClass().getResource("/cpusim/gui/about/cpusim_icon.jpg");
        Image icon = new Image(iconURL.toExternalForm());
        stage.getIcons().add(icon);

        // Load the stylesheet for the line numbers in the CodePanes
        URL styleSheetURL = getClass().getResource("/cpusim/gui/css/LineNumbers.css");
        String stylesheet = styleSheetURL.toExternalForm();
        mainScene.getStylesheets().add(stylesheet);

        // load the machine, if provided
        if (machineFileName.equals("")) {
            mediator.newMachine(); // a new machine
        }
        else {
            mediator.openMachine(new File(machineFileName));
        }

        // Now load the text file, if provided
        if (textFileName.equals("")) {
            deskController.addDefaultTab();
        }
        else {
            deskController.open(new File(textFileName));
        }

        // Fixes crash on Windows 10 systems with Intel Chips
        // See http://stackoverflow.com/questions/31786980/javafx-windows-10-combobox-error
        // Setting this property will prevent automatic screen readers from working wth the application
        // and may disable other similar accessibility features
        System.setProperty("glass.accessible.force", "false");


        stage.show();
	}
}