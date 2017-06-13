package com.asegno.e4.banking.dialogs;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class DialogUtils {
	
	public static final String XML = "*.xml";
	public static final String ALL = "*.*";
	public static final String XML_FILES = "XML Files";
	public static final String ALL_FILES = "All Files";

	/** Open a Save As dialog which allow to select the file to save */
	public static File saveAsXmlFile(Shell parentShell) {
		FileDialog fileDialog = new FileDialog(parentShell, SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] {XML});
		fileDialog.setFilterNames(new String[] {XML_FILES});
		String selectedPath = fileDialog.open();
		if(selectedPath==null)
			return null;
		File selectedFile = new File(selectedPath);
		if(selectedFile.exists()) {
			if(!MessageDialog.openConfirm(parentShell, "Replace file", "Do you want to replace the existing file?")) {
				return null;
			}
		}
		if(!selectedFile.exists()) {
			try {
				selectedFile.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return selectedFile;
	}
	
	/** Open a Load dialog, which allows to select the file to load */
	public static File loadXmlFile(Shell parentShell) {
		FileDialog fileDialog = new FileDialog(parentShell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[] {XML});
		fileDialog.setFilterNames(new String[] {XML_FILES});
		String selectedPath = fileDialog.open();
		if(selectedPath==null)
			return null;
		File selectedFile = new File(selectedPath);
		if(!selectedFile.exists()) {
			return null;
		}
		return selectedFile;
	}
	
}
