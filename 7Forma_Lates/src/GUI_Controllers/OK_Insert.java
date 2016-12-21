package GUI_Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ConnectManager.SQLiteJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class OK_Insert implements Initializable{
	private Stage thisdialogstage;
	private Stage Parentdialogstage;
	private Integer id_Otdelenie;
	
	
	@FXML private ComboBox<String> Name;
	@FXML private TextField Chislo_Koek; 
	@FXML private TextField Plan_KD; 
	@FXML private DatePicker Date_Open;
	@FXML private DatePicker Date_Close;
	@FXML private Button btnInsert;
	
	@FXML public void KeyPress(KeyEvent key){
		if (key.getCode() == KeyCode.ESCAPE){
			getDialogstage().close();			
		}
	}

	@Override
 	public void initialize(URL location, ResourceBundle resources) {
		Name.setItems(FXCollections.observableArrayList(Get_List()));
	}
	
	public Stage getDialogstage() {
		return thisdialogstage;
	}
	public void setDialogstage(Stage dialogstage) {
		this.thisdialogstage = dialogstage;
	}
	public Stage getParentdialogstage() {
		return Parentdialogstage;
	}
	public void setParentdialogstage(Stage parentdialogstage) {
		this.Parentdialogstage = parentdialogstage;
	}
	public Integer getId_Otdelenie() {
		return id_Otdelenie;
	}
	public void setId_Otdelenie(Integer id_Otdelenie) {
		this.id_Otdelenie = id_Otdelenie+1;
	}
	private boolean isDataNull(LocalDate date){
		return date == null;
	}
	private boolean isNull(final String str){
		return str == null || str.isEmpty() || str.equals("null");
	}
	
	@FXML public void onInsert(ActionEvent event) throws SQLException{
		Connection conn = SQLiteJDBC.Get_Conn();	
		String query = "INSERT INTO Registr (Vsego,Vsego_DS,Vsego_Selo,Vsego_Mother,Date) VALUES (0,0,0,0,'"
				+(isDataNull(Date_Close.getValue()) ? LocalDate.now() : Date_Close.getValue())+"')";
		conn.createStatement().executeUpdate(query);		
		
		String sql="INSERT INTO OK (id_Otdelenie,id_Koiki,Chislo_Koek,Plan_KD,Date_Open,Date_Close,id_Registr) VALUES (?,?,?,?,?,?,"
				+ "(SELECT seq FROM sqlite_sequence WHERE name='Registr'))";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1,getId_Otdelenie());
		pstmt.setInt(2, (Name.getSelectionModel().getSelectedIndex()+1));
		pstmt.setString(3,(isNull(Chislo_Koek.getText()) ? "0" : Chislo_Koek.getText()));
		pstmt.setString(4,(isNull(Plan_KD.getText()) ? "0" : Plan_KD.getText()));
		pstmt.setString(5,isDataNull(Date_Open.getValue()) ? "1900-01-01" : Date_Open.getValue().toString());
		pstmt.setString(6,isDataNull(Date_Close.getValue()) ? "9999-12-31" : Date_Close.getValue().toString());
		
		pstmt.executeUpdate();

		pstmt.close();
		conn.close();
		getDialogstage().close();			
	}
	private ObservableList<String> Get_List() {
		try {
			String Queary ="SELECT Name,id_Koiki FROM Koiki";
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
	
}