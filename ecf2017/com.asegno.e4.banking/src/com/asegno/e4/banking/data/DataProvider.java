package com.asegno.e4.banking.data;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.asegno.e4.banking.model.Account;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.model.Charge;
import com.asegno.e4.banking.model.Customer;
import com.asegno.e4.banking.model.Deposit;
import com.asegno.e4.banking.model.Transaction;
import com.asegno.e4.banking.model.Transfer;
import com.asegno.e4.banking.model.Withdrawal;

public class DataProvider {
	
	private static final String SELECT_BANK = "SELECT b FROM Bank b WHERE b.id=:bankId";
	private static final String PERSISTENCE_UNIT_NAME = "banks";
    private static EntityManagerFactory FACTORY;
    private static DataProvider INSTANCE = new DataProvider();
    private EntityManager entityManager;
	
	private DataProvider() {
		try {			
			FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			entityManager = FACTORY.createEntityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DataProvider instance() {
		return INSTANCE;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public static String getPersistenceURL() {
		return (String) instance().entityManager.getProperties().get("javax.persistence.jdbc.url");
	}

	public Bank getBank() {
		return new Bank();
	}
	
	public static Bank getMockBank() {
		Bank bank = new Bank();
		//
		bank.setName("Demo Bank");
		// create customers
		Customer c1 = new Customer("Tom", "Jones", "Garden Street 8", "tjones@test.com", "1998-04-06");
		Customer c2 = new Customer("Diana", "Jones", "Garden Street 8", "djones@test.com", "1999-11-12");
		Customer c3 = new Customer("Mark", "Reuters", "Maple Street 122", "mr@dot.com", "1958-03-18");
		Customer c4 = new Customer("Spencer", "White", "Avenue Pontida 1", "swhite@site.com", "2000-01-05");
		Customer c5 = new Customer("Alex", "Michaelson", "Red Square 14b", "alex@oxygen.com", "1982-05-06");
		Customer c6 = new Customer("Francois", "Berger", "Frederickstrasse 87", "fberger@main.com", "1998-04-06");
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
	
	/** Persist the data object to XML file 
	 * @throws JAXBException */
	public static void persistXml(File xmlFile, Bank bank) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Bank.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(bank, xmlFile);
	}
	
	/** Load the data object from XML file */
	public static Bank loadXml(File xmlFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Bank.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Bank bank = (Bank) unmarshaller.unmarshal(xmlFile);
		return bank;
	}
	
	/** Persist the data object to DB */
	public static void persistDB(Bank bank){
		EntityManager em = instance().getEntityManager();
		em.getTransaction().begin();
		em.merge(bank);
		em.flush();
		em.getTransaction().commit();
	}
	
	/** Load the data object from DB */
	public static Bank loadDB() throws JAXBException {
		EntityManager em = instance().getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Bank> query = em.createQuery(SELECT_BANK, Bank.class);
		query.setParameter("bankId", "00001");
		Bank bank = query.getSingleResult();
		em.getTransaction().commit();
		return bank;
	}

}
