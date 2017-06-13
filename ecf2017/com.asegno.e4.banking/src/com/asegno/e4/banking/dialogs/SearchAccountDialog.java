package com.asegno.e4.banking.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Account;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.parts.filters.AccountsFilter;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoProperties;

public class SearchAccountDialog extends Dialog {
	private DataBindingContext m_bindingContext;
	
	private Text textFilter;
	private Button btnAccountId;
	private Button btnFirstName;
	private Button btnLastName;
	private Button btnAddress;
	private Button btnCustomerId;
	private Button btnSearch;
	private Button btnClear;
	private List list;
	private ListViewer listViewer;

	private Bank bank;
	private AccountsFilter filter;
	
	private Account selectedAccount;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	@Inject
	public SearchAccountDialog(Shell parentShell, IEclipseContext context) {
		super(parentShell);
		this.bank = context.get(Bank.class);
		this.filter = new AccountsFilter();
		resetFilter();
	}
	
	private void resetFilter() {
		this.filter.setFilterText("");
		this.filter.setCustomersFilter(true, true,  true,  false);
		this.filter.setAccountFilter(true);
	}

	@Inject
	@Optional
	private void subscribeTopicUpdated(@UIEventTopic(EventConstants.TOPIC_ACCOUNT_MODIFIED) Account account) {
		if(listViewer!=null) {
			listViewer.refresh();
		}
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Accounts Search");
		label.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		GridData gd_label_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label_1.widthHint = 100;
		label_1.setLayoutData(gd_label_1);
		label_1.setText("Filter");
		
		textFilter = new Text(composite_1, SWT.BORDER);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_2 = new Label(composite_1, SWT.NONE);
		label_2.setText("Account: ");
		
		btnAccountId = new Button(composite_1, SWT.CHECK);
		btnAccountId.setText("account id");
		
		Label label_3 = new Label(composite_1, SWT.NONE);
		label_3.setText("Customer");
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		
		btnFirstName = new Button(composite_2, SWT.CHECK);
		btnFirstName.setText("First name");
		btnFirstName.setSelection(true);
		
		btnLastName = new Button(composite_2, SWT.CHECK);
		btnLastName.setText("Last name");
		btnLastName.setSelection(true);
		
		btnAddress = new Button(composite_2, SWT.CHECK);
		btnAddress.setText("Address");
		btnAddress.setSelection(true);
		
		btnCustomerId = new Button(composite_2, SWT.CHECK);
		btnCustomerId.setText("customer id");
		new Label(composite_1, SWT.NONE);
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		
		btnSearch = new Button(composite_3, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSearch.setText("Search");
		
		btnClear = new Button(composite_3, SWT.NONE);
		btnClear.setText("Clear");
		
		Composite composite_4 = new Composite(container, SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		
		Label label_4 = new Label(composite_4, SWT.NONE);
		label_4.setText("Search Results");
		label_4.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// User interaction
		final SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listViewer.refresh();
			}
		};
		
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listViewer.refresh();
			}
		});
		btnFirstName.addSelectionListener(selectionAdapter);
		btnLastName.addSelectionListener(selectionAdapter);
		btnAddress.addSelectionListener(selectionAdapter);
		btnCustomerId.addSelectionListener(selectionAdapter);
		btnSearch.addSelectionListener(selectionAdapter);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetFilter();
				listViewer.refresh();
			}
		});
		
		// add selection listener
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
		    @Override
		    public void selectionChanged(SelectionChangedEvent event) {
		        IStructuredSelection selection = listViewer.getStructuredSelection();
		        if(!selection.isEmpty() && selection.getFirstElement() instanceof Account) {
		        	selectedAccount = (Account) selection.getFirstElement();
		        }
		    }
		});
		
		listViewer.addFilter(filter);

		return container;
	}
	
	public Account getSelectedAccount() {
		return this.selectedAccount;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 562);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextFilterObserveWidget = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut, SWT.DefaultSelection}).observe(textFilter);
		IObservableValue filterTextFilterObserveValue = PojoProperties.value("filterText").observe(filter);
		bindingContext.bindValue(observeTextTextFilterObserveWidget, filterTextFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnIAccountIdObserveWidget = WidgetProperties.selection().observe(btnAccountId);
		IObservableValue filterAccountIdNumberFilterObserveValue = PojoProperties.value("filterAccountIdNumber").observe(filter);
		bindingContext.bindValue(observeSelectionBtnIAccountIdObserveWidget, filterAccountIdNumberFilterObserveValue, null, null);
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
		IObservableValue observeSelectionBtnIdNumberObserveWidget = WidgetProperties.selection().observe(btnCustomerId);
		IObservableValue filterIdNumberFilterObserveValue = PojoProperties.value("filterIdNumber").observe(filter);
		bindingContext.bindValue(observeSelectionBtnIdNumberObserveWidget, filterIdNumberFilterObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = BeansObservables.observeMap(listContentProvider.getKnownElements(), Account.class, "accountDescription");
		listViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		listViewer.setContentProvider(listContentProvider);
		//
		IObservableList accountsBankObserveList = PojoProperties.list("accounts").observe(bank);
		listViewer.setInput(accountsBankObserveList);
		//
		return bindingContext;
	}

}
