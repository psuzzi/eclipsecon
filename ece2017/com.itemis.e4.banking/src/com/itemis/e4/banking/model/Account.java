package com.itemis.e4.banking.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

import com.itemis.e4.banking.Utils;

@Entity
@XmlRootElement
public class Account extends BaseModel{
	
	static final String ID_CREATION_DATE = "creationDate";
	static final String ID_BALANCE = "balance";
	static final String ID_CUSTOMERS = "customers";
	static final String ID_TRANSACTIONS = "transactions";
	
	private Date creationDate;
	private double balance = 0.0d;

	private List<Customer> customers = new ArrayList<>();
	private List<Transaction> transactions = new ArrayList<>();
	
	public Account() {
		super();
		this.creationDate = Utils.stringToDate("2016-02-01");
	}
	
	// link Account to its customers
	
	public Account link(Customer... customers) {
		for (Customer customer : customers) {
			this.getCustomers().add(customer);
		}
		return this;
	}
	
	// description
	
	public String getDescription() {
		List<String> owners = new ArrayList<>();
		for (Customer c : customers) {
			owners.add(String.format("%s %s", c.getFirstName(), c.getLastName()));
		}
		return String.format("%s (%s)", id, String.join(",", owners));
	}
	
	// get/setters
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		firePropertyChange(ID_CREATION_DATE, this.creationDate, this.creationDate = creationDate);
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		firePropertyChange(ID_BALANCE, this.balance, this.balance = balance);
	}
	
	@XmlList
	@XmlIDREF
	public List<Customer> getCustomers() {
		return customers;
	}
	
	public void setCustomers(List<Customer> customers) {
		firePropertyChange(ID_CUSTOMERS, this.customers, this.customers = customers);
	}
	
	@XmlList
	@XmlIDREF
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		firePropertyChange(ID_TRANSACTIONS, this.transactions, this.transactions = transactions);
	}

}
