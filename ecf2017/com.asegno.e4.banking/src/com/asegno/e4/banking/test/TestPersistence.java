package com.asegno.e4.banking.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.asegno.e4.banking.data.DataProvider;
import com.asegno.e4.banking.model.Account;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.model.Customer;
import com.asegno.e4.banking.model.Transaction;

public class TestPersistence {

	Bank bank;
	Bank bank2;
	
	@Test
	public void testPersistenceXML() throws IOException, JAXBException {
		bank = DataProvider.getMockBank();
		File xmlFile = File.createTempFile("mockBank", ".xml");
		System.out.println("Verify XML persistence on " + xmlFile.getCanonicalPath());
		DataProvider.persistXml(xmlFile, bank);
		bank2 = DataProvider.loadXml(xmlFile);
		checkBankData();
		System.out.println("Check XML I/O completed");
	}
	
	@Test
	public void testPersistenceDB() throws JAXBException {
		bank = DataProvider.getMockBank();
		System.out.println("Verify DB persistence with " + DataProvider.getPersistenceURL());
		DataProvider.persistDB(bank);
		bank2 = DataProvider.loadDB();
		checkBankData();
		System.out.println("Check DB I/O completed");
	}

	private void checkBankData() {
		System.out.println("Check bank data");
		checkCustomers(bank.getCustomers(), bank2.getCustomers());
		checkAccounts(bank.getAccounts(), bank2.getAccounts());
		checkTransactions(bank.getTransactions(), bank2.getTransactions());
	}

	private void checkCustomers(List<Customer> lc1, List<Customer> lc2) {
		assertEquals("Same customers size", lc1.size(), lc2.size());
		for(int i=0; i<lc1.size(); i++) {
			Customer c1 = lc1.get(i);
			Customer c2 = lc2.get(i);
			assertEquals("Same customer accounts", c1.getAccounts().size() , c2.getAccounts().size());
		}
	}

	private void checkAccounts(List<Account> la1, List<Account> la2) {
		assertEquals("Same accounts size", la1.size(), la2.size());
		for(int i=0; i< la1.size(); i++) {
			Account a1 = la1.get(i);
			Account a2 = la2.get(i);
			assertEquals("Same account amount ", a1.getBalance(), a2.getBalance(), 0.001);
			assertEquals("Same account transactions", a1.getTransactions().size(), a2.getTransactions().size());
			assertEquals("Same account customers", a1.getCustomers().size(), a2.getCustomers().size());
		}
	}

	private void checkTransactions(List<Transaction> t1, List<Transaction> t2) {
		assertEquals("Same transactions size", bank.getTransactions().size(), t2.size());
		for(int i=0; i<t1.size(); i++) {
			assertEquals("Same source account", t1.get(i).getSourceAccountId(), t2.get(i).getSourceAccountId());
			assertEquals("Same recipient account", t1.get(i).getRecipientAccountId(), t2.get(i).getRecipientAccountId());
		}
	}
	
}
