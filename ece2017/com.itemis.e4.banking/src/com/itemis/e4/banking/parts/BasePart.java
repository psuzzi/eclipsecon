
package com.itemis.e4.banking.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.itemis.e4.banking.EventConstants;
import com.itemis.e4.banking.model.BaseModel;

/** Base part for UIs of a given object */
public class BasePart<T extends BaseModel> implements PropertyChangeListener {
	
	@Inject
	protected IEventBroker eventBroker;
	
	protected T model;

	public void setModel(T model) {
		if(model==null)
			return;
		disposeListeners(model);
		this.model = model;
		model.addPropertyChangeListener(this);
	}
	
	protected void disposeBindings(DataBindingContext m_bindingContext) {
		if(m_bindingContext==null)
			return;
		m_bindingContext.dispose();
	}
	
	protected void disposeListeners(BaseModel model) {
		if(model==null)
			return;
		model.removePropertyChangeListener(this);
	}
	
	@PreDestroy
	public void preDestroy() {
		disposeListeners(model);
	}
	
	public void postEvent(String topic, Object data) {
		eventBroker.post(topic, data);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		System.out.println("Property change: " + e);
		if(e.getSource()==model) {
			postEvent(EventConstants.TOPIC_MODEL_MODIFIED, model);
		}
	}
	
	protected void addSelectionListener(SelectionListener selectionListener, Button ... buttons ) {
		for(Button b : buttons) {
			b.addSelectionListener(selectionListener);
		}
	}
	
	public static void resizeColumns(Table table) {
		int totalWidth = table.getClientArea().width;
		int floorDiv = Math.floorDiv(totalWidth, table.getColumnCount());
		for(TableColumn tc : table.getColumns()) {
			tc.setWidth(floorDiv);
		}
	}

}