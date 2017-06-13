
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Composite;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Bank;
import com.asegno.e4.banking.parts.filters.TransactionsFilter;

public class TransactionSearchPart extends BasePart<Bank>{
	
	private TransactionsFilter filter = new TransactionsFilter();
	
	@Inject
	protected ESelectionService selectionService;
	
	@Inject
	public TransactionSearchPart(MApplication app) {
		setModel(app.getContext().get(Bank.class));
		filter.resetFilter();
	}
	
	@Override
	public void setModel(Bank model) {
		if(model==null)
			return;
		super.setModel(model);
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


	@PostConstruct
	public void postConstruct(Composite parent) {
		
	}
	
	protected void update() {
		// TODO Auto-generated method stub
	}

}