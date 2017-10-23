package com.itemis.e4.banking.data;

import static com.itemis.e4.banking.Utils.log;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import com.itemis.e4.banking.model.Account;
import com.itemis.e4.banking.model.Bank;
import com.itemis.e4.banking.model.Charge;
import com.itemis.e4.banking.model.Customer;
import com.itemis.e4.banking.model.Deposit;
import com.itemis.e4.banking.model.Transaction;
import com.itemis.e4.banking.model.Transfer;
import com.itemis.e4.banking.model.Withdrawal;

public class DataProvider {
	
	public static Bank getMockBank() {
		Bank bank = new Bank();
		// create customers
		Customer c1 = new Customer("Tom", "Jones", "Garden Street 8");
		Customer c2 = new Customer("Diana", "Jones", "Garden Street 8");
		Customer c3 = new Customer("Mark", "Reuters", "Maple Street 122");
		Customer c4 = new Customer("Spencer", "White", "Avenue Pontida 1");
		Customer c5 = new Customer("Alex", "Michaelson", "Red Square 14b");
		Customer c6 = new Customer("Francois", "Berger", "Frederickstrasse 87");
		bank.addCustomers(c1,c2,c3,c4,c5,c6);
		// add accounts and link to customers
		Account a1 = new Account().link(c1);
		Account a2 = new Account().link(c1, c2);
		Account a3 = new Account().link(c3);
		Account a4 = new Account().link(c4);
		Account a5 = new Account().link(c5);
		Account a6 = new Account().link(c6);
		Account a7 = new Account().link(c6);
		bank.addAccounts(a1,a2,a3,a4,a5,a6,a7);
		// add transactions
		Transaction t1 = new Deposit().create(5000, a1).confirm("2016-02-20").process();
		Transaction t2 = new Charge().create(250, a1).confirm("2016-03-10").process();
		Transaction t3 = new Transfer().create(1000, a1, a2).confirm("2016-04-05").process();
		Transaction t4 = new Deposit().create(10000, a3).confirm("2016-04-06").process();
		Transaction t5 = new Deposit().create(5000, a3).confirm("2016-04-10").process();
		Transaction t6 = new Deposit().create(5000, a3).confirm("2016-06-21").process();
		Transaction t7 = new Deposit().create(10000, a3).confirm("2016-06-23").process();
		Transaction t8 = new Withdrawal().create(2500, a3).confirm("2016-07-01").process();
		Transaction t9 = new Charge().create(1500, a3).confirm("2016-07-03").process();
		Transaction t10 = new Transfer().create(1000, a3, a2).confirm("2016-07-05").process();
		bank.addTransactions(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10);
		//
		return bank;
	}
	
	public static void checkMockBankData(Bank mockBank) {
		log("Check mockBank data");
		assertEquals("Nr. of Customers", 6, mockBank.getCustomers().size());
		assertEquals("Nr. of Accounts", 7, mockBank.getAccounts().size());
		assertEquals("Nr. of Transactions", 10, mockBank.getTransactions().size());
	}
	
	public static void checkMockBankContainment(Bank mockBank) {
		log("Check mockBank containment");
		// test containment
		Customer c1 = mockBank.getCustomers().get(0);
		Account a1 = mockBank.getAccounts().get(0);
		Transaction t1 = mockBank.getTransactions().get(0);
		//
		assertThat("Account's customer", a1.getCustomers().contains(c1)); 
		assertThat("Customer's account", c1.getAccounts().contains(a1));
		assertThat("Transaction's account", t1.getSourceAccount().equals(a1));
	}
	
	public static void compareBanksData(Bank bank, Bank bank2) {
		log("Compare Bank data");
		compareCustomersData(bank.getCustomers(), bank2.getCustomers());
		compareAccountsData(bank.getAccounts(), bank2.getAccounts());
		compareTransactionsData(bank.getTransactions(), bank2.getTransactions());
	}

	public static void compareCustomersData(List<Customer> lc1, List<Customer> lc2) {
		log("Compare Customers data");
		assertEquals("Same customers size", lc1.size(), lc2.size());
		for(int i=0; i<lc1.size(); i++) {
			Customer c1 = lc1.get(i);
			Customer c2 = lc2.get(i);
			assertEquals("Same customer accounts", c1.getAccounts().size() , c2.getAccounts().size());
		}
	}

	public static void compareAccountsData(List<Account> la1, List<Account> la2) {
		log("Compare Accounts data");
		assertEquals("Same accounts size", la1.size(), la2.size());
		for(int i=0; i< la1.size(); i++) {
			Account a1 = la1.get(i);
			Account a2 = la2.get(i);
			assertEquals("Same account amount ", a1.getBalance(), a2.getBalance(), 0.001);
			assertEquals("Same account transactions", a1.getTransactions().size(), a2.getTransactions().size());
			assertEquals("Same account customers", a1.getCustomers().size(), a2.getCustomers().size());
		}
	}

	public static void compareTransactionsData(List<Transaction> t1, List<Transaction> t2) {
		log("Compare Transactions data");
		assertEquals("Same transactions size", t1.size(), t2.size());
		for(int i=0; i<t1.size(); i++) {
			assertEquals("Same source account", t1.get(i).getSourceAccountId(), t2.get(i).getSourceAccountId());
			assertEquals("Same recipient account", t1.get(i).getRecipientAccountId(), t2.get(i).getRecipientAccountId());
		}
	}
	
}
