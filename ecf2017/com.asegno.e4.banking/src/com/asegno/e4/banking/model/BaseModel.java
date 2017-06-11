package com.asegno.e4.banking.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.persistence.Column;
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
	
	protected String id;

    protected final PropertyChangeSupport changeSupport =  new PropertyChangeSupport(this);
    
    public BaseModel() {
		id = ""+(long) (Math.random() * 2 * 100000);
	}
    
    @Id
    @Column(name="ID")
    @XmlAttribute
    @XmlID
    public String getId() {
		return id;
	}
    
    public void setId(String id) {
    	firePropertyChange("id", this.id, this.id = id);
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
