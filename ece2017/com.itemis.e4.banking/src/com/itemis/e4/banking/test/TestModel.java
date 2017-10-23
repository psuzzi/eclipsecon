package com.itemis.e4.banking.test;

import org.junit.Test;

import com.itemis.e4.banking.data.DataProvider;
import com.itemis.e4.banking.model.Bank;

public class TestModel {

	Bank bank;
	
	@Test
	public void testDataModel() {
		bank = DataProvider.getMockBank();
		// test mock data
		DataProvider.checkMockBankData(bank);
	}
	
	@Test
	public void testObjectContainment() {
		bank = DataProvider.getMockBank();
		// test containment
		DataProvider.checkMockBankContainment(bank);
	}

}
