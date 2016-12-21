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

public class Koiki implements Initializable{
	private Stage thisdialogstage;

	@FXML private TableView<Components.tKoiki> Table_Koiki = new TableView<Components.tKoiki>() ;
	@FXML private Button btnUpdate;
	
	private ObservableList<Components.tKoiki> masterData = FXCollections.observableArrayList();

	@FXML public void onInsert(ActionEvent event) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Koiki_Insert.fxml"));
			Parent root = (Parent)loader.load();			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Добавить койку");
			stage.setScene(new Scene(root));
			Koiki_Insert controller =(Koiki_Insert)loader.getController();
			controller.setDialogstage(stage);
			controller.setTypeFormi(true);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	@FXML public void onUpdate(ActionEvent event) throws ClassNotFoundException, SQLException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Koiki_Insert.fxml"));
			Parent root = (Parent)loader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Изменить койку");
			stage.setScene(new Scene(root));
			
			Koiki_Insert controller =(Koiki_Insert)loader.getController();
			controller.setDialogstage(stage);
			controller.setParentdialogstage(getDialogstage());
			controller.setTypeFormi(false);
			controller.setId(Table_Koiki.getSelectionModel().getSelectedIndex());
			controller.setUpdateText(Table_Koiki.getSelectionModel().getSelectedItem().getName());
			controller.setUpdateDate_Open(Table_Koiki.getSelectionModel().getSelectedItem().getDate_Open());
			controller.setUpdateDate_Close(Table_Koiki.getSelectionModel().getSelectedItem().getDate_Close());
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
		Table_Koiki.getColumns().clear();
		masterData.clear();
			
		TableColumn<Components.tKoiki,String> Name = new TableColumn<Components.tKoiki,String>("Койка");
		Name.setCellValueFactory(new PropertyValueFactory<Components.tKoiki, String>("Name"));
		TableColumn<Components.tKoiki,String> Date_Open = new TableColumn<Components.tKoiki,String>("Дата открытия");
		Date_Open.setCellValueFactory(new PropertyValueFactory<Components.tKoiki, String>("Date_Open"));
		TableColumn<Components.tKoiki,String> Date_Close = new TableColumn<Components.tKoiki,String>("Дата закрытия");
		Date_Close.setCellValueFactory(new PropertyValueFactory<Components.tKoiki, String>("Date_Close"));
		Table_Koiki.getColumns().setAll(Name,Date_Open,Date_Close);
		String queary="SELECT * FROM Koiki";
		Connection conn = SQLiteJDBC.Get_Conn();
		ResultSet rs = conn.createStatement().executeQuery(queary);
		while(rs.next()){
			Components.tKoiki koiki = new Components.tKoiki();
			koiki.setName(rs.getString("Name"));
			koiki.setDate_Open(rs.getString("Date_Open"));
			koiki.setDate_Close(rs.getString("Date_Close"));
			masterData.add(koiki);
		}
		rs.close();
		conn.close();
		Table_Koiki.setItems(masterData);
	}

	public Stage getDialogstage() {
		return thisdialogstage;
	}
	public void setDialogstage(Stage dialogstage) {
		this.thisdialogstage = dialogstage;
	}
	
	@FXML public void ButtonChange(){
		if (!Table_Koiki.getSelectionModel().isEmpty()){
			btnUpdate.setDisable(false);
		}	
	}

}
