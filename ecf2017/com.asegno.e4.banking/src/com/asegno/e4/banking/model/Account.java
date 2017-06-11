package com.asegno.e4.banking.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

import com.asegno.e4.banking.data.Utils;

@Entity
@Table(name="T_ACCOUNT")
@XmlRootElement
public class Account extends BaseModel {

	private Date creationDate;
	private double balance = 0.0d;

	private List<Customer> customers = new ArrayList<>();

	private List<Transaction> transactions = new ArrayList<>();
	
	public Account() {
		super();
		this.creationDate = Utils.stringToDate("2016-02-01");
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		firePropertyChange("balance", this.balance, this.balance = balance);
	}

	@XmlList
	@XmlIDREF
	public List<Customer> getCustomers() {
		return customers;
	}
	
	public void setCustomers(List<Customer> customers) {
		firePropertyChange("customers", this.customers, this.customers = customers);
	}

	@XmlList
	@XmlIDREF
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		firePropertyChange("transactions", this.transactions, this.transactions = transactions);
	}

	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		firePropertyChange("creationDate", this.creationDate, this.creationDate = creationDate);
	}
	
	public String getAccountDescription() {
		List<String> owners = new ArrayList<>();
		for (Customer c : customers) {
			owners.add(String.format("%s %s", c.getFirstName(), c.getLastName()));
		}
		return String.format("%s (%s)", id, String.join(",", owners));
	}

	public Account link(Customer... customers) {
		for (Customer customer : customers) {
			this.getCustomers().add(customer);
		}
		return this;
	}

	
	
}
