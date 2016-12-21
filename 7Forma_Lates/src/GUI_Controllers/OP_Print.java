package GUI_Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class OP_Print implements Initializable{
	private Stage thisdialogstage;
	@FXML public WebView WView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Автоматически созданная заглушка метода
		
	}
	@FXML public void KeyPress(KeyEvent key){
		if (key.getCode() == KeyCode.ESCAPE){
			getDialogstage().close();
		}
	}
	
	public Stage getDialogstage() {
		return thisdialogstage;
	}
	public void setDialogstage(Stage thisdialogstage) {
		this.thisdialogstage = thisdialogstage;
	}
	
	@FXML public void onPrint(ActionEvent event) {
		print();
	}
	private void print(){
		PrinterJob printerJob = PrinterJob.createPrinterJob();
		if (printerJob.showPageSetupDialog(null)&& printerJob.printPage(WView)) {
			printerJob.endJob();
		}
	}
}
