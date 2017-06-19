
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Account;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.model.Customer;
import com.asegno.e4.banking.model.Transaction;

public class CustomerDetailPart extends BasePart<Customer>{
	
	private DataBindingContext m_bindingContext;
	
	private Text textFirstName;
	private Text textLastName;
	private Text textBirthDate;
	private Text textAddress;
	private Text textEmail;
	private Table tableAccounts;
	private Table tableTransactions;
	private TableViewer tableViewer;
	private TableViewer tableViewer_1;
	
	@Override
	public void setModel(Customer model) {
		if(model==null)
			return;
		disposeBindings(m_bindingContext);
		super.setModel(model);
		if(tableViewer==null)
			return;
		m_bindingContext = initDataBindings();
		update();
	}
	
	public Customer getModel() {
		return model;
	}
	
	/** Called by injection when a Customer is set in the active selection */
	@Inject
	public void selectionChanged(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Customer customer) {
		if(customer==null)
			return;
		setModel(customer);
	}
	
	/** called by the E4 framework when an event is posted with the given topic and a Bank object */
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Bank model) {
		setModel(new Customer());
	}
	
	/** Called by injection when a message with the given topic is called */
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Customer model) {
		update();
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Label lblCustomerInformation = new Label(parent, SWT.NONE);
		lblCustomerInformation.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblCustomerInformation.setText("Customer Information");
		
		Group grpGeneral = new Group(parent, SWT.NONE);
		grpGeneral.setLayout(new GridLayout(2, false));
		grpGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpGeneral.setText("General");
		
		Label lblFirstName = new Label(grpGeneral, SWT.NONE);
		GridData gd_lblFirstName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblFirstName.widthHint = 100;
		lblFirstName.setLayoutData(gd_lblFirstName);
		lblFirstName.setText("First name");
		
		textFirstName = new Text(grpGeneral, SWT.BORDER);
		textFirstName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLastName = new Label(grpGeneral, SWT.NONE);
		lblLastName.setText("Last name");
		
		textLastName = new Text(grpGeneral, SWT.BORDER);
		textLastName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBirthDate = new Label(grpGeneral, SWT.NONE);
		lblBirthDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBirthDate.setText("Birth date");
		
		textBirthDate = new Text(grpGeneral, SWT.BORDER);
		textBirthDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpContact = new Group(parent, SWT.NONE);
		grpContact.setLayout(new GridLayout(2, false));
		grpContact.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpContact.setText("Contact");
		
		Label lblAddress = new Label(grpContact, SWT.NONE);
		GridData gd_lblAddress = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblAddress.widthHint = 100;
		lblAddress.setLayoutData(gd_lblAddress);
		lblAddress.setText("Address");
		
		textAddress = new Text(grpContact, SWT.BORDER);
		textAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblEmail = new Label(grpContact, SWT.NONE);
		lblEmail.setText("Email");
		
		textEmail = new Text(grpContact, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpAccounts = new Group(parent, SWT.NONE);
		grpAccounts.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpAccounts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		grpAccounts.setText("Accounts");
		
		tableViewer = new TableViewer(grpAccounts, SWT.BORDER | SWT.FULL_SELECTION);
		tableAccounts = tableViewer.getTable();
		tableAccounts.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnAccountId = tableViewerColumn.getColumn();
		tblclmnAccountId.setWidth(100);
		tblclmnAccountId.setText("Account ID");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnBalance = tableViewerColumn_1.getColumn();
		tblclmnBalance.setWidth(100);
		tblclmnBalance.setText("Balance");
		
		Group grpTransactions = new Group(parent, SWT.NONE);
		grpTransactions.setLayout(new FillLayout(SWT.HORIZONTAL));
		grpTransactions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		grpTransactions.setText("Transactions");
		
		tableViewer_1 = new TableViewer(grpTransactions, SWT.BORDER | SWT.FULL_SELECTION);
		tableTransactions = tableViewer_1.getTable();
		tableTransactions.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn_2.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText("Description");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnSourceAccount = tableViewerColumn_3.getColumn();
		tblclmnSourceAccount.setWidth(100);
		tblclmnSourceAccount.setText("Source account");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnProcessed = tableViewerColumn_4.getColumn();
		tblclmnProcessed.setWidth(100);
		tblclmnProcessed.setText("Processed");
		
		update();
		
		m_bindingContext = initDataBindings();
		
	}
	
	private void update() {
		tableViewer.refresh();
		tableViewer_1.refresh();
		resizeColumns(tableAccounts);
		resizeColumns(tableTransactions);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextFirstNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(textFirstName);
		IObservableValue firstNameGetModelObserveValue = BeanProperties.value("firstName").observe(getModel());
		bindingContext.bindValue(observeTextTextFirstNameObserveWidget, firstNameGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextLastNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(textLastName);
		IObservableValue lastNameGetModelObserveValue = BeanProperties.value("lastName").observe(getModel());
		bindingContext.bindValue(observeTextTextLastNameObserveWidget, lastNameGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextBirthDateObserveWidget = WidgetProperties.text(SWT.Modify).observe(textBirthDate);
		IObservableValue birthDateStringGetModelObserveValue = BeanProperties.value("birthDateString").observe(getModel());
		bindingContext.bindValue(observeTextTextBirthDateObserveWidget, birthDateStringGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextAddressObserveWidget = WidgetProperties.text(SWT.Modify).observe(textAddress);
		IObservableValue addressGetModelObserveValue = BeanProperties.value("address").observe(getModel());
		bindingContext.bindValue(observeTextTextAddressObserveWidget, addressGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextEmailObserveWidget = WidgetProperties.text(SWT.Modify).observe(textEmail);
		IObservableValue emailGetModelObserveValue = BeanProperties.value("email").observe(getModel());
		bindingContext.bindValue(observeTextTextEmailObserveWidget, emailGetModelObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), Account.class, new String[]{"id", "balance"});
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tableViewer.setContentProvider(listContentProvider);
		//
		IObservableList accountsGetModelObserveList = BeanProperties.list("accounts").observe(getModel());
		tableViewer.setInput(accountsGetModelObserveList);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
		IObservableMap[] observeMaps_1 = BeansObservables.observeMaps(listContentProvider_1.getKnownElements(), Transaction.class, new String[]{"description", "sourceAccountDescription", "processed"});
		tableViewer_1.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
		tableViewer_1.setContentProvider(listContentProvider_1);
		//
		IObservableList transactionsGetModelObserveList = BeanProperties.list("transactions").observe(getModel());
		tableViewer_1.setInput(transactionsGetModelObserveList);
		//
		return bindingContext;
	}
}