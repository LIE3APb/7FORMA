package GUI_Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ConnectManager.SQLiteJDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class OP implements Initializable{
	private Stage thisdialogstage;
	
	@FXML private DatePicker Data_IN;
	@FXML private DatePicker Data_OUT;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Автоматически созданная заглушка метода
		
	}

	public Stage getdialogstage() {
		return thisdialogstage;
	}

	public void setDialogstage(Stage stage) {
		this.thisdialogstage = stage;		
	}
	@FXML public void KeyPress(KeyEvent key){
		if (key.getCode() == KeyCode.ESCAPE){
			getdialogstage().close();			
		}
	}
	@FXML public void on_OK(ActionEvent event) throws IOException{
		
		//String sourceFileName="/home/demonaz/workspace/7Forma_Lates/src/Otchet/Invoice.jrxml";
		String sourceFileName="C:\\Users\\802499\\git\\7FORMA\\7Forma_Lates\\src\\Otchet\\Invoice.jrxml";
		//String sourceFileName="C:/Users/802499/git/7FORMA/7Forma_Lates/src/Otchet/Invoice.jrxml";
		JOptionPane.showMessageDialog(null, sourceFileName);
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(sourceFileName);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("Data_IN", Data_IN.getValue().toString());
			parameters.put("Data_OUT", Data_OUT.getValue().toString());		
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,SQLiteJDBC.Get_Conn());
			JasperViewer visor = new JasperViewer(jasperPrint, false);
		
			visor.setVisible(true);
		} catch (JRException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
