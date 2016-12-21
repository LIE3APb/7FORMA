package Components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class tRegistr {
	
	public tRegistr(){
		this.Flag = new SimpleBooleanProperty(false);
	}
	
	private SimpleStringProperty Name = new SimpleStringProperty();
	public String getName(){return Name.get();}
	public void setName(String value){this.Name.set(value);}
	
	private SimpleIntegerProperty Vsego = new SimpleIntegerProperty();
	public int getVsego(){return Vsego.get();}
	public void setVsego(int value){this.Vsego.set(value);}
	
	private SimpleIntegerProperty Vsego_DS = new SimpleIntegerProperty();
	public int getVsego_DS(){return Vsego_DS.get();}
	public void setVsego_DS(int value){this.Vsego_DS.set(value);}
	
	private SimpleIntegerProperty Vsego_Selo = new SimpleIntegerProperty();
	public int getVsego_Selo(){return Vsego_Selo.get();}
	public void setVsego_Selo(int value){this.Vsego_Selo.set(value);}
	
	private SimpleIntegerProperty Vsego_Mother = new SimpleIntegerProperty();
	public int getVsego_Mother(){return Vsego_Mother.get();}
	public void setVsego_Mother(int value){this.Vsego_Mother.set(value);}
	
	private SimpleStringProperty Date = new SimpleStringProperty();
	public String getDate(){return Date.get();}
	public void setDate(String value){this.Date.set(value);}
	
	private final BooleanProperty Flag;
	public void setFlag(boolean val) {this.Flag.setValue(val);}
	public boolean getFlag() {return Flag.get();}

}
