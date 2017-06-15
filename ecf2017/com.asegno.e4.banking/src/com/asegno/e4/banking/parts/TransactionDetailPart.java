
package com.asegno.e4.banking.parts;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.data.Utils;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.model.Transaction;
import org.eclipse.wb.swt.SWTResourceManager;

public class TransactionDetailPart extends BasePart<Transaction>{
	private DataBindingContext m_bindingContext;
	private Text textType;
	private Text textAmount;
	private Text textDateConfirmed;
	private Text textDateProcessed;
	private Text textSourceId;
	private Text textSourceDescription;
	private Text textRecipientId;
	private Text textRecipientDescription;
	private Button btnConfirmed;
	private Button btnProcessed;
	private Group grpRecipientAccount;
	private Button btnConfirm;
	private Button btnProcess;
	
	@Override
	public void setModel(Transaction model) {
		if(model==null)
			return;
		disposeBindings(m_bindingContext);
		super.setModel(model);
		if(textAmount==null)
			return;
		m_bindingContext = initDataBindings();
		update();
	}
	
	public Transaction getModel() {
		return model;
	}
	
	/** Called by injection when a Transaction is set in the active selection */
	@Inject
	public void selectionChanged(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Transaction transaction) {
		if(transaction==null)
			return;
		setModel(transaction);
	}
	
