package com.asegno.e4.banking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@DiscriminatorValue("D")
@XmlRootElement
public class Deposit extends Transaction {
	
	@Override
	public String getType() {
		return "Deposit";
	}

	@Override
	public synchronized boolean performProcessing() {
		sourceAccount.setBalance(sourceAccount.getBalance()+amount);
		return true;
	}

	@Override
	public double getSourceAccountDeltaAmount() {
		return amount;
	}

}
