package com.itemis.e4.banking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@DiscriminatorValue("C")
@XmlRootElement
public class Charge extends Transaction {
	
	@Override
	public String getType() {
		return "Charge";
	}
	
	@Override
	public synchronized boolean performProcessing() {
		sourceAccount.setBalance(sourceAccount.getBalance()-amount);
		return true;
	}
	
	@Override
	public double getSourceAccountDeltaAmount() {
		return -amount;
	}

}
