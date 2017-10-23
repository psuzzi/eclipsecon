package com.itemis.e4.banking.test;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.itemis.e4.banking.Utils;
import com.itemis.e4.banking.data.DataProvider;
import com.itemis.e4.banking.data.XmlDataProvider;
import com.itemis.e4.banking.model.Bank;

public class TestXml {

	Bank bank;
	Bank bank2;
	
	@Test
	public void testPersistenceXML() throws IOException, JAXBException {
		
		// load mock data and persist to XML
		bank = DataProvider.getMockBank();
		File xmlFile = File.createTempFile("mockBank", ".xml");
		XmlDataProvider.persistXml(xmlFile, bank);

		// load persisted XML and compare
		bank2 = XmlDataProvider.loadXml(xmlFile);
		DataProvider.compareBanksData(bank, bank2);
		Utils.log("Check XML I/O completed");
	}

}
