package com.itemis.e4.banking.data;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.itemis.e4.banking.Utils;
import com.itemis.e4.banking.model.Bank;

public class XmlDataProvider{
	
	/** Persist the data object to XML file 
	 * @throws JAXBException */
	public static void persistXml(File xmlFile, Bank bank) throws JAXBException {
		Utils.log("Persist XML to:" + xmlFile);
		JAXBContext jaxbContext = JAXBContext.newInstance(Bank.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(bank, xmlFile);
	}
	
	/** Load the data object from XML file */
	public static Bank loadXml(File xmlFile) throws JAXBException {
		Utils.log("Load XML from:" + xmlFile);
		JAXBContext jaxbContext = JAXBContext.newInstance(Bank.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Bank bank = (Bank) unmarshaller.unmarshal(xmlFile);
		return bank;
	}

}
