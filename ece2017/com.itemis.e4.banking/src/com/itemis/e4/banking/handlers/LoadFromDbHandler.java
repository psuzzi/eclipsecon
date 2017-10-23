 
package com.itemis.e4.banking.handlers;

import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import com.itemis.e4.banking.EventConstants;
import com.itemis.e4.banking.data.DbDataProvider;
import com.itemis.e4.banking.model.Bank;

public class LoadFromDbHandler {
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, EPartService partService,
			MApplication app, IEventBroker eventBroker) throws JAXBException {
		Bank bank = DbDataProvider.loadDB();
		app.getContext().set(Bank.class, bank);
		eventBroker.send(EventConstants.TOPIC_MODEL_MODIFIED, bank);
	}
		
}