
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.parts.filters.AccountsFilter;

public class AccountSearchPart extends BasePart<Bank>{
	
	private AccountsFilter filter = new AccountsFilter();
	
	@Inject
	protected ESelectionService selectionService;
	
	@Inject
	public AccountSearchPart(MApplication app) {
		setModel(app.getContext().get(Bank.class));
		filter.resetFilter();
	}
	
	@Override
	public void setModel(Bank model) {
		if(model==null)
			return;
		super.setModel(model);
	}
	
	public Bank gteModel() {
		return model;
	}
	
	/** called by the E4 framework when an event is posted with the given topic and a Bank object */
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Bank model) {
		setModel(model);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
	}
	
	protected SelectionAdapter updateListener = new SelectionAdapter() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			update();
		};
	};

	protected void update() {
		// TODO Auto-generated method stub
	}

}