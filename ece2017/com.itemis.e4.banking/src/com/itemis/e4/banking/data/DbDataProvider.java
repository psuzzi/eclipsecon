package com.itemis.e4.banking.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.itemis.e4.banking.model.Bank;

public class DbDataProvider{
	
	private static final String SELECT_BANK = "SELECT b FROM Bank b WHERE b.id=:bankId";
	private static final String PERSISTENCE_UNIT_NAME = "banks";
    private static EntityManagerFactory FACTORY;
    private static DbDataProvider INSTANCE = new DbDataProvider();
    private EntityManager entityManager;
	
	private DbDataProvider() {
		try {			
			FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			entityManager = FACTORY.createEntityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DbDataProvider instance() {
		return INSTANCE;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public static String getPersistenceURL() {
		return (String) instance().entityManager.getProperties().get("javax.persistence.jdbc.url");
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
	public static Bank loadDB()  {
		EntityManager em = instance().getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Bank> query = em.createQuery(SELECT_BANK, Bank.class);
		query.setParameter("bankId", "00001");
		Bank bank = query.getSingleResult();
		em.getTransaction().commit();
		return bank;
	}

}
