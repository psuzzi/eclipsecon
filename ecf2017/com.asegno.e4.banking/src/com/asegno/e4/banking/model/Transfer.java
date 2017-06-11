package com.asegno.e4.banking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@DiscriminatorValue("T")
@XmlRootElement
public class Transfer extends Transaction {
	
	@Override
	public String getType() {
		return "Transfer";
	}
	
	@Override
	public String getFullDescription() {
		return String.format("%s %s (%s->%s)", getType(), amount, getSourceAccount().getId(), getRecipientAccount().getId());
	}
	
	@Override
	public boolean isHasRecipientAccount() {
		return true;
	}

	@Override
	public synchronized boolean performProcessing() {
		sourceAccount.setBalance(sourceAccount.getBalance()-amount);
		recipientAccount.setBalance(recipientAccount.getBalance()+amount);
		return true;
	}
	
	@Override
	public double getSourceAccountDeltaAmount() {
		return -amount;
	}

}
