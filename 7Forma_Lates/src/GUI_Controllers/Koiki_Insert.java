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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Koiki_Insert implements Initializable{
	private Stage thisdialogstage;
	private Stage Parentdialogstage;
	private boolean TypeFormi;//Если Истина то Добавить иначе Изменить
	private String UpdateText = "Initial";
	private LocalDate UpdateDate_Open;
	private LocalDate UpdateDate_Close;
	private Integer id;
	
	
	@FXML private TextField Name;
	@FXML private DatePicker Date_Open;
	@FXML private DatePicker Date_Close;
	@FXML private Button btnInsert;
	
	@FXML public void onInsert(ActionEvent event) throws Exception{
		if(isTypeFormi()){
			Insert();
		} else{
			Update();
		}
		
	}
	@FXML public void onUpdate(ActionEvent event) throws Exception{
		
	}
	
	@FXML public void KeyPress(KeyEvent key){
		if (key.getCode() == KeyCode.ESCAPE){
			getDialogstage().close();			
		}
	}

	
	@Override
 	public void initialize(URL location, ResourceBundle resources) {
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
	public boolean isTypeFormi() {
		return TypeFormi;
	}
	public void setTypeFormi(boolean tipeFormi) {
		this.TypeFormi = tipeFormi;
		if (!tipeFormi){
			btnInsert.setText("Изменить");			
		} else{
			btnInsert.setText("Добавить");
		}
	}
	public String getUpdateText() {
		return UpdateText;
	}
	public void setUpdateText(String updateText) {
		if (updateText==null){updateText="";}
		this.UpdateText = updateText;
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
	private boolean isNull(final String str){
		return str == null || str.isEmpty() || str.equals("null");
	}
	private boolean isDataNull(LocalDate date){
		return date == null;
	}
	
	private void Insert() throws SQLException{
		Connection conn = ConnectManager.SQLiteJDBC.Get_Conn();
		String query = "INSERT INTO Koiki(Name,Date_Open,Date_Close) VALUES (?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1,Name.getText());
		pstmt.setString(2,(isDataNull(Date_Open.getValue()) ? "1900-01-01" : Date_Open.getValue().toString()));
		pstmt.setString(3,(isDataNull(Date_Close.getValue()) ? "9999-12-31" : Date_Close.getValue().toString()));
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();	
		getDialogstage().close();
	}
	private void Update() throws SQLException{
		Connection conn = ConnectManager.SQLiteJDBC.Get_Conn();
		String query = "UPDATE Koiki SET Name = ? ,Date_Open = ? ,Date_Close = ? WHERE (id_Koiki = ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1,Name.getText());
		pstmt.setString(2,(isDataNull(Date_Open.getValue()) ? "1900-01-01" : Date_Open.getValue().toString()));
		pstmt.setString(3,(isDataNull(Date_Close.getValue()) ? "9999-12-31" : Date_Close.getValue().toString()));
		pstmt.setInt(4,getId());
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
		getDialogstage().close();
	}
	
}