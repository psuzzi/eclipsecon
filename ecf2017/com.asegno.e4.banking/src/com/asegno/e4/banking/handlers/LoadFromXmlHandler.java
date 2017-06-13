 
package com.asegno.e4.banking.handlers;

import java.io.File;

import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import com.asegno.e4.banking.EventConstants;
import com.asegno.e4.banking.data.DataProvider;
import com.asegno.e4.banking.dialogs.DialogUtils;
import com.asegno.e4.banking.model.Bank;

public class LoadFromXmlHandler {
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, EPartService partService,
			MApplication app, IEventBroker eventBroker) throws JAXBException {
		File xmlFile = DialogUtils.loadXmlFile(parentShell);
		if(xmlFile==null)
			return;
		Bank bank = DataProvider.loadXml(xmlFile);
		app.getContext().set(Bank.class, bank);
		eventBroker.send(EventConstants.TOPIC_MODEL_MODIFIED, bank);
	}
		
}