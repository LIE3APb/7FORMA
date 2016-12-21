package GUI_Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import ConnectManager.SQLiteJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Otdelenie implements Initializable{
	private Stage thisdialogstage;

	@FXML private TableView<Components.tOtdelenie> Table_Otdelenie = new TableView<Components.tOtdelenie>() ;
	@FXML private Button btnUpdate;
	
	private ObservableList<Components.tOtdelenie> masterData = FXCollections.observableArrayList();

	@FXML public void onInsert(ActionEvent event) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Otdelenie_Insert.fxml"));
			Parent root = (Parent)loader.load();			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Добавить отделение");
			stage.setScene(new Scene(root));
			Otdelenie_Insert controller =(Otdelenie_Insert)loader.getController();
			controller.setDialogstage(stage);
			controller.setTypeFormi(true,"","","");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML public void onUpdate(ActionEvent event) throws ClassNotFoundException, SQLException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Otdelenie_Insert.fxml"));
			Parent root = (Parent)loader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Изменить отделение");
			stage.setScene(new Scene(root));
			
			Otdelenie_Insert controller =(Otdelenie_Insert)loader.getController();
			controller.setDialogstage(stage);
			controller.setParentdialogstage(getDialogstage());
			controller.setTypeFormi(false,Table_Otdelenie.getSelectionModel().getSelectedItem().getName(),
					Table_Otdelenie.getSelectionModel().getSelectedItem().getDate_Open(),
					Table_Otdelenie.getSelectionModel().getSelectedItem().getDate_Close());
			controller.setId(Table_Otdelenie.getSelectionModel().getSelectedIndex());
			//controller.setUpdateText(Table_Otdelenie.getSelectionModel().getSelectedItem().get(0));
			//controller.setUpdateDate(Table_Otdelenie.getSelectionModel().getSelectedItem().get(1));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void KeyPress(KeyEvent key){
		if (key.getCode() == KeyCode.ESCAPE){
			getDialogstage().close();
		}
	}
	
	@FXML public void retry(){
		System.out.println("smena focusa");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnUpdate.setDisable(true);
		try {
			ViewTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void ViewTable() throws SQLException {
		//String queary="SELECT Name AS Наименование, Date_Close AS 'Дата закрытия' FROM Otdelenie";	
		Table_Otdelenie.getColumns().clear();
		masterData.clear();
			
		TableColumn<Components.tOtdelenie,String> Name = new TableColumn<Components.tOtdelenie,String>("Отделение");
		Name.setCellValueFactory(new PropertyValueFactory<Components.tOtdelenie, String>("Name"));
		TableColumn<Components.tOtdelenie,String> Date_Open = new TableColumn<Components.tOtdelenie,String>("Дата открытия");
		Date_Open.setCellValueFactory(new PropertyValueFactory<Components.tOtdelenie, String>("Date_Open"));
		TableColumn<Components.tOtdelenie,String> Date_Close = new TableColumn<Components.tOtdelenie,String>("Дата закрытия");
		Date_Close.setCellValueFactory(new PropertyValueFactory<Components.tOtdelenie, String>("Date_Close"));
		Table_Otdelenie.getColumns().setAll(Name,Date_Open,Date_Close);
		String queary="SELECT * FROM Otdelenie";
		Connection conn = SQLiteJDBC.Get_Conn();
		ResultSet rs = conn.createStatement().executeQuery(queary);
		while(rs.next()){
			Components.tOtdelenie otdelenie = new Components.tOtdelenie();
			otdelenie.setName(rs.getString("Name"));
			otdelenie.setDate_Close(rs.getString("Date_Close"));
			otdelenie.setDate_Open(rs.getString("Date_Open"));
			masterData.add(otdelenie);
		}
		rs.close();
		conn.close();
		Table_Otdelenie.setItems(masterData);
	}

	public Stage getDialogstage() {
		return thisdialogstage;
	}
	public void setDialogstage(Stage dialogstage) {
		this.thisdialogstage = dialogstage;
	}
	
	@FXML public void ButtonChange(){
		if (!Table_Otdelenie.getSelectionModel().isEmpty()){
			btnUpdate.setDisable(false);
		}		
	}

}
