package Components;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Osnova {
	
	public Osnova(){
	}
	
	private SimpleStringProperty Name = new SimpleStringProperty();
	public String getName(){return Name.get();}
	public void setName(String value){this.Name.set(value);}
	
	private SimpleIntegerProperty IN_ALL = new SimpleIntegerProperty();
	public int getIN_ALL(){return IN_ALL.get();}
	public void setIN_ALL(int value){this.IN_ALL.set(value);}
	
	private SimpleIntegerProperty IN_DS = new SimpleIntegerProperty();
	public int getIN_DS(){return IN_DS.get();}
	public void setIN_DS(int value){this.IN_DS.set(value);}
	
	private SimpleIntegerProperty IN_Selo = new SimpleIntegerProperty();
	public int getIN_Selo(){return IN_Selo.get();}
	public void setIN_Selo(int value){this.IN_Selo.set(value);}
	
	private SimpleIntegerProperty OUT_ALL = new SimpleIntegerProperty();
	public int getOUT_ALL(){return OUT_ALL.get();}
	public void setOUT_ALL(int value){this.OUT_ALL.set(value);}
	
	private SimpleIntegerProperty OUT_DS = new SimpleIntegerProperty();
	public int getOUT_DS(){return OUT_DS.get();}
	public void setOUT_DS(int value){this.OUT_DS.set(value);}
	
	private SimpleIntegerProperty OUT_Selo = new SimpleIntegerProperty();
	public int getOUT_Selo(){return OUT_Selo.get();}
	public void setOUT_Selo(int value){this.OUT_Selo.set(value);}
	
	private SimpleIntegerProperty id_OK = new SimpleIntegerProperty();
	public int getid_OK(){return id_OK.get();}
	public void setid_OK(int value){this.id_OK.set(value);}
	
	private SimpleIntegerProperty id_Osnova = new SimpleIntegerProperty();
	public int getid_Osnova(){return id_Osnova.get();}
	public void setid_Osnova(int value){this.id_Osnova.set(value);}


}
