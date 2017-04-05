package org.eclipse.example.bowling.tutorial.popup.actions;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import bowling.Matchup;
import bowling.Tournament;

public abstract class AbstractTournamentExampleDialog extends Dialog {

	protected Label numberOfMatchups;

	protected AbstractTournamentExampleDialog(Shell parentShell) {
		super(parentShell);
	}

	private static final int _UNDO = 1002;
	private static final int _ADDMATCHUP = 1001;
	private Tournament tournament;
	private ComposedAdapterFactory composedAdapterFactory;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite root = (Composite) super.createDialogArea(parent);
		root.setLayout(new FillLayout(SWT.VERTICAL));
		Composite labelComposite = new Composite(root, SWT.None);
		labelComposite.setLayout(new GridLayout(2, false));
		Label text = new Label(labelComposite, SWT.None);
		text.setText("Number of Matchups: ");
		numberOfMatchups = new Label(labelComposite, SWT.None);
		TreeViewer treeViewer = new TreeViewer(root, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		initializeTreeviewer(treeViewer);
		initializeListener();
		updateNumberOfMatchups();
		root.layout();
		parent.pack();
		return parent;
	}

	protected abstract void initializeListener();

	protected abstract void loadContent(IFile file) throws IOException;

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	protected abstract void initializeTreeviewer(TreeViewer treeViewer);

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case _ADDMATCHUP:
			addMatchup();
			break;
		case _UNDO:
			undo();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

	protected abstract void undo();

	protected abstract void addMatchup();

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, _ADDMATCHUP, "Add Matchup", true);
		createButton(parent, _UNDO, "Undo", true);
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void okPressed() {
		try {
			save();
		} catch (IOException e) {
			Status status = new Status(IStatus.ERROR,
					"org.eclipse.example.bowling.tutorial", 0, e.getMessage(),
					null);
			ErrorDialog.openError(this.getShell(), "Error on save",
					"Something went wrong on save", status);
		}
		super.okPressed();
	}

	protected abstract void save() throws IOException;

	/**
	 * This method update the Label displaying the number of Matchup contained
	 * in the opened Tournament
	 */
	protected void updateNumberOfMatchups() {
		if (tournament != null) {
			EList<Matchup> matchups = tournament.getMatchups();
			if (matchups != null) {
				numberOfMatchups.setText(matchups.size() + "");
				return;
			}
		}
		numberOfMatchups.setText("");
		return;
	}

	/**
	 * Return an ComposedAdapterFactory for all registered modesl
	 * 
	 * @return a ComposedAdapterFactory
	 */
	protected AdapterFactory getAdapterFactory() {
		if (composedAdapterFactory == null) {
			composedAdapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		}
		return composedAdapterFactory;
	}

}
