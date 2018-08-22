package com.itemis.e4.banking.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Customer extends BaseModel{
	
	static final String ID_CUSTOMER = "customer";
	static final String ID_FIRST_NAME = "firstName";
	static final String ID_LAST_NAME = "lastName";
	static final String ID_ADDRESS = "address";
	static final String ID_ACCOUNTS = "accounts";
	
	private String firstName;
	private String lastName;
	private String address;
	
	private List<Account> accounts = new ArrayList<>();
	
	public Customer() {
		super();
	}
	
	public Customer(String firstName, String lastName, String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}
	
	// description
	
	public String getDescription() {
		return String.format("%s %s, %s (%s)", firstName, lastName, address, id);
	}
	
	// get/setters
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		firePropertyChange(ID_FIRST_NAME, this.firstName, this.firstName = firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		firePropertyChange(ID_LAST_NAME, this.lastName, this.lastName = lastName);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		firePropertyChange(ID_ADDRESS, this.address, this.address = address);
	}
	
	@XmlList
	@XmlIDREF
	@XmlElement
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		firePropertyChange(ID_ACCOUNTS, this.accounts, this.accounts = accounts);
	}
	
	// additional get/setters for UI databinding
	
	public List<Transaction> getTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		for (Account a : accounts) {
			transactions.addAll(a.getTransactions());
		}
		return transactions;
	}

}
