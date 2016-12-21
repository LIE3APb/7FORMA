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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OK implements Initializable{
	private Stage thisdialogstage;

	@FXML private TableView<Components.tOK> Table_OK = new TableView<Components.tOK>() ;
	private ObservableList<Components.tOK> masterData = FXCollections.observableArrayList();
	@FXML private Button btnUpdate;
	@FXML private Button btnAdd;
	@FXML ComboBox<String> ViborOtdeleni9 = new ComboBox<String>();

	@FXML public void onInsert(ActionEvent event) throws Exception {
		if (!ViborOtdeleni9.getSelectionModel().isEmpty()){
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/OK_Insert.fxml"));
				Parent root = (Parent)loader.load();			
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Добавить койку в "+ViborOtdeleni9.getSelectionModel().getSelectedItem());
				stage.setScene(new Scene(root));
				OK_Insert controller =(OK_Insert)loader.getController();
				controller.setDialogstage(stage);
				controller.setId_Otdelenie(ViborOtdeleni9.getSelectionModel().getSelectedIndex());
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	@FXML public void onUpdate(ActionEvent event) throws ClassNotFoundException, SQLException {
		if (!ViborOtdeleni9.getSelectionModel().isEmpty()&!Table_OK.getSelectionModel().isEmpty()){
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/OK_Update.fxml"));
				Parent root = (Parent)loader.load();
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Изменить койку в "+ViborOtdeleni9.getSelectionModel().getSelectedItem());
				stage.setScene(new Scene(root));
				
				OK_Update controller =(OK_Update)loader.getController();
				controller.setDialogstage(stage);
				controller.setParentdialogstage(getDialogstage());
				controller.setUpdateText(Table_OK.getSelectionModel().getSelectedItem().getName());
				controller.setChislo_Koek(Table_OK.getSelectionModel().getSelectedItem().getChislo_Koek().toString());
				controller.setPlan_KD(Table_OK.getSelectionModel().getSelectedItem().getPlan_KD().toString());
				controller.setUpdateDate_Open(Table_OK.getSelectionModel().getSelectedItem().getDate_Open());
				controller.setUpdateDate_Close(Table_OK.getSelectionModel().getSelectedItem().getDate_Close());
				controller.setId_Otdelenie(ViborOtdeleni9.getSelectionModel().getSelectedIndex());		
				
				controller.setId_OK(Table_OK.getSelectionModel().getSelectedItem().getid_OK());			
				
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		btnAdd.setDisable(true);
		ViborOtdeleni9.setItems(FXCollections.observableArrayList(Get_List()));
	}
	
	private ObservableList<String> Get_List() {
		try {
			String Queary ="SELECT Name,id_Otdelenie FROM Otdelenie";
			Connection conn = SQLiteJDBC.Get_Conn();
			ResultSet rs = conn.createStatement().executeQuery(Queary);
			ObservableList<String> row = FXCollections.observableArrayList();
			while (rs.next()) {
				row.add(rs.getString(1));
			}
			return row;		
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void ViewTable(String queary) throws SQLException {			
			Table_OK.getColumns().clear();
			masterData.clear();
		
			TableColumn<Components.tOK,String> Name = new TableColumn<Components.tOK,String>("Койка");
			Name.setCellValueFactory(new PropertyValueFactory<Components.tOK, String>("Name"));
			TableColumn<Components.tOK,Integer> Chislo_Koek = new TableColumn<Components.tOK,Integer>("Число коек");
			Chislo_Koek.setCellValueFactory(cellData -> cellData.getValue().Chislo_KoekProperty().asObject());	
			TableColumn<Components.tOK,Integer> Plan_KD = new TableColumn<Components.tOK,Integer>("План К/Д");
			Plan_KD.setCellValueFactory(cellData -> cellData.getValue().Plan_KDProperty().asObject());	
			TableColumn<Components.tOK,String> Date_Open = new TableColumn<Components.tOK,String>("Действует с");
			Date_Open.setCellValueFactory(new PropertyValueFactory<Components.tOK, String>("Date_Open"));
			TableColumn<Components.tOK,String> Date_Close = new TableColumn<Components.tOK,String>("Действует по");
			Date_Close.setCellValueFactory(new PropertyValueFactory<Components.tOK, String>("Date_Close"));
		
			TableColumn<Components.tOK,Integer> id_OK = new TableColumn<Components.tOK,Integer>("id_OK");
			//id_OK.setCellValueFactory(new PropertyValueFactory<Components.tOK, String>("id_OK"));
			id_OK.setCellValueFactory(cellData -> cellData.getValue().id_OKProperty().asObject());
			id_OK.setVisible(false);
			
			Table_OK.getColumns().setAll(Name,Chislo_Koek,Plan_KD,Date_Open,Date_Close,id_OK);			

			Connection conn = SQLiteJDBC.Get_Conn();
			ResultSet rs = conn.createStatement().executeQuery(queary);
			while(rs.next()){
				Components.tOK ok = new Components.tOK();
				ok.setName(rs.getString("Name"));
				ok.setChislo_Koek(rs.getInt("Chislo_Koek"));
				ok.setPlan_KD(rs.getInt("Plan_KD"));
				ok.setDate_Open(rs.getString("Date_Open"));
				ok.setDate_Close(rs.getString("Date_Close"));
				ok.setid_OK(rs.getInt("id_OK"));		
				masterData.add(ok);
			}
			rs.close();
			conn.close();
			Table_OK.setItems(masterData);
	}

	public Stage getDialogstage() {
		return thisdialogstage;
	}

	public void setDialogstage(Stage dialogstage) {
		this.thisdialogstage = dialogstage;
	}
	
	@FXML public void ButtonChange(){
		if (!ViborOtdeleni9.getSelectionModel().isEmpty()&!Table_OK.getSelectionModel().isEmpty()){
			btnUpdate.setDisable(false);
		}
	}
	@FXML public void ComboAction() throws SQLException{
		
		String str = "SELECT k.Name AS Name,ok.Chislo_Koek AS Chislo_Koek,ok.Plan_KD AS Plan_KD,ok.Date_Open AS Date_Open,ok.Date_Close AS Date_Close, ok.id_OK AS id_OK "
				+ "FROM Koiki k "
				+ "INNER JOIN OK ok "
				+ "ON k.id_Koiki = ok.id_Koiki "
				+ "WHERE ok.id_Otdelenie = "+(ViborOtdeleni9.getSelectionModel().getSelectedIndex()+1);
		ViewTable(str);
		btnAdd.setDisable(false);
		btnUpdate.setDisable(true);
	}
	
	
	

}
