package com.itemis.e4.banking.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Bank extends BaseModel{
	
	static final String ID_TRANSACTIONS = "transactions";
	static final String ID_ACCOUNTS = "accounts";
	static final String ID_CUSTOMERS = "customers";
	
	private List<Customer> customers = new ArrayList<>();
	private List<Account> accounts = new ArrayList<>();
	private List<Transaction> transactions = new ArrayList<>();
	
	public Bank() {
		this.id = "00001";
	}
	
	// get/setters
	
	@OneToMany
	@JoinColumn(name="BANK_ID")
	@XmlElement
	public List<Customer> getCustomers() {
		return customers;
	}
	
	public void setCustomers(List<Customer> customers) {
		firePropertyChange(ID_CUSTOMERS, this.customers, this.customers = customers);
	}
	
	@OneToMany
	@JoinColumn(name="BANK_ID")
	@XmlElement
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		firePropertyChange(ID_ACCOUNTS, this.accounts, this.accounts = accounts);
	}
	
	@OneToMany
	@JoinColumn(name="BANK_ID")
	@XmlElement
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		firePropertyChange(ID_TRANSACTIONS, this.transactions, this.transactions = transactions);
	}
	
	// add objects and link them together 
	
	/** Add bank customer. Fires property change */
	public void addCustomer(Customer c) {
		customers.add(c);
		firePropertyChange(ID_CUSTOMERS, null, c);
	}
	
	public void addCustomers(Customer ... cs){
		for(Customer c: cs) {
			addCustomer(c);
		}
	}
	
	/** Add account and update customer's accounts. Fires property change */
	public void addAccount(Account a) {
		accounts.add(a);
		for(Customer c: a.getCustomers()) {
			c.getAccounts().add(a); 
		}
		firePropertyChange(ID_ACCOUNTS, null, a);
	}
	
	public void addAccounts(Account ... as) {
		for(Account a: as) {
			addAccount(a);
		}
	}
	
	/** Add transaction and update account's transactions. Fires property change */
	public void addTransaction(Transaction t) {
		transactions.add(t);
		t.getSourceAccount().getTransactions().add(t);
		if(t.isHasRecipientAccount()) {
			t.getRecipientAccount().getTransactions().add(t);
		}
		firePropertyChange(ID_TRANSACTIONS, null, t);
	}
	
	public void addTransactions(Transaction ... ts) {
		for(Transaction t: ts) {
			addTransaction(t);
		}
	}

}
