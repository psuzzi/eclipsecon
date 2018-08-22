package com.itemis.e4.banking.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@MappedSuperclass
@XmlSeeAlso({Bank.class, Customer.class, Account.class, Transaction.class})
@XmlRootElement
public class BaseModel {
	
	static final String ID_ID = "id";

	protected String id;

    protected final PropertyChangeSupport changeSupport =  new PropertyChangeSupport(this);
    
    public BaseModel() {
		id = ""+(long) (Math.random() * 2 * 100000);
	}
    
    @Id
    @XmlAttribute
    @XmlID
    public String getId() {
		return id;
	}
    
    public void setId(String id) {
    	firePropertyChange(ID_ID, this.id, this.id = id);
	}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
