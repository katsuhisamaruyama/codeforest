package org.jtool.codeforest.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jtool.codeforest.Activator;

public class CodeForestWizard extends Wizard implements INewWizard{
	private CodeForestPage page;
	private boolean analysisFlag = false;
	private boolean evolutionFlag = false;
	private String projectName;
	private String[] vcsData;

	@Override
	public boolean performFinish() {
		if(page.projectList.getSelection()[0] == null){
			return false;
		}
		projectName = page.projectList.getSelection()[0];

		if(page.analysis.getSelection()){
			analysisFlag = true;
		}
		
		if(page.evolution.getSelection()){
			evolutionFlag = true;
			vcsData = new String[4];
			vcsData[0] = page.vcsList.getSelection()[0];
			vcsData[1] = page.address.toString();
			vcsData[2] = page.user.toString();
			vcsData[3] = page.password.toString();
		}
		
		return true;
	}

	@Override
	public void addPages(){
		page = new CodeForestPage();
		addPage(page);

	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean canFinish(){
		if(page.isPageComplete()){
			return true;
		}
		return false;
	}

	public String getProjectName(){
		return projectName;
	}
	
	public String[] getVCSData(){
		return vcsData;
	}

	public boolean isAnalysis(){
		return analysisFlag;
	}
	
	public boolean isEvolution(){
		return evolutionFlag;
	}

	class CodeForestPage extends WizardPage {
		private final String[] VCS = {"Subversion", "CVS",};

		Button analysis;
		Button evolution;
		
		List projectList;
		List vcsList;
		Text path;
		Text address;
		Text user;
		Text password;

		protected CodeForestPage() {
			super("CodeForest");
			setTitle("CodeForest");
			setPageComplete(true);
		}

		@Override
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.FILL);
			GridLayout layout = new GridLayout(3, true);
			layout.horizontalSpacing = 50;
			composite.setLayout(layout);
			GridData gridData;

			Group projectPanel = new Group(composite, SWT.NONE);
			projectPanel.setLayout(new GridLayout(1, false));
			projectPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
			projectPanel.setText("Select from Workspace");

			projectList = new List(projectPanel, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
			setProject(projectList);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			projectList.setLayoutData(gridData);
			
			Group analysisPanel = new Group(composite, SWT.NONE);
			analysisPanel.setLayout(new GridLayout(2, true));
			gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
			gridData.horizontalSpan = 2;
			analysisPanel.setLayoutData(gridData);
			analysisPanel.setText("Analysis check");

			analysis = new Button(analysisPanel, SWT.CHECK);
			analysis.setText("Analysis");

			evolution = new Button(analysisPanel, SWT.CHECK);
			evolution.setText("Evolution");

			vcsList = new List(analysisPanel, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
			setVCSList(vcsList);
			gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			gridData.horizontalSpan = 2;
			vcsList.setLayoutData(gridData);
			
			Label pathLabel = new Label(analysisPanel, SWT.SINGLE | SWT.BORDER);
			pathLabel.setText("Path");
			gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			gridData.horizontalSpan = 1;
			pathLabel.setLayoutData(gridData);
						
			path = new Text(analysisPanel, SWT.SINGLE | SWT.BORDER);
			path.setLayoutData(gridData);
			
			Label addressLabel = new Label(analysisPanel, SWT.SINGLE | SWT.BORDER);
			addressLabel.setText("HOST");
			addressLabel.setLayoutData(gridData);

			address = new Text(analysisPanel, SWT.SINGLE | SWT.BORDER);
			address.setLayoutData(gridData);

			Label userLabel = new Label(analysisPanel, SWT.SINGLE | SWT.BORDER);
			userLabel.setText("USER");
			userLabel.setLayoutData(gridData);

			user = new Text(analysisPanel, SWT.SINGLE | SWT.BORDER);
			user.setLayoutData(gridData);

			Label passwordLabel = new Label(analysisPanel, SWT.SINGLE | SWT.BORDER);
			passwordLabel.setText("PASSWORD");
			passwordLabel.setLayoutData(gridData);

			password = new Text(analysisPanel, SWT.SINGLE | SWT.BORDER);
			password.setLayoutData(gridData);

			setControl(composite);
		}

		private void setProject(List list){
			IProject[] projects = Activator.getWorkspaceRoot().getProjects();
			for(int i = 0; i < projects.length; i++){
					list.add(projects[i].getName());
			}
		}
		
		private void setVCSList(List list){
			for(int i = 0; i < VCS.length; i++){
				list.add(VCS[i]);
			}
		}
	}
}

