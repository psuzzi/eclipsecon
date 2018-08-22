package com.itemis.e4.banking.model;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.itemis.e4.banking.Utils;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="T_TRANSACTION")
@DiscriminatorColumn(name="T_TYPE")
@XmlSeeAlso({Deposit.class, Charge.class, Withdrawal.class, Transfer.class})
@XmlRootElement
public class Transaction extends BaseModel{
	
	static final String ID_AMOUNT = "amount";
	static final String ID_SOURCE_ACCOUNT = "sourceAccount";
	static final String ID_RECIPIENT_ACCOUNT = "recipientAccount";
	static final String ID_CONFIRMED_DATE = "confirmedDate";
	static final String ID_PROCESSED_DATE = "processedDate";
	static final String ID_CONFIRMED = "confirmed";
	static final String ID_PROCESSED = "processed";
	
	protected double amount = 0.0d;
	protected Account sourceAccount = null;
	protected Account recipientAccount = null;
	protected Date confirmedDate = null;
	protected Date processedDate = null;
	protected boolean confirmed = false;
	protected boolean processed = false;
	
	public Transaction() {
		super();
	}
	
	// creation
	
	public Transaction create(long amount, Account sourceAccount) {
		return create(amount, sourceAccount, null);
	}
	
	public Transaction create(long amount, Account sourceAccount, Account recipientAccount) {
		setAmount(amount);
		setSourceAccount(sourceAccount);
		setRecipientAccount(recipientAccount);
		return this;
	}
	
	// subclass data
	
	public String getType() {return "";}
	
	// true for transactions between accounts
	public boolean isHasRecipientAccount() { return false; }
	
	public boolean performProcessing() {return false;}
	
	public double getSourceAccountDeltaAmount() {return amount;}

	public double getRecipientAccountDeltaAmount() {
		return -getSourceAccountDeltaAmount();
	}
	
	// description
	
	public String getDescription() {
		return String.format("%s %s", getType(), amount);
	}
	
	public String getFullDescription() {
		return String.format("%s %s (%s)", getType(), amount, getSourceAccount().getId());
	}
	
	// get/setters
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		firePropertyChange(ID_AMOUNT, this.amount, this.amount = amount);
	}

	@XmlIDREF
	public Account getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(Account sourceAccount) {
		firePropertyChange(ID_SOURCE_ACCOUNT, this.sourceAccount, this.sourceAccount = sourceAccount);
	}

	@XmlIDREF
	public Account getRecipientAccount() {
		return recipientAccount;
	}

	public void setRecipientAccount(Account recipientAccount) {
		firePropertyChange(ID_RECIPIENT_ACCOUNT, this.recipientAccount, this.recipientAccount = recipientAccount);
	}

	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		firePropertyChange(ID_CONFIRMED_DATE, this.confirmedDate, this.confirmedDate = confirmedDate);
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		firePropertyChange(ID_PROCESSED_DATE, this.processedDate, this.processedDate = processedDate);
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		firePropertyChange(ID_CONFIRMED, this.confirmed, this.confirmed = confirmed);
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		firePropertyChange(ID_PROCESSED, this.processed, this.processed = processed);
	}
	
	// Additional methods needed in datamodel test and UI databinding
	
	public String getSourceAccountId() {
		if(getSourceAccount()==null)
			return "";
		return getSourceAccount().getId();
	}
	
	public String getSourceAccountDescription() {
		if(getSourceAccount()==null)
			return "";
		return getSourceAccount().getDescription();
	}
	
	public String getRecipientAccountId() {
		if(getRecipientAccount()==null)
			return "";
		return getRecipientAccount().getId();
	}
	
	public String getRecipientAccountDescription() {
		if(getRecipientAccount()==null)
			return "";
		return getRecipientAccount().getDescription();
	}
	
	// additional get/setters for UI databinding
	
	public boolean isNotConfirmed() {
		return !confirmed;
	}
	
	public boolean isNotProcessed() {
		return !processed;
	}
	
	@XmlTransient
	public String getConfirmedDateString() {
		if(confirmedDate==null) {
			return "";
		}
		return Utils.dateToString(confirmedDate);
	}
	
	@XmlTransient
	public String getProcessedDateString() {
		if(processedDate==null) {
			return "";
		}
		return(Utils.dateToString(getProcessedDate()));
	}
	
	// operations
	
	public Transaction confirm() {
		return confirm(new Date());
	}
	
	public Transaction confirm(String dateString) {
		return confirm(Utils.stringToDate(dateString));
	}
	
	public Transaction confirm(Date confirmedDate) {
		if(confirmedDate==null) {
			setConfirmedDate(new Date());
		} else {
			setConfirmedDate(confirmedDate);
		}
		setConfirmed(true);
		return this;
	}
	
	public Transaction process() {
		return process(getConfirmedDate());
	}
	
	public Transaction process(String dateString) {
		return process(Utils.stringToDate(dateString));
	}

	public Transaction process(Date processedDate) {
		if(performProcessing()) {			
			if(processedDate==null) {
				setProcessedDate(new Date());
			} else {
				setProcessedDate(processedDate);
			}
			setProcessed(true);
		}
		return this;
	}
	

}