	/** called by the E4 framework when an event is posted with the given topic and object */
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Bank bank) {
		setModel(new Transaction());
	}
	
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Transaction transaction) {
		update();
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Label lblTransactionInformation = new Label(composite, SWT.NONE);
		lblTransactionInformation.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTransactionInformation.setText("Transaction Information");
		
		Group grpDetails = new Group(composite, SWT.NONE);
		grpDetails.setLayout(new GridLayout(2, false));
		grpDetails.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpDetails.setText("Details");
		
		Label lblType = new Label(grpDetails, SWT.NONE);
		GridData gd_lblType = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblType.widthHint = 80;
		lblType.setLayoutData(gd_lblType);
		lblType.setText("Type");
		
		textType = new Text(grpDetails, SWT.BORDER);
		textType.setEditable(false);
		textType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAmount = new Label(grpDetails, SWT.NONE);
		lblAmount.setText("Amount");
		
		textAmount = new Text(grpDetails, SWT.BORDER);
		textAmount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpWorkflow = new Group(composite, SWT.NONE);
		grpWorkflow.setLayout(new GridLayout(4, false));
		grpWorkflow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpWorkflow.setText("Workflow");
		
		btnConfirm = new Button(grpWorkflow, SWT.NONE);
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date date = Utils.stringToDate(textDateConfirmed.getText());
				if(date==null) {
					textDateConfirmed.setText(Utils.dateToString(new Date()));
				}
				getModel().confirm(date);
			}
		});
		GridData gd_btnConfirm = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnConfirm.widthHint = 60;
		btnConfirm.setLayoutData(gd_btnConfirm);
		btnConfirm.setText("Confirm");
		
		Label lblDate = new Label(grpWorkflow, SWT.NONE);
		lblDate.setAlignment(SWT.RIGHT);
		GridData gd_lblDate = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblDate.widthHint = 80;
		lblDate.setLayoutData(gd_lblDate);
		lblDate.setText("date");
		
		textDateConfirmed = new Text(grpWorkflow, SWT.BORDER);
		textDateConfirmed.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		btnConfirmed = new Button(grpWorkflow, SWT.CHECK);
		btnConfirmed.setEnabled(false);
		btnConfirmed.setText("confirmed");
		
		btnProcess = new Button(grpWorkflow, SWT.NONE);
		btnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Date date = Utils.stringToDate(textDateProcessed.getText());
				if(date==null) {
					textDateProcessed.setText(Utils.dateToString(new Date()));
				}
				if(!getModel().isConfirmed()) {					
					getModel().confirm(date);
				}
				getModel().process(date);
			}
		});
		GridData gd_btnProcess = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnProcess.widthHint = 60;
		btnProcess.setLayoutData(gd_btnProcess);
		btnProcess.setText("Process");
		
		Label lblDate_1 = new Label(grpWorkflow, SWT.NONE);
		lblDate_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDate_1.setText("date");
		
		textDateProcessed = new Text(grpWorkflow, SWT.BORDER);
		textDateProcessed.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		btnProcessed = new Button(grpWorkflow, SWT.CHECK);
		btnProcessed.setEnabled(false);
		btnProcessed.setText("processed");
		
		Group grpSourceAccount = new Group(composite, SWT.NONE);
		grpSourceAccount.setLayout(new GridLayout(2, false));
		grpSourceAccount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpSourceAccount.setText("Source Account");
		
		Label lblId = new Label(grpSourceAccount, SWT.NONE);
		GridData gd_lblId = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblId.widthHint = 80;
		lblId.setLayoutData(gd_lblId);
		lblId.setText("ID");
		
		textSourceId = new Text(grpSourceAccount, SWT.BORDER);
		textSourceId.setEditable(false);
		textSourceId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(grpSourceAccount, SWT.NONE);
		lblDescription.setText("Description");
		
		textSourceDescription = new Text(grpSourceAccount, SWT.BORDER);
		textSourceDescription.setEditable(false);
		textSourceDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		grpRecipientAccount = new Group(composite, SWT.NONE);
		grpRecipientAccount.setLayout(new GridLayout(2, false));
		grpRecipientAccount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpRecipientAccount.setText("Recipient Account");
		
		Label lblId_1 = new Label(grpRecipientAccount, SWT.NONE);
		GridData gd_lblId_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblId_1.widthHint = 80;
		lblId_1.setLayoutData(gd_lblId_1);
		lblId_1.setText("ID");
		
		textRecipientId = new Text(grpRecipientAccount, SWT.BORDER);
		textRecipientId.setEditable(false);
		textRecipientId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription_1 = new Label(grpRecipientAccount, SWT.NONE);
		lblDescription_1.setText("Description");
		
		textRecipientDescription = new Text(grpRecipientAccount, SWT.BORDER);
		textRecipientDescription.setEditable(false);
		textRecipientDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		m_bindingContext = initDataBindings();
		
	}
	
	private void update() {
		// Update the UI
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextTypeObserveWidget = WidgetProperties.text(SWT.Modify).observe(textType);
		IObservableValue typeGetModelObserveValue = BeanProperties.value("type").observe(getModel());
		bindingContext.bindValue(observeTextTextTypeObserveWidget, typeGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextAmountObserveWidget = WidgetProperties.text(SWT.Modify).observe(textAmount);
		IObservableValue amountGetModelObserveValue = BeanProperties.value("amount").observe(getModel());
		bindingContext.bindValue(observeTextTextAmountObserveWidget, amountGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextDateConfirmedObserveWidget = WidgetProperties.text(SWT.Modify).observe(textDateConfirmed);
		IObservableValue confirmedDateStringGetModelObserveValue = BeanProperties.value("confirmedDateString").observe(getModel());
		bindingContext.bindValue(observeTextTextDateConfirmedObserveWidget, confirmedDateStringGetModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnConfirmedObserveWidget = WidgetProperties.selection().observe(btnConfirmed);
		IObservableValue confirmedGetModelObserveValue = BeanProperties.value("confirmed").observe(getModel());
		bindingContext.bindValue(observeSelectionBtnConfirmedObserveWidget, confirmedGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextDateProcessedObserveWidget = WidgetProperties.text(SWT.Modify).observe(textDateProcessed);
		IObservableValue processedDateStringGetModelObserveValue = BeanProperties.value("processedDateString").observe(getModel());
		bindingContext.bindValue(observeTextTextDateProcessedObserveWidget, processedDateStringGetModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnProcessedObserveWidget = WidgetProperties.selection().observe(btnProcessed);
		IObservableValue processedGetModelObserveValue = BeanProperties.value("processed").observe(getModel());
		bindingContext.bindValue(observeSelectionBtnProcessedObserveWidget, processedGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextSourceIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(textSourceId);
		IObservableValue sourceAccountIdGetModelObserveValue = BeanProperties.value("sourceAccountId").observe(getModel());
		bindingContext.bindValue(observeTextTextSourceIdObserveWidget, sourceAccountIdGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextSourceDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(textSourceDescription);
		IObservableValue sourceAccountDescriptionGetModelObserveValue = BeanProperties.value("sourceAccountDescription").observe(getModel());
		bindingContext.bindValue(observeTextTextSourceDescriptionObserveWidget, sourceAccountDescriptionGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextRecipientIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(textRecipientId);
		IObservableValue recipientAccountIdGetModelObserveValue = BeanProperties.value("recipientAccountId").observe(getModel());
		bindingContext.bindValue(observeTextTextRecipientIdObserveWidget, recipientAccountIdGetModelObserveValue, null, null);
		//
		IObservableValue observeTextTextRecipientDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(textRecipientDescription);
		IObservableValue recipientAccountDescriptionGetModelObserveValue = BeanProperties.value("recipientAccountDescription").observe(getModel());
		bindingContext.bindValue(observeTextTextRecipientDescriptionObserveWidget, recipientAccountDescriptionGetModelObserveValue, null, null);
		//
		IObservableValue observeVisibleGrpRecipientAccountObserveWidget = WidgetProperties.visible().observe(grpRecipientAccount);
		IObservableValue hasRecipientAccountGetModelObserveValue = BeanProperties.value("hasRecipientAccount").observe(getModel());
		bindingContext.bindValue(observeVisibleGrpRecipientAccountObserveWidget, hasRecipientAccountGetModelObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnConfirmObserveWidget = WidgetProperties.enabled().observe(btnConfirm);
		IObservableValue notConfirmedGetModelObserveValue = BeanProperties.value("notConfirmed").observe(getModel());
		bindingContext.bindValue(observeEnabledBtnConfirmObserveWidget, notConfirmedGetModelObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnProcessObserveWidget = WidgetProperties.enabled().observe(btnProcess);
		IObservableValue notProcessedGetModelObserveValue = BeanProperties.value("notProcessed").observe(getModel());
		bindingContext.bindValue(observeEnabledBtnProcessObserveWidget, notProcessedGetModelObserveValue, null, null);
		//
		return bindingContext;
	}
}