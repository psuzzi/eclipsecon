package com.asegno.e4.banking.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="T_BANK")
@XmlRootElement(name="Bank")
public class Bank extends BaseModel{
	
	private String name;
	
	public Bank() {
		this.id = "00001";
	}
	
	private List<Customer> customers = new ArrayList<>();
	
	private List<Account> accounts = new ArrayList<>();
	
	private List<Transaction> transactions = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", name, this.name = name);
	}

	@OneToMany
	@JoinColumn(name="BANK_ID")
	@XmlElement(name="customers")
	public List<Customer> getCustomers() {
		return customers;
	}
	
	public void setCustomers(List<Customer> customers) {
		firePropertyChange("customers", this.customers, this.customers = customers);
	}

	@OneToMany
	@JoinColumn(name="BANK_ID")
	@XmlElement(name="accounts")
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		firePropertyChange("accounts", this.accounts, this.accounts = accounts);
	}

	@OneToMany
	@JoinColumn(name="BANK_ID")
	@XmlElement(name="transactions")
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		firePropertyChange("transactions", this.transactions, this.transactions = transactions);
	}

	// Add Objects and link them together 
	
	public void addCustomer(Customer c) {
		customers.add(c);
		firePropertyChange("customers", null, c);
	}
	
	public void addCustomers(Customer ... cs){
		for(Customer c: cs) {
			addCustomer(c);
		}
	}
	
	/** Add account and update customer's accounts */
	public void addAccount(Account a) {
		accounts.add(a);
		for(Customer c: a.getCustomers()) {
			c.getAccounts().add(a); 
		}
		firePropertyChange("accounts", null, a);
	}
	
	public void addAccounts(Account ... as) {
		for(Account a: as) {
			addAccount(a);
		}
	}
	
	/** Add transaction and update account's transactions */
	public void addTransaction(Transaction t) {
		transactions.add(t);
		t.getSourceAccount().getTransactions().add(t);
		if(t.isHasRecipientAccount()) {
			t.getRecipientAccount().getTransactions().add(t);
		}
		firePropertyChange("transactions", null, t);
	}
	
	public void addTransactions(Transaction ... ts) {
		for(Transaction t: ts) {
			addTransaction(t);
		}
	}

}
