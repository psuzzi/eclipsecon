package com.itemis.e4.banking.dialogs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.itemis.e4.banking.model.Account;
import com.itemis.e4.banking.model.Bank;
import com.itemis.e4.banking.model.Transaction;

public class CreateTransactionDialog extends TitleAreaDialog {
	private DataBindingContext m_bindingContext;
	
	Transaction transaction;
	
	private Text textType;
	private Text textAmount;
	private Text textSourceId;
	private Text textSourceDescription;
	private Text textRecipientId;
	private Text textRecipientDescription;
	private Group group_2;

	private IEclipseContext context;

	private Button okButton;

	private Button cancelButton ;

	private Bank bank;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	@Inject
	public CreateTransactionDialog(
		@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, IEclipseContext context) {
		super(parentShell);
		this.context = context;
		this.bank = context.get(Bank.class);
	}
	
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Fill the required fields to create a transaction");
		setTitle("Create Transaction");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group.setText("Details");
		group.setLayout(new GridLayout(2, false));
		
		Label label = new Label(group, SWT.NONE);
		GridData gd_label = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 100;
		label.setLayoutData(gd_label);
		label.setText("Type");
		
		textType = new Text(group, SWT.BORDER);
		textType.setEditable(false);
		textType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("Amount");
		
		textAmount = new Text(group, SWT.BORDER);
		textAmount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group group_1 = new Group(container, SWT.NONE);
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group_1.setText("Source account");
		group_1.setLayout(new GridLayout(3, false));
		
		Label label_2 = new Label(group_1, SWT.NONE);
		GridData gd_label_2 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.widthHint = 100;
		label_2.setLayoutData(gd_label_2);
		label_2.setText("Id:");
		
		textSourceId = new Text(group_1, SWT.BORDER);
		textSourceId.setEditable(false);
		textSourceId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSourceSearch = new Button(group_1, SWT.NONE);
		btnSourceSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Account account = searchAccountDialog();
				if(account!=null) {
					transaction.setSourceAccount(account);
					update();
				}
			}
		});
		btnSourceSearch.setText("Search...");
		
		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setText("Description: ");
		
		textSourceDescription = new Text(group_1, SWT.BORDER);
		textSourceDescription.setEditable(false);
		textSourceDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group_1, SWT.NONE);
		
		group_2 = new Group(container, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group_2.setText("Recipient account");
		group_2.setLayout(new GridLayout(3, false));
		
		Label label_4 = new Label(group_2, SWT.NONE);
		GridData gd_label_4 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_label_4.widthHint = 100;
		label_4.setLayoutData(gd_label_4);
		label_4.setText("Id:");
		
		textRecipientId = new Text(group_2, SWT.BORDER);
		textRecipientId.setEditable(false);
		textRecipientId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnRecipientSearch = new Button(group_2, SWT.NONE);
		btnRecipientSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Account account = searchAccountDialog();
				if(account!=null) {
					transaction.setRecipientAccount(account);
					update();
				}
			}
		});
		btnRecipientSearch.setText("Search...");
		
		Label label_5 = new Label(group_2, SWT.NONE);
		label_5.setText("Description: ");
		
		textRecipientDescription = new Text(group_2, SWT.BORDER);
		textRecipientDescription.setEditable(false);
		textRecipientDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group_2, SWT.NONE);
		
		textAmount.addListener(SWT.FocusOut, (e)->{
			update();
		});

		return area;
	}

	protected Account searchAccountDialog() {
		SearchAccountDialog accountDialog = ContextInjectionFactory.make(SearchAccountDialog.class, context);
		if(accountDialog.open()==Window.OK) {
			return accountDialog.getSelectedAccount();
		}
		return null;
	}
	
	private void update() {
		m_bindingContext.updateTargets();
		okButton.setEnabled(validatePage());
	}
	
	private boolean validatePage() {
		if(transaction==null || transaction.getAmount()<=0 || transaction.getSourceAccount()==null || (transaction.isHasRecipientAccount()&&transaction.getRecipientAccount()==null)) {
			setErrorMessage("Please fill in valid transaction data");
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				bank.addTransaction(transaction);
			}
		});
		cancelButton  = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
		update();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 467);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(textType);
		IObservableValue typeTransactionObserveValue = BeanProperties.value("type").observe(transaction);
		bindingContext.bindValue(observeTextTextObserveWidget, typeTransactionObserveValue, null, null);
		//
		IObservableValue observeTextText_1ObserveWidget = WidgetProperties.text(SWT.Modify).observe(textAmount);
		IObservableValue amountTransactionObserveValue = BeanProperties.value("amount").observe(transaction);
		bindingContext.bindValue(observeTextText_1ObserveWidget, amountTransactionObserveValue, null, null);
		//
		IObservableValue observeTextText_2ObserveWidget = WidgetProperties.text(SWT.Modify).observe(textSourceId);
		IObservableValue sourceAccountIdTransactionObserveValue = BeanProperties.value("sourceAccountId").observe(transaction);
		bindingContext.bindValue(observeTextText_2ObserveWidget, sourceAccountIdTransactionObserveValue, null, null);
		//
		IObservableValue observeTextText_3ObserveWidget = WidgetProperties.text(SWT.Modify).observe(textSourceDescription);
		IObservableValue sourceAccountDescriptionTransactionObserveValue = BeanProperties.value("sourceAccountDescription").observe(transaction);
		bindingContext.bindValue(observeTextText_3ObserveWidget, sourceAccountDescriptionTransactionObserveValue, null, null);
		//
		IObservableValue observeTextText_4ObserveWidget = WidgetProperties.text(SWT.Modify).observe(textRecipientId);
		IObservableValue recipientAccountIdTransactionObserveValue = BeanProperties.value("recipientAccountId").observe(transaction);
		bindingContext.bindValue(observeTextText_4ObserveWidget, recipientAccountIdTransactionObserveValue, null, null);
		//
		IObservableValue observeTextText_5ObserveWidget = WidgetProperties.text(SWT.Modify).observe(textRecipientDescription);
		IObservableValue recipientAccountDescriptionTransactionObserveValue = BeanProperties.value("recipientAccountDescription").observe(transaction);
		bindingContext.bindValue(observeTextText_5ObserveWidget, recipientAccountDescriptionTransactionObserveValue, null, null);
		//
		IObservableValue observeVisibleGroup_2ObserveWidget = WidgetProperties.visible().observe(group_2);
		IObservableValue hasRecipientAccountTransactionObserveValue = BeanProperties.value("hasRecipientAccount").observe(transaction);
		bindingContext.bindValue(observeVisibleGroup_2ObserveWidget, hasRecipientAccountTransactionObserveValue, null, null);
		//
		return bindingContext;
	}
}
