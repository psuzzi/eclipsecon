package com.asegno.e4.banking.controls;

import java.awt.Dialog;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.asegno.e4.banking.dialogs.CreateTransactionDialog;
import com.asegno.e4.banking.model.Charge;
import com.asegno.e4.banking.model.Deposit;
import com.asegno.e4.banking.model.Transaction;
import com.asegno.e4.banking.model.Transfer;
import com.asegno.e4.banking.model.Withdrawal;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Toolcontrol to create a transaction
 * @author psuzzi
 *
 */
public class TransactionCreateToolControl {
	
	private IEclipseContext context;
	private Composite composite;
	private Button btnNewTransaction;
	private Shell shell;
	private CCombo combo;

	@Inject
	public TransactionCreateToolControl(IEclipseContext context) {
		this.context = context;
		System.out.println("Create Transaction toolControl ");
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		this.shell = parent.getShell();		
		parent.getParent().setRedraw(true);
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(null);
		
		btnNewTransaction = new Button(composite, SWT.NONE);
		btnNewTransaction.setBounds(5, 0, 98, 23);
		btnNewTransaction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createTransaction();
			}
		});
		btnNewTransaction.setText("New transaction");
		
		combo = new CCombo(composite, SWT.BORDER | SWT.READ_ONLY);
		combo.setBounds(108, 2, 104, 21);
		combo.setItems(new String[] {"Deposit", "Withdrawal", "Charge", "Transfer"});
		combo.select(0);
	}

	protected void createTransaction() {
		String selection = combo.getText();
		switch (selection) {
		case "Deposit":
			openDialog(new Deposit());
			break;
		case "Withdrawal":
			openDialog(new Withdrawal());
			break;
		case "Charge":
			openDialog(new Charge());
			break;
		case "Transfer":
			openDialog(new Transfer());
			break;
		default:
			break;
		}
	}

	private void openDialog(Transaction transaction) {
		System.out.println("Create Transaction " + transaction);
		CreateTransactionDialog dialog = ContextInjectionFactory.make(CreateTransactionDialog.class, context);
		dialog.setTransaction(transaction);
		dialog.open();
	}
}
