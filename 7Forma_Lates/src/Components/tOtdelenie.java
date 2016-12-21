package Components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;

public class tOtdelenie {
	public tOtdelenie(){
	}

	private SimpleStringProperty Name = new SimpleStringProperty();
	public String getName(){return Name.get();}
	public void setName(String value){this.Name.set(value);}
	
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

	private boolean isNull(final String str) {
		return str == null || str.isEmpty() || str.equals("null");
	}
}