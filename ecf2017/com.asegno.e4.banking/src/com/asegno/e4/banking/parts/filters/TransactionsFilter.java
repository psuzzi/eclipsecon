package com.asegno.e4.banking.parts.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;

import com.asegno.e4.banking.model.Transaction;

public class TransactionsFilter extends AccountsFilter{
	
	private boolean filterTransactionId;
	private boolean filterProcessedTransactions;
	private boolean filterTransactionType;
	private boolean filterTransactionAmount;
	
	public void setTransactionsFilter(boolean filterProcessedTransactions, boolean filterTransactionIdNumber, boolean filterTransactionType, boolean filterTransactionAmount) {
		this.filterProcessedTransactions = filterProcessedTransactions;
		this.filterTransactionId = filterTransactionIdNumber;
		this.filterTransactionType = filterTransactionType;
		this.filterTransactionAmount = filterTransactionAmount;
	}
	
	@Override
	public void resetFilter() {
		super.resetFilter();
		setTransactionsFilter(false, true, true, true);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(filterText == null || filterText.length()==0) {
			return true;
		}
		return filterTransaction((Transaction) element);
	}
	
	protected boolean filterTransaction(Transaction t) {
		// exclude non processed transactions when requested
		if(this.filterProcessedTransactions && !t.isProcessed()) {
			return false;
		}
		boolean select = (filterTransactionId && (""+t.getId()).matches(filterRegex()) ||
				filterTransactionType && (t.getType().matches(filterRegex())) ||
				filterTransactionAmount && (matchAmount(t.getAmount(), getFilterText()))
				);
		select = select || filterAccount(t.getSourceAccount());
		if(t.isHasRecipientAccount()) {
			select = select || filterAccount(t.getRecipientAccount()); 
		}
		return select;
	}

	private Pattern pAmounts = Pattern.compile(".*(\\d+(?:\\.\\d+)?)(:?-(\\d+(?:\\.\\d+)?))?.*");
	
	private boolean matchAmount(double amount, String filterText) {
		Matcher m = pAmounts.matcher(filterText);
		if(m.matches()) {
			System.out.println(String.format("Matches %s: %s", filterText, amount));
			for(int i=0; i<m.groupCount(); i++) {
				System.out.println(String.format("- %s", m.group(i)));
			}
		}
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	public boolean isFilterTransactionId() {
		return filterTransactionId;
	}
	
	public void setFilterTransactionId(boolean filterTransactionIdNumber) {
		this.filterTransactionId = filterTransactionIdNumber;
	}
	
	public boolean isFilterProcessedTransactions() {
		return filterProcessedTransactions;
	}
	
	public void setFilterProcessedTransactions(boolean filterProcessedTransactions) {
		this.filterProcessedTransactions = filterProcessedTransactions;
	}

	public boolean isFilterTransactionType() {
		return filterTransactionType;
	}

	public void setFilterTransactionType(boolean filterTransactionType) {
		this.filterTransactionType = filterTransactionType;
	}

	public boolean isFilterTransactionAmount() {
		return filterTransactionAmount;
	}

	public void setFilterTransactionAmount(boolean filterTransactionAmount) {
		this.filterTransactionAmount = filterTransactionAmount;
	}

}
