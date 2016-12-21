package GUI_Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class OK_Update implements Initializable{
	private Stage thisdialogstage;
	private Stage Parentdialogstage;
	private String UpdateText = "Initial";
	private LocalDate UpdateDate_Open;
	private LocalDate UpdateDate_Close;
	private Integer id;
	private Integer id_Otdelenie;
	private Integer id_Koiki;
	private Integer id_OK;	
	
	@FXML private Label Name;
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
		//Name.setItems(FXCollections.observableArrayList(Get_List()));
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


	public String getUpdateText() {
		return UpdateText;
	}
	public void setUpdateText(String updateText) {
		if (updateText==null){updateText="";}
		this.UpdateText = updateText;
		//Name.setText(updateText);
		Name.setText(updateText);
	}
	public LocalDate getUpdateDate_Open() {
		return UpdateDate_Open;
	}
	public void setUpdateDate_Open(String updateDate) {
		if (isNull(updateDate)){
			updateDate="1900-01-01";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateTime = LocalDate.parse(updateDate, formatter);
		this.UpdateDate_Open = dateTime;
		Date_Open.setValue(dateTime);
	}
	public LocalDate getUpdateDate_Close() {
		return UpdateDate_Close;
	}
	public void setUpdateDate_Close(String updateDate) {
		if (isNull(updateDate)){
			updateDate="9999-12-31";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateTime = LocalDate.parse(updateDate, formatter);
		this.UpdateDate_Close = dateTime;
		Date_Close.setValue(dateTime);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id+1;
	}
	public Integer getId_Otdelenie() {
		return id_Otdelenie;
	}
	public void setId_Otdelenie(Integer id_Otdelenie) {
		this.id_Otdelenie = id_Otdelenie+1;
	}
	public Integer getId_Koiki() {
		return id_Koiki;
	}

	public void setId_Koiki(Integer id_Koiki) {
		this.id_Koiki = id_Koiki;
	}

	public Integer getId_OK() {
		return id_OK;
	}

	public void setId_OK(Integer id_OK) {
		this.id_OK = id_OK;
	}

	public void setChislo_Koek(String chislo_Koek) {
		Chislo_Koek.setText(chislo_Koek);
	}
	public void setPlan_KD(String plan_kd) {
		Plan_KD.setText(plan_kd);
	}

	private boolean isNull(final String str){
		return str == null || str.isEmpty() || str.equals("null");
	}
	private boolean isDataNull(LocalDate date){
		return date == null;
	}
	
	@FXML public void onInsert(ActionEvent event) throws SQLException{
		Connection conn = ConnectManager.SQLiteJDBC.Get_Conn();
		String query = "UPDATE OK SET Chislo_Koek = ? ,Plan_KD = ? ,Date_Open = ? ,Date_Close = ? WHERE (id_OK = ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1,(isNull(Chislo_Koek.getText()) ? "0" : Chislo_Koek.getText()));
		pstmt.setString(2,(isNull(Plan_KD.getText()) ? "0" : Plan_KD.getText()));
		pstmt.setString(3,(isDataNull(Date_Open.getValue()) ? "1900-01-01" : Date_Open.getValue().toString()));
		pstmt.setString(4,(isDataNull(Date_Close.getValue()) ? "9999-12-31" : Date_Close.getValue().toString()));
		pstmt.setInt(5,getId_OK());
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
		getDialogstage().close();
	}
	
}