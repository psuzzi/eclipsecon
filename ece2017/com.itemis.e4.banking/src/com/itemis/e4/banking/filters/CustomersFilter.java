package com.itemis.e4.banking.filters;

import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.itemis.e4.banking.model.Customer;

public class CustomersFilter extends ViewerFilter {

	protected String filterText;
	protected boolean filterName;
	protected boolean filterAddress;
	protected boolean filterCustomerId;

	public void setCustomersFilter(boolean filterName, boolean filterAddress,
			boolean filterCustomerIdNumber) {
		this.filterName = filterName;
		this.filterAddress = filterAddress;
		this.filterCustomerId = filterCustomerIdNumber;
	}
	
	public void resetFilter() {
		setFilterText("");
		setCustomersFilter(true, true, false);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (filterText == null || filterText.length() == 0) {
			return true;
		}
		return filterCustomer((Customer) element);
	}
	
	public String filterRegex(){
		return "(?i:.*" + Pattern.quote(filterText) + ".*)";
	}
	
	protected boolean filterCustomer(Customer c) {
		return (
				(filterName && 
					( c.getFirstName().matches(filterRegex()) || 
					  c.getLastName().matches(filterRegex()) ) )
				|| (filterAddress && c.getAddress().matches(filterRegex()))
				|| (filterCustomerId && ("" + c.getId()).matches(filterRegex())));
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}
	
	public boolean isFilterName() {
		return filterName;
	}
	
	public void setFilterName(boolean filterName) {
		this.filterName = filterName;
	}

	public boolean isFilterAddress() {
		return filterAddress;
	}

	public void setFilterAddress(boolean filterAddress) {
		this.filterAddress = filterAddress;
	}

	public boolean isFilterCustomerId() {
		return filterCustomerId;
	}

	public void setFilterCustomerId(boolean filterCustomerIdNumber) {
		this.filterCustomerId = filterCustomerIdNumber;
	}

}
