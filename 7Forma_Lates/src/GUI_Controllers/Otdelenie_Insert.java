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

public class Otdelenie_Insert implements Initializable{
	private Stage thisdialogstage;
	private Stage Parentdialogstage;
	private boolean TypeFormi;//Если Истина то Добавить иначе Изменить
	private String UpdateText = "Initial";
	private LocalDate UpdateDate;
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
			//getParentdialogstage().setTitle("blablabla");
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
	public void setTypeFormi(boolean tipeFormi,String name, String sDate_Open, String sDate_Close) {
		this.TypeFormi = tipeFormi;
		if (!tipeFormi){
			btnInsert.setText("Изменить");		
			Name.setText(name);
			Date_Open.setValue(LocalDate.parse(sDate_Open, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			Date_Close.setValue(LocalDate.parse(sDate_Close, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
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
		System.out.println("updateText = "+updateText);
	}
	public LocalDate getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(String updateDate) {
		if (isNull(updateDate)){
			updateDate="9999-12-31";
			System.out.println(updateDate);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateTime = LocalDate.parse(updateDate, formatter);
		this.UpdateDate = dateTime;
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
		String query = "INSERT INTO Otdelenie(Name,Date_Open,Date_Close) VALUES (?,?,?)";
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
		String query = "UPDATE Otdelenie SET Name = ? ,Date_Open = ? ,Date_Close = ? WHERE (id_Otdelenie = ?)";
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