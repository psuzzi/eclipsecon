package com.asegno.e4.banking.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

import com.asegno.e4.banking.data.Utils;

@Entity
@Table(name="T_CUSTOMER")
@XmlRootElement
public class Customer extends BaseModel{
	
	private String firstName;
	private String lastName;
	private String address;
	private String email;
	private Calendar birthDate;
	
	public Customer() {
		super();
	}
	
	public Customer(String firstName, String lastName, String address, String email, String birthDateString) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.email = email;
		setBirthDateString(birthDateString);
	}

	public String getDescription() {
		return String.format("%s %s, %s (%s)", firstName, lastName, address, id);
	}
	
	private List<Account> accounts = new ArrayList<>();
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		firePropertyChange("firstName", this.firstName, this.firstName = firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		firePropertyChange("lastName", this.lastName, this.lastName = lastName);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		firePropertyChange("address", this.address, this.address = address);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		firePropertyChange("email", this.email, this.email = email);
	}

	public Calendar getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Calendar birthDate) {
		firePropertyChange("birthDate", this.birthDate, this.birthDate = birthDate);
	}

	public String getBirthDateString() {
		return Utils.dateToString(birthDate.getTime());
	}
	
	public void setBirthDateString(String birthDateString) {
		Calendar birthDateCal = Calendar.getInstance();
		birthDateCal.setTime(Utils.stringToDate(birthDateString));
		setBirthDate(birthDateCal);			
	}

	@XmlList
	@XmlIDREF
	@XmlElement(name="accounts")
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		firePropertyChange("accounts", this.accounts, this.accounts = accounts);
	}
	
}
