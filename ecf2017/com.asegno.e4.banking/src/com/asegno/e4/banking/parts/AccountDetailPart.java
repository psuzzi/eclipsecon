
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Account;

public class AccountDetailPart extends BasePart<Account>{
	

	private Text textAccountID;
	private Text textBalance;
	private Table table;
	private Table table_1;
	private TableViewer tableViewer;
	private TableViewer tableViewer_1;
	
	
	@Override
	public void setModel(Account model) {
		if(model==null)
			return;
		super.setModel(model);
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
		
	}
	
	private void update() {
		// Update the UI
	}

}