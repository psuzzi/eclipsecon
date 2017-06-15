
package com.asegno.e4.banking.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Composite;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Transaction;

public class TransactionDetailPart extends BasePart<Transaction>{
	
	@Override
	public void setModel(Transaction model) {
		if(model==null)
			return;
		super.setModel(model);
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
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Transaction transaction) {
		update();
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
	}
	
	private void update() {
		// Update the UI
	}

}