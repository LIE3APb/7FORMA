package GUI_Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ConnectManager.SQLiteJDBC;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableRow;

public class Osnova implements Initializable{
	
	@FXML private DatePicker calendar;
	@FXML public TableView<Components.tRegistr> table = new TableView<Components.tRegistr>();
	@FXML ComboBox<String> ViborOtdeleni9 = new ComboBox<String>();
	
	private ObservableList<Components.tRegistr> masterData = FXCollections.observableArrayList();
	private ObservableList<Integer> dataidotdelenia = FXCollections.observableArrayList();

	@FXML public void onOpen_Otdelenie(ActionEvent event) throws Exception{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Otdelenie.fxml"));
			Parent root = (Parent)loader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Справочник отделений");
			stage.setScene(new Scene(root));
			Otdelenie controller = (Otdelenie)loader.getController();
			controller.setDialogstage(stage);
			stage.show();
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		}
		
	}
	@FXML public void onOpen_Koiki(ActionEvent event) throws Exception{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Koiki.fxml"));
			Parent root = (Parent)loader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			//stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Справочник коек");
			stage.setScene(new Scene(root));
			Koiki controller = (Koiki)loader.getController();
			controller.setDialogstage(stage);
			stage.show();
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		}
		
	}
	@FXML public void onOpen_OK(ActionEvent event) throws Exception{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/OK.fxml"));
			Parent root = (Parent)loader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Связь коек с отделением");
			stage.setScene(new Scene(root));
			OK controller = (OK)loader.getController();
			controller.setDialogstage(stage);
			stage.show();
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		}
		
	}
	@FXML public void onOpen_OP(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/OP.fxml"));
		Parent root = (Parent)loader.load();
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Параметры");
		stage.setScene(new Scene(root));
		OP controller = (OP)loader.getController();
		controller.setDialogstage(stage);
		stage.show();
	}
	
	@FXML public void onQuite(ActionEvent event) throws Exception{
		System.out.println("Exit");
		Platform.exit();
		System.exit(0);	
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//table.getColumns().clear();
		ViborOtdeleni9.setItems(FXCollections.observableArrayList(Get_List()));
		calendar.setValue(LocalDate.now());
		InitializeMenuItemToButton();
	}

	@SuppressWarnings("unchecked")
	private void ViewTable(int otdelenie,LocalDate date) throws SQLException{
		System.out.println("ViewTable");
		table.getColumns().clear();
		masterData.clear();
		
		TableColumn<Components.tRegistr,String> Sostoit = new TableColumn<Components.tRegistr,String>("Состоит");
		
		TableColumn<Components.tRegistr,String> Koika = new TableColumn<Components.tRegistr,String>("Койка");
		Koika.setCellValueFactory(new PropertyValueFactory<Components.tRegistr, String>("Name"));
		TableColumn<Components.tRegistr,Integer> Vsego = new TableColumn<Components.tRegistr,Integer>("Всего");
		Vsego.setCellValueFactory(new PropertyValueFactory<Components.tRegistr, Integer>("Vsego"));
		TableColumn<Components.tRegistr,Integer> Vsego_DS = new TableColumn<Components.tRegistr,Integer>("в т.ч. Дневной стационар");
		Vsego_DS.setCellValueFactory(new PropertyValueFactory<Components.tRegistr, Integer>("Vsego_DS"));
		TableColumn<Components.tRegistr,Integer> Vsego_Selo = new TableColumn<Components.tRegistr,Integer>("в т.ч. Село");
		Vsego_Selo.setCellValueFactory(new PropertyValueFactory<Components.tRegistr, Integer>("Vsego_Selo"));
		TableColumn<Components.tRegistr,Integer> Vsego_Mother = new TableColumn<Components.tRegistr,Integer>("Матерей по уходу");
		Vsego_Mother.setCellValueFactory(new PropertyValueFactory<Components.tRegistr, Integer>("Vsego_Mother"));
		
		TableColumn<Components.tRegistr,Boolean> flag = new TableColumn<Components.tRegistr,Boolean>();
		flag.setCellValueFactory(new PropertyValueFactory<Components.tRegistr, Boolean>("Flag"));
		flag.setVisible(false);
				
		Sostoit.getColumns().addAll(Vsego,Vsego_DS,Vsego_Selo);
		
		table.getColumns().setAll(Koika,Sostoit,Vsego_Mother,flag);
		
		Connection conn = SQLiteJDBC.Get_Conn();
		
		String sql = "SELECT Name,"
				+ "Vsego,"
				+ "Vsego_DS,"
				+ "Vsego_Selo,"
				+ "Vsego_Mother,"
				+ "CASE WHEN Date < date('now') THEN 'true' ELSE 'false' END AS Flag"
				+ " FROM Registr r INNER JOIN (SELECT id_OK,id_Registr,Name FROM OK INNER JOIN Koiki k ON OK.id_Koiki = k.id_Koiki "
				+ "WHERE id_Otdelenie = "+otdelenie+") o "
				+ "ON o.id_Registr = r.id_Registr";

		System.out.println(sql);
		
		ResultSet rs = conn.createStatement().executeQuery(sql);
		while(rs.next()){
			Components.tRegistr registr = new Components.tRegistr();
			registr.setName(rs.getString("Name"));
			registr.setVsego(rs.getInt("Vsego"));
			registr.setVsego_DS(rs.getInt("Vsego_DS"));
			registr.setVsego_Selo(rs.getInt("Vsego_Selo"));
			registr.setVsego_Mother(rs.getInt("Vsego_Mother"));
			registr.setFlag(rs.getBoolean("Flag"));
			masterData.add(registr);	
		}
		rs.close();
		conn.close();
		table.setItems(masterData);
	}
	
	@FXML public void ComboAction() throws SQLException{
		ViewTable(dataidotdelenia.get(ViborOtdeleni9.getSelectionModel().getSelectedIndex()),calendar.getValue());	
	}
	@FXML public void DatePickerAction() throws SQLException{
		if (!ViborOtdeleni9.getSelectionModel().isEmpty()){
			ViewTable(dataidotdelenia.get(ViborOtdeleni9.getSelectionModel().getSelectedIndex()),calendar.getValue());	
		}
	}
	
	private void print() throws SQLException{
		PrinterJob printerJob = PrinterJob.createPrinterJob();
		if (printerJob.showPageSetupDialog(null)&& printerJob.printPage(table)) {
			printerJob.endJob();
		}
		if (!ViborOtdeleni9.getSelectionModel().isEmpty()&!table.getSelectionModel().isEmpty()){
			ViewTable(dataidotdelenia.get(ViborOtdeleni9.getSelectionModel().getSelectedIndex()),calendar.getValue());	
		}
	}

	private void InitializeMenuItemToButton(){
		final ContextMenu contextMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Печать");
		item1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					print();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		MenuItem item2 = new MenuItem("Обновить");
		item2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				if (!ViborOtdeleni9.getSelectionModel().isEmpty()){
					try {
						ViewTable(ViborOtdeleni9.getSelectionModel().getSelectedIndex()+1,calendar.getValue());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}
			}
		});
		contextMenu.getItems().addAll(item1,item2);
		table.setContextMenu(contextMenu);		
	}
	
	private ObservableList<String> Get_List() {
		try {
			String Queary ="SELECT Name,id_Otdelenie FROM Otdelenie";
			Connection conn = SQLiteJDBC.Get_Conn();
			ResultSet rs = conn.createStatement().executeQuery(Queary);
			ObservableList<String> row = FXCollections.observableArrayList();
			while (rs.next()) {
				row.add(rs.getString(1));
				dataidotdelenia.add(rs.getInt(2));
			}
			return row;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@FXML public void onbtn(ActionEvent event) throws SQLException{
		if (!ViborOtdeleni9.getSelectionModel().isEmpty()){
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI_FX_Forms/Osnova_Insert.fxml"));
				Parent root = (Parent)loader.load();
				Stage stage = new Stage();
				stage.initModality(Modality.APPLICATION_MODAL);
				//stage.initStyle(StageStyle.UNDECORATED);
				stage.setTitle("Справочник отделений");
				stage.setScene(new Scene(root));
				Osnova_Insert controller = (Osnova_Insert)loader.getController();
				controller.setId_Otdelenie(dataidotdelenia.get(ViborOtdeleni9.getSelectionModel().getSelectedIndex()));
				controller.setDialogstage(stage);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Error: "+e.getMessage());
			}
		}
	}
	

	interface Printable{
	    void print(String s);
	}

	
	public class FormattedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
		public FormattedTableCellFactory() {
	    }
		@Override
		public TableCell<S, T> call(TableColumn<S, T> p) {
			TableCell<S, T> cell = new TableCell<S, T>() {
				@SuppressWarnings("unchecked")
				@Override
				protected void updateItem(Object item, boolean empty) {
					super.updateItem((T) item, empty);
					this.setStyle("-fx-background-color: green");
				}
			};
			return cell;		
		}
	}

	
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 ){
			Printable printer = q->System.out.println(q);
			printer.print("Hello Java!");
			 //System.out.println("2 click = "+table.getSelectionModel().getSelectedItem().getName());
			 /*table.setRowFactory( row -> new TableRow<Components.tRegistr>()  {
				 public void updateItem(Components.tRegistr item, boolean empty){
					 super.updateItem(item, empty);
					 this.setStyle("-fx-background-color: green");
				 }
			 });*/
			//table.setRowFactory(value -> Callback<TableView<S>,TableRow<S>>(value));
			Callback<TableView<Components.tRegistr>,TableRow<Components.tRegistr>> rf =table.getRowFactory();
			System.out.println(rf.getClass().getName());

			
			//(TableRow<Components.tRegistr>) table.getItems().get(table.getSelectionModel().getSelectedIndex());
					 //setAll(table.getSelectionModel().getSelectedItems());
			

			
			
		}
	}
	
	@FXML public void OnMousePressed(MouseEvent event){
		//if(!table.getSelectionModel().isEmpty()){
			//table.getSelectionModel().getSelectedItem().setFlag(true);
			//System.out.println("Select = "+table.getSelectionModel().getSelectedItem().getFlag());
			
			table.setRowFactory( row -> new TableRow<Components.tRegistr>()  {
				
				public void updateItem(Components.tRegistr item, boolean empty){
					super.updateItem(item, empty);

					
					//System.out.println("item = "+item.getFlag());
					//if(item.getFlag()){
						this.setStyle("-fx-background-color: green");
					//}else{
					//	row.setStyle("-fx-background-color: black");
					//}
				}		
			});
		
		/*table.setRowFactory(row -> new TableRow<Components.tRegistr>() {
			public void updateItem(Components.tRegistr item, boolean empty){
				super.updateItem(item, empty);
				//setStyle("-fx-background-color: green");
				//if(table.getSelectionModel().getSelectedItems().contains(item)){
					//setStyle("-fx-background-color: green");
				if(!table.getSelectionModel().isEmpty()){
				if(item.getFlag()){
					this.setStyle("-fx-background-color: green");
				}else{
					this.setStyle("-fx-background-color: black");
				}
				}
				//}
			}
		});*/
				
		//}
	}

	
}

