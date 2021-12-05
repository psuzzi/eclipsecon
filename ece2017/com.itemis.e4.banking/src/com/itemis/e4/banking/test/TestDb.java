package com.itemis.e4.banking.test;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.itemis.e4.banking.Utils;
import com.itemis.e4.banking.data.DataProvider;
import com.itemis.e4.banking.data.DbDataProvider;
import com.itemis.e4.banking.model.Bank;

public class TestDb {
	
	Bank bank;
	Bank bank2;
	

	@Test
	public void testPersistenceDB() throws JAXBException {
		
		// load mock data and persist to DB
		bank = DataProvider.getMockBank();
		Utils.log("Verify DB persistence with " + DbDataProvider.getPersistenceURL());
		DbDataProvider.persistDB(bank);
		
		// load persisted from DB and compare
		bank2 = DbDataProvider.loadDB();
		DataProvider.compareBanksData(bank, bank2);
		Utils.log("Check DB I/O completed");
	}

}
