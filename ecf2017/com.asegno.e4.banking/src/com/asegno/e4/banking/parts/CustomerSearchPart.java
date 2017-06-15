
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.model.Customer;
import com.asegno.e4.banking.parts.filters.CustomersFilter;
import org.eclipse.wb.swt.SWTResourceManager;

public class CustomerSearchPart extends BasePart<Bank>{
	
	private DataBindingContext m_bindingContext;
	
	private CustomersFilter filter = new CustomersFilter();
	private Text textFilter;
	private ListViewer listViewer;
	private Button btnFirstName;
	private Button btnLastName;
	private Button btnAddress;
	private Button btnCustomerId;
	
	@Inject
	protected ESelectionService selectionService;
	
	@Inject
	public CustomerSearchPart(MApplication app) {
		setModel(app.getContext().get(Bank.class));
		filter.resetFilter();
	}
	
	@Override
	public void setModel(Bank model) {
		if(model==null)
			return;
		disposeBindings(m_bindingContext);
		super.setModel(model);
		if(listViewer==null)
			return;
		m_bindingContext = initDataBindings();
		update();
	}
	
	public Bank getModel() {
		return model;
	}
	
	/** called by the E4 framework when an event is posted with the given topic and a Bank object */
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Bank model) {
		setModel(model);
	}
	
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Customer model) {
		update();
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Label lblSearchCustomers = new Label(composite, SWT.NONE);
		lblSearchCustomers.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblSearchCustomers.setText("Search Customers");
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilter = new Label(composite_1, SWT.NONE);
		GridData gd_lblFilter = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblFilter.widthHint = 80;
		lblFilter.setLayoutData(gd_lblFilter);
		lblFilter.setText("Filter");
		
		textFilter = new Text(composite_1, SWT.BORDER);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCustomer = new Label(composite_1, SWT.NONE);
		lblCustomer.setText("Customer");
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new GridLayout(2, false));
		
		btnFirstName = new Button(composite_2, SWT.CHECK);
		btnFirstName.setText("First name");
		
		btnLastName = new Button(composite_2, SWT.CHECK);
		btnLastName.setText("Last name");
		
		btnAddress = new Button(composite_2, SWT.CHECK);
		btnAddress.setText("Address");
		
		btnCustomerId = new Button(composite_2, SWT.CHECK);
		btnCustomerId.setText("Customer ID");
		new Label(composite_1, SWT.NONE);
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnSearch = new Button(composite_3, SWT.NONE);
		btnSearch.setText("Search");
		
		Button btnClear = new Button(composite_3, SWT.NONE);
		btnClear.setText("Clear");
		
		Label lblSearchResults = new Label(composite, SWT.NONE);
		lblSearchResults.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblSearchResults.setText("Search Results");
		
		listViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// User Interaction
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				update();
			}
		});

		btnFirstName.addSelectionListener(listener);
		btnLastName.addSelectionListener(listener);
		btnAddress.addSelectionListener(listener);
		btnCustomerId.addSelectionListener(listener);
		btnSearch.addSelectionListener(listener);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.resetFilter();
				update();
			}
		});
		
		listViewer.addSelectionChangedListener((e)->{
			selectionService.setSelection(listViewer.getStructuredSelection().getFirstElement());
		});
		
		listViewer.addFilter(filter);
		
		update();
		
		m_bindingContext = initDataBindings();
		
	}
	
	protected SelectionAdapter listener = new SelectionAdapter() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			update();
		};
	};

	public void update() {	
		listViewer.refresh();
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextFilterObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(textFilter);
		IObservableValue filterTextFilterObserveValue = PojoProperties.value("filterText").observe(filter);
		bindingContext.bindValue(observeTextTextFilterObserveWidget, filterTextFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnFirstNameObserveWidget = WidgetProperties.selection().observe(btnFirstName);
		IObservableValue filterFirstNameFilterObserveValue = PojoProperties.value("filterFirstName").observe(filter);
		bindingContext.bindValue(observeSelectionBtnFirstNameObserveWidget, filterFirstNameFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnLastNameObserveWidget = WidgetProperties.selection().observe(btnLastName);
		IObservableValue filterLastNameFilterObserveValue = PojoProperties.value("filterLastName").observe(filter);
		bindingContext.bindValue(observeSelectionBtnLastNameObserveWidget, filterLastNameFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnAddressObserveWidget = WidgetProperties.selection().observe(btnAddress);
		IObservableValue filterAddressFilterObserveValue = PojoProperties.value("filterAddress").observe(filter);
		bindingContext.bindValue(observeSelectionBtnAddressObserveWidget, filterAddressFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnCustomerIdObserveWidget = WidgetProperties.selection().observe(btnCustomerId);
		IObservableValue filterCustomerIdFilterObserveValue = PojoProperties.value("filterCustomerId").observe(filter);
		bindingContext.bindValue(observeSelectionBtnCustomerIdObserveWidget, filterCustomerIdFilterObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(listContentProvider.getKnownElements(), Customer.class, "description");
		listViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		listViewer.setContentProvider(listContentProvider);
		//
		IObservableList customersGetModelObserveList = BeanProperties.list("customers").observe(getModel());
		listViewer.setInput(customersGetModelObserveList);
		//
		return bindingContext;
	}
}