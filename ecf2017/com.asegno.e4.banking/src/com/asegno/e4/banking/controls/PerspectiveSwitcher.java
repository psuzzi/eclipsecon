package com.asegno.e4.banking.controls;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.swt.util.ISWTResourceUtilities;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.service.event.Event;

public class PerspectiveSwitcher{
	
	private static final String MPERSPECTIVE = "MPerspective";

	private MPerspectiveStack perspectiveStack;
	private ToolBar toolBar;
	private boolean skipEvent = false;
	
	private SelectionAdapter itemSelectionAdapter = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			ToolItem selected = (ToolItem) e.getSource();
			if (skipEvent) {
				return;
			}
			skipEvent = true;
			ToolItem selectedItem = (ToolItem) e.getSource();
			selectToolItem(selectedItem);
			skipEvent = false;
		}
	};
	
	@Inject
	@Optional
	void elementSelected(
			@UIEventTopic(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT) Event event) {
		Object element = event.getProperty(UIEvents.EventTags.ELEMENT);
		if (element instanceof MPerspective) {
			// perspective is changed
			handlePerspectiveChanged();
		}
	}

	@Inject
	public PerspectiveSwitcher(Composite parent, MWindow window, IEclipseContext context) {
		
		perspectiveStack = getPerspectiveStack(window);
		
		toolBar = (ToolBar) parent.getParent();
		
		int size = perspectiveStack.getChildren().size();
		
		List<MPerspective> perspectives = perspectiveStack.getChildren().stream()
					.filter(p->p.isToBeRendered()&&p.isVisible())
					.collect(Collectors.toList());
		
		int i=0;
		for(MPerspective p: perspectives) {
			ToolItem ti = new ToolItem(toolBar, SWT.CHECK);
			ti.setText(""+p.getLabel());
			String iconURI = p.getIconURI();
			if(iconURI!=null) {
				ISWTResourceUtilities resUtils = (ISWTResourceUtilities) context
						.get(IResourceUtilities.class.getName());
				Image image = resUtils.imageDescriptorFromURI(
						URI.createURI(iconURI)).createImage();
				ti.setImage(image);
			}
			ti.setData(MPERSPECTIVE, p);
			ti.addSelectionListener(itemSelectionAdapter);
			if(++i<perspectives.size()-1) {
				new ToolItem(toolBar, SWT.SEPARATOR);
			}
		}
		handlePerspectiveChanged();
	}
	
	/**
	 * Update the toolbar item selection
	 */
	protected void handlePerspectiveChanged() {
		MPerspective selectedPerspective = perspectiveStack.getSelectedElement();
		// get the selected item		
		ToolItem selectedItem = null;
		for(ToolItem i: toolBar.getItems()) {
			if(i.getSelection()) {
				selectedItem = i;
				break;
			}
		}
		// 
		if(selectedItem!=null) {
			MPerspective perspective = (MPerspective) selectedItem.getData(MPERSPECTIVE);
			if(perspective!=selectedPerspective) {
				selectPerspective(selectedPerspective);
			}
		} else {
			selectPerspective(selectedPerspective);			
		}
	}
	
	private void selectPerspective(MPerspective selectedPerspective) {
		for (ToolItem item : toolBar.getItems()) {
			MPerspective perspective = (MPerspective) item.getData(MPERSPECTIVE);
			if (perspective == selectedPerspective) {
				if (item.isEnabled()) {
					item.setSelection(true);
					selectToolItem(item);
				}
				break;
			}
		}
	}
	
	/**
	 * Select the given toolitem and deselect the others
	 */
	private void selectToolItem(ToolItem selectedItem) {
		if (!selectedItem.getSelection()) {
			selectedItem.setSelection(true);
		} else {
			for (ToolItem item : toolBar.getItems()) {
				if (item != selectedItem && item.isEnabled()) {
					item.setSelection(false);
				}
			}
			MPerspective perspective = (MPerspective) selectedItem.getData(MPERSPECTIVE);
			perspectiveStack.setSelectedElement(perspective);

		}
	}

	private MPerspectiveStack getPerspectiveStack(MWindow window) {
		for(MWindowElement elem : window.getChildren()) {
			if(elem instanceof MPerspectiveStack) {
				return (MPerspectiveStack) elem;
			}
		}
		return null;
	}
	
}
