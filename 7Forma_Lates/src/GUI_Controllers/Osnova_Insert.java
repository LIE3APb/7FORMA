package GUI_Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ConnectManager.SQLiteJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Osnova_Insert implements Initializable{
	private Stage thisdialogstage;
	private Integer id_Otdelenie;
	private Integer id_OK;
	private String OK_Date;
	private String update_IN_ALL;
	private String update_IN_DS;
	private String update_IN_Selo;
	private String update_OUT_All;
	private String update_OUT_DS;
	private String update_OUT_Selo;
	private Integer r_Vsego;
	private Integer r_Vsego_DS;
	private Integer r_Vsego_Selo;
	private Integer r_Vsego_Mother;
	private Integer r_id_Registr;
	private boolean TypeFormi=true;;//Если Истина то Добавить иначе Изменить
	private ObservableList<Integer> dateid_OK = FXCollections.observableArrayList();


	@FXML ComboBox<String> ViborKoiki = new ComboBox<String>();
	@FXML Button btnInsert;
	@FXML TextField IN_All; 
	@FXML TextField IN_DS; 
	@FXML TextField IN_Selo; 
	@FXML TextField OUT_All; 
	@FXML TextField OUT_DS; 
	@FXML TextField OUT_Selo;
	@FXML TextField IN_Perevod;
	@FXML TextField OUT_Perevod; 
	@FXML TextField IN_Mother;
	@FXML TextField OUT_Mother;
	@FXML TextField DIE; 

	@FXML public void onInsert(ActionEvent event) throws Exception {		
		if (!ViborKoiki.getSelectionModel().isEmpty()&!ProverkaZapolneniyaPoleiy()){
			if(isTypeFormi()){
				Connection conn = ConnectManager.SQLiteJDBC.Get_Conn();						
				String query = "INSERT INTO Osnova (id_OK,IN_All,IN_DS,IN_Selo,OUT_All,OUT_DS,OUT_Selo,IN_Perevod,OUT_Perevod,"
						+ "IN_Mother,OUT_Mother,DIE,Date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setInt(1,getId_OK());
				pstmt.setString(2,IN_All.getText());
				pstmt.setString(3,IN_DS.getText());
				pstmt.setString(4,IN_Selo.getText());
				pstmt.setString(5,OUT_All.getText());
				pstmt.setString(6,OUT_DS.getText());
				pstmt.setString(7,OUT_Selo.getText());
				pstmt.setString(8,IN_Perevod.getText());
				pstmt.setString(9,OUT_Perevod.getText());
				pstmt.setString(10,IN_Mother.getText());
				pstmt.setString(11,OUT_Mother.getText());
				pstmt.setString(12,DIE.getText());
				pstmt.setString(13,LocalDate.now().toString());
				
				pstmt.executeUpdate();
				
				String sql = "SELECT r.id_Registr AS id_Registr,r.Vsego AS Vsego,r.Vsego_DS AS Vsego_DS,"
						+ "r.Vsego_Selo AS Vsego_Selo,r.Vsego_Mother AS Vsego_Mother FROM Registr r INNER JOIN OK ok ON "
						+ "r.id_Registr = ok.id_Registr WHERE ok.id_OK = "+ getId_OK();
				
				System.out.println("sql = "+sql);
				
				ResultSet rs = conn.createStatement().executeQuery(sql);
				while(rs.next()){
					setR_id_Registr(rs.getInt("id_Registr"));
					setR_Vsego(rs.getInt("Vsego"));
					setR_Vsego_DS(rs.getInt("Vsego_DS"));
					setR_Vsego_Selo(rs.getInt("Vsego_Selo"));
					setR_Vsego_Mother(rs.getInt("Vsego_Mother"));
				}
				System.out.println("id_Registr = "+getR_id_Registr());
				
				String query2 ="UPDATE Registr SET Vsego = "+getR_Vsego()+"+"+IN_All.getText()+"-"+OUT_All.getText()
						+ ",Vsego_DS = "+getR_Vsego_DS()+"+"+IN_DS.getText()+"-"+OUT_DS.getText()
						+ ",Vsego_Selo = "+getR_Vsego_Selo()+"+"+IN_Selo.getText()+"-"+OUT_Selo.getText()
						+ ",Vsego_Mother = "+getR_Vsego_Mother()+"+"+IN_Mother.getText()+"-"+OUT_Mother.getText()
						+ ",Date = '"+LocalDate.now().toString()+"'"
						+ " WHERE (id_Registr = "+getR_id_Registr()+")";
				System.out.println("query2 = "+query2);
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(query2);
				
				
				rs.close();
				pstmt.close();
				stmt.close();
				conn.close();
				getDialogstage().close();
				
			} else{
				update();
			}			
		}		
	}
	private void update() throws SQLException{
		Statement stmt = null;
		Connection conn = ConnectManager.SQLiteJDBC.Get_Conn();
		stmt = conn.createStatement();
		
		String sql = "INSERT INTO Osnova (id_OK,id_Registr,IN_All,IN_DS,IN_Selo,OUT_All,OUT_DS,OUT_Selo,Date) " +
                "VALUES ("+getId_OK()+",1"
                +","+IN_All.getText()
                +","+IN_DS.getText()
                +","+IN_Selo.getText()
                +","+OUT_All.getText()
                +","+OUT_DS.getText()
                +","+OUT_Selo.getText()
                +",'"+getOK_Date()+"')";
					//(isDataNull(Date_Close.getValue()) ? "9999-12-31" : Date_Close.getValue())+"');"; 
		System.out.println(sql);
		
		//stmt.executeUpdate(sql);
		stmt.close();
		conn.close();
		getDialogstage().close();
		
	}
	
	private boolean ProverkaZapolneniyaPoleiy() {
		if(IN_All.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Поступило всего'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(IN_DS.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Поступило в т.ч. Дневной стационар'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(IN_Selo.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Поступило в т.ч. сельских'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(OUT_All.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Выписано всего'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(OUT_DS.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Выписано в т.ч. Дневной стационар'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(OUT_Selo.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Выписано в т.ч. сельских'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(IN_Mother.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Поступило матерей по уходу'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(OUT_Mother.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Выписано матерей по уходу'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(IN_Perevod.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Переведено из других отделений'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(OUT_Perevod.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Переведено в другие отделения'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(DIE.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Не заполнено поле 'Умерло'","Ошибка заполнения", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
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

			//ViborKoiki.setItems(FXCollections.observableArrayList(Get_List()));

		btnInsert.setDisable(true);
		
	}	
	private ObservableList<String> Get_List() throws SQLException {
		System.out.println(getId_Otdelenie());
			String Queary ="SELECT Name,ok.id_OK FROM Koiki k INNER JOIN OK ok ON k.id_Koiki = ok.id_Koiki WHERE ok.id_Otdelenie = "+getId_Otdelenie();
			Connection conn = SQLiteJDBC.Get_Conn();
			ResultSet rs = conn.createStatement().executeQuery(Queary);
			ObservableList<String> row = FXCollections.observableArrayList();
			while (rs.next()) {
				row.add(rs.getString(1));
				dateid_OK.add(rs.getInt(2));
			}
			return row;
	}
	public Stage getDialogstage() {
		return thisdialogstage;
	}
	public void setDialogstage(Stage dialogstage) {
		this.thisdialogstage = dialogstage;
	}
	public Integer getId_Otdelenie() {
		return id_Otdelenie;
	}
	public void setId_Otdelenie(Integer id_otd) throws SQLException {
		this.id_Otdelenie = id_otd;
		ViborKoiki.setItems(FXCollections.observableArrayList(Get_List()));
	}
	public Integer getId_OK() {
		return id_OK;
	}
	public void setId_OK(Integer integer) {
		this.id_OK = Integer.valueOf(integer);
	}
	public String getOK_Date() {
		return OK_Date;
	}
	public void setOK_Date(String oK_Date) {
		OK_Date = oK_Date;
	}

	public boolean isTypeFormi() {
		return TypeFormi;
	}

	public void setTypeFormi(boolean typeFormi) {
		TypeFormi = typeFormi;
	}

	public String getUpdate_IN_ALL() {
		return update_IN_ALL;
	}
	public void setUpdate_IN_ALL(String update_IN_ALL) {
		this.IN_All.setText(update_IN_ALL);
		this.update_IN_ALL = update_IN_ALL;
	}
	public String getUpdate_IN_DS() {
		return update_IN_DS;
	}
	public void setUpdate_IN_DS(String update_IN_DS) {
		this.IN_DS.setText(update_IN_DS);
		this.update_IN_DS = update_IN_DS;
	}
	public String getUpdate_IN_Selo() {
		return update_IN_Selo;
	}
	public void setUpdate_IN_Selo(String update_IN_Selo) {
		this.IN_Selo.setText(update_IN_Selo);
		this.update_IN_Selo = update_IN_Selo;
	}
	public String getUpdate_OUT_All() {
		return update_OUT_All;
	}
	public void setUpdate_OUT_All(String update_OUT_All) {
		this.OUT_All.setText(update_OUT_All);
		this.update_OUT_All = update_OUT_All;
	}
	public String getUpdate_OUT_DS() {
		return update_OUT_DS;
	}
	public void setUpdate_OUT_DS(String update_OUT_DS) {
		this.OUT_DS.setText(update_OUT_DS);
		this.update_OUT_DS = update_OUT_DS;
	}
	public String getUpdate_OUT_Selo() {
		return update_OUT_Selo;
	}
	public void setUpdate_OUT_Selo(String update_OUT_Selo) {
		this.OUT_Selo.setText(update_OUT_Selo);
		this.update_OUT_Selo = update_OUT_Selo;
	}
	public Integer getR_Vsego() {
		return r_Vsego;
	}
	public void setR_Vsego(Integer r_Vsego) {
		this.r_Vsego = r_Vsego;
	}
	public Integer getR_Vsego_DS() {
		return r_Vsego_DS;
	}
	public void setR_Vsego_DS(Integer r_Vsego_DS) {
		this.r_Vsego_DS = r_Vsego_DS;
	}
	public Integer getR_Vsego_Selo() {
		return r_Vsego_Selo;
	}
	public void setR_Vsego_Selo(Integer r_Vsego_Selo) {
		this.r_Vsego_Selo = r_Vsego_Selo;
	}
	public Integer getR_Vsego_Mother() {
		return r_Vsego_Mother;
	}
	public void setR_Vsego_Mother(Integer r_Vsego_Mother) {
		this.r_Vsego_Mother = r_Vsego_Mother;
	}
	public Integer getR_id_Registr() {
		return r_id_Registr;
	}
	public void setR_id_Registr(Integer r_id_Registr) {
		this.r_id_Registr = r_id_Registr;
	}
	@FXML public void ComboAction(){
		setId_OK(dateid_OK.get(ViborKoiki.getSelectionModel().getSelectedIndex()));
		btnInsert.setDisable(false);
		
		// id_OK = SQLiteJDBC.getSecondValue().get(ViborKoiki.getSelectionModel().getSelectedIndex())
		
		//System.out.println("Osnova_Insert ComboAction: "+SQLiteJDBC.getSecondValue().get(ViborKoiki.getSelectionModel().getSelectedIndex()));
		//ViewTable(str);
		//Table_Otdelenie.getColumns().get(2).setVisible(false);
		//btnAdd.setDisable(false);
	}
	
	
	

}
