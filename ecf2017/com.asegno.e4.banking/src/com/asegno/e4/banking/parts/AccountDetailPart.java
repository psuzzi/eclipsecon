
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

public class AccountDetailPart extends BasePart<Account>{
	
	
	@Override
	public void setModel(Account model) {
		if(model==null)
			return;
		super.setModel(model);
	}
	
	public Account getModel() {
		return model;
	}
	
	/** Called by injection when a Customer is set in the active selection */
	@Inject
	public void changed(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Account account) {
		if(account==null)
			return;
		setModel(account);
	}
	
	@Inject
	@Optional
	private void modelModified(@UIEventTopic(EventConstants.TOPIC_MODEL_MODIFIED) Account account) {
		update();
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
	}
	
	private void update() {
	}

}