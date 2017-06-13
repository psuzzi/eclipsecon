 
package com.asegno.e4.banking.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.model.Bank;

public class NewBankHander {
	@Execute
	public void execute(MApplication app, IEventBroker eventBroker) {
		Bank bank = new Bank();
		app.getContext().set(Bank.class, bank);
		eventBroker.send(EventConstants.TOPIC_MODEL_MODIFIED, bank);
	}
		
}