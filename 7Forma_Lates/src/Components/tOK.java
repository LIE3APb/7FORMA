package Components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class tOK {
	public tOK(){
	}
	private SimpleStringProperty Name = new SimpleStringProperty();
	public String getName(){return Name.get();}
	public void setName(String value){this.Name.set(value);}
	
	private SimpleStringProperty Date_Open = new SimpleStringProperty();
	public String getDate_Open(){
		if (isNull(Date_Open.get())){
			this.Date_Open.set("1900-01-01");
		}
		return this.Date_Open.get();
	}
	public void setDate_Open(String value){
		if (isNull(value)){
			value="1900-01-01";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateTime = LocalDate.parse(value, formatter);
		this.Date_Open.set(dateTime.toString());
	}
	
	private SimpleStringProperty Date_Close = new SimpleStringProperty();
	public String getDate_Close(){
		if (isNull(Date_Close.get())){
			this.Date_Close.set("9999-12-31");
		}
		return this.Date_Close.get();
	}
	public void setDate_Close(String value){
		if (isNull(value)){
			value="9999-12-31";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateTime = LocalDate.parse(value, formatter);
		this.Date_Close.set(dateTime.toString());
	}
	
	private SimpleIntegerProperty id_OK = new SimpleIntegerProperty();
	public Integer getid_OK(){return id_OK.get();}
	public void setid_OK(Integer value){this.id_OK.set(value);}
	public IntegerProperty id_OKProperty() {return id_OK;}
	
	private SimpleIntegerProperty Chislo_Koek = new SimpleIntegerProperty();
	public Integer getChislo_Koek(){return Chislo_Koek.get();}
	public void setChislo_Koek(Integer value){this.Chislo_Koek.set(value);}
	public IntegerProperty Chislo_KoekProperty() {return Chislo_Koek;}
	
	private SimpleIntegerProperty Plan_KD = new SimpleIntegerProperty();
	public Integer getPlan_KD(){return Plan_KD.get();}
	public void setPlan_KD(Integer value){this.Plan_KD.set(value);}
	public IntegerProperty Plan_KDProperty() {return Plan_KD;}

	private boolean isNull(final String str) {
		return str == null || str.isEmpty() || str.equals("null");
	}

}
