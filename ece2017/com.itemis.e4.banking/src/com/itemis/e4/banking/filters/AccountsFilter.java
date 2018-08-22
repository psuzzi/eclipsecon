package com.itemis.e4.banking.filters;

import org.eclipse.jface.viewers.Viewer;

import com.itemis.e4.banking.model.Account;
import com.itemis.e4.banking.model.Customer;

public class AccountsFilter extends CustomersFilter{
	
	protected boolean filterAccountId;
	
	public void setAccountFilter(boolean filterAccountIdNumber) {
		this.filterAccountId = filterAccountIdNumber;
	}
	
	@Override
	public void resetFilter() {
		super.resetFilter();
		setAccountFilter(true);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(filterText == null || filterText.length()==0) {
			return true;
		}
		return filterAccount((Account) element);
	}
	
	protected boolean filterAccount(Account a) {
		boolean select = filterAccountId && (""+a.getId()).matches(filterRegex());
		for(Customer c : a.getCustomers()) {
			select = select || filterCustomer(c);
		}
		return select;
	}
	
	public boolean isFilterAccountId() {
		return filterAccountId;
	}
	
	public void setFilterAccountId(boolean filterAccountIdNumber) {
		this.filterAccountId = filterAccountIdNumber;
	}

}
