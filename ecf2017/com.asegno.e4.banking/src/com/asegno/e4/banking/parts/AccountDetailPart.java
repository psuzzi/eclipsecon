
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Composite;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Account;
import com.asegno.e4.banking.model.Bank;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.beans.BeansObservables;
import com.asegno.e4.banking.model.Customer;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.core.databinding.observable.list.IObservableList;
import com.asegno.e4.banking.model.Transaction;

public class AccountDetailPart extends BasePart<Account>{
	private DataBindingContext m_bindingContext;
	private Text textAccountID;
	private Text textBalance;
	private Table table;
	private Table table_1;
	private TableViewer tableViewer;
	private TableViewer tableViewer_1;
	private Canvas canvas;
	
	
	@Override
	public void setModel(Account model) {
		if(model==null)
			return;
		disposeBindings(m_bindingContext);
		super.setModel(model);
		if(table==null)
			return;
		m_bindingContext = initDataBindings();
		update();
	}
	
	public Account getModel() {
		return model;
	}
	
	/** Called by injection when an Account is set in the active selection */
	@Inject
	public void selectionChanged(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Account account) {
		if(account==null)
			return;
		setModel(account);
	}
	
	/** called by the E4 framework when an event is posted with the given topic and object */
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Bank model) {
		setModel(new Account());
	}
	
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Account account) {
		update();
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Label lblAccountDetail = new Label(composite, SWT.NONE);
		lblAccountDetail.setText("Account Detail");
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAccountId = new Label(composite_1, SWT.NONE);
		lblAccountId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAccountId.setText("Account ID: ");
		
		textAccountID = new Text(composite_1, SWT.BORDER);
		textAccountID.setEditable(false);
		textAccountID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBalance = new Label(composite_1, SWT.NONE);
		lblBalance.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBalance.setText("balance");
		
		textBalance = new Text(composite_1, SWT.BORDER);
		textBalance.setEditable(false);
		textBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		canvas = new Canvas(composite, SWT.NONE);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		Group grpCustomers = new Group(composite, SWT.NONE);
		grpCustomers.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpCustomers = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpCustomers.heightHint = 80;
		grpCustomers.setLayoutData(gd_grpCustomers);
		grpCustomers.setText("Customers");
		
		tableViewer = new TableViewer(grpCustomers, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText("Description");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn_1.getColumn();
		tblclmnId.setWidth(100);
		tblclmnId.setText("ID");
		
		Group grpTransactions = new Group(composite, SWT.NONE);
		grpTransactions.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpTransactions = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpTransactions.heightHint = 120;
		grpTransactions.setLayoutData(gd_grpTransactions);
		grpTransactions.setText("Transactions");
		
		tableViewer_1 = new TableViewer(grpTransactions, SWT.BORDER | SWT.FULL_SELECTION);
		table_1 = tableViewer_1.getTable();
		table_1.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnDescription_1 = tableViewerColumn_2.getColumn();
		tblclmnDescription_1.setWidth(100);
		tblclmnDescription_1.setText("Description");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnSourceAccount = tableViewerColumn_3.getColumn();
		tblclmnSourceAccount.setWidth(100);
		tblclmnSourceAccount.setText("Source Account");
		
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
		resizeColumns(table);
		resizeColumns(table_1);
		LightweightSystem lws = new LightweightSystem(canvas);
		ChartHelper.generateGraph(lws, getModel());
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextAccountIDObserveWidget = WidgetProperties.text(SWT.Modify).observe(textAccountID);
		IObservableValue idGetModelObserveValue = BeanProperties.value("id").observe(getModel());
		bindingContext.bindValue(observeTextTextAccountIDObserveWidget, idGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextBalanceObserveWidget = WidgetProperties.text(SWT.Modify).observe(textBalance);
		IObservableValue balanceGetModelObserveValue = BeanProperties.value("balance").observe(getModel());
		bindingContext.bindValue(observeTextTextBalanceObserveWidget, balanceGetModelObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), Customer.class, new String[]{"description", "id"});
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tableViewer.setContentProvider(listContentProvider);
		//
		IObservableList customersGetModelObserveList = BeanProperties.list("customers").observe(getModel());
		tableViewer.setInput(customersGetModelObserveList);
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