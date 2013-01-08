package infodoc.basic;

import infodoc.basic.activity.AssignMulipleUsers;
import infodoc.basic.activity.AssignMultipleUserGroups;
import infodoc.basic.activity.AssignSingleUser;
import infodoc.basic.activity.AssignSingleUserGroup;
import infodoc.basic.activity.Create;
import infodoc.basic.activity.Transfer;
import infodoc.basic.activity.Unassign;
import infodoc.basic.activity.Update;
import infodoc.basic.component.Dashboard;
import infodoc.basic.component.SummaryComponent;
import infodoc.basic.component.HqlReportViewer;
import infodoc.basic.report.ActivityVolumeByUser;
import infodoc.basic.report.ActivityVolumeByUserGroup;
import infodoc.basic.report.CapacityReport;
import infodoc.basic.report.FinalizedProcessesReport;
import infodoc.basic.report.PendingProcessesReport;
import infodoc.basic.report.PerformanceByUserGroupReport;
import infodoc.basic.report.PerformanceByUserReport;
import infodoc.basic.validator.Email;
import infodoc.basic.validator.LongNumber;
import infodoc.basic.validator.MaxStringLength;
import infodoc.basic.validator.MinStringLength;
import infodoc.basic.validator.Regexp;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.HqlReport;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Process;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutorHelper;
import infodoc.core.ui.auth.OptionsWindow;
import infodoc.core.ui.auth.OptionsWindow.Listener;
import infodoc.core.ui.comun.InfodocModule;
import infodoc.core.ui.comun.InfodocReport;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.fieldfactory.ActivityFieldFactory;
import infodoc.core.ui.fieldfactory.JavaReportFieldFactory;
import infodoc.core.ui.fieldfactory.PropertyFieldFactory;
import infodoc.core.ui.fieldfactory.ValidationFieldFactory;
import infodoc.core.ui.processinstance.ProcessInstanceSearchComponent;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.johan.Toolbox;
import org.vaadin.johan.Toolbox.ORIENTATION;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;

import enterpriseapp.Utils;
import enterpriseapp.ui.window.MDIWindow;

public class BasicModule extends InfodocModule implements Command {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(BasicModule.class);
	
	private static final Listener listener = new Listener() {
		
		private TextField dashboardUrl = new TextField(BasicConstants.uiUserDashboardUrl);
		
		@Override
		public void addComponent(FormLayout formLayout) {
			formLayout.addComponent(dashboardUrl);
		}
		
		@Override
		public void attach(User user) {
			if(user.getDashboardUrl() == null || user.getDashboardUrl().isEmpty()) {
				dashboardUrl.setValue(BasicConstants.infodocBasicDefaultDashboardUrl);
			} else {
				dashboardUrl.setValue(user.getDashboardUrl());
			}
		}
		
		@Override
		public void updateButtonClick(User user) {
			user.setDashboardUrl(dashboardUrl.getValue().toString());
		}
		
	};
	
	private MDIWindow mdiWindow;
	private User user;
	private Map<MenuItem, Activity> activityMap = new HashMap<MenuItem, Activity>();
	private Map<MenuItem, Process> processMap = new HashMap<MenuItem, Process>();

	@Override
	public void init() {
		Utils.loadProperties("basic-ui.properties", "basic-ui");
		Utils.loadProperties("basic-configuration.properties", "basic-configuration");
		addJavaClasses();
		addOptions();
	}
	
	public void addJavaClasses() {
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.TextField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.TextAreaField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.BooleanField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.DateField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.FileField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.SingleClassificationField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.MultipleClassificationsField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.SingleProcessInstance.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.MultipleProcessInstances.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.SingleAssignedByMeProcessInstanceField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.MultipleAssignedByMeProcessInstancesField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.SingleUserField.class.getName());
		PropertyFieldFactory.getJavaClasses().add(infodoc.basic.field.SingleUserGroupField.class.getName());
		
		JavaReportFieldFactory.getJavaClasses().add(CapacityReport.class.getName());
		JavaReportFieldFactory.getJavaClasses().add(FinalizedProcessesReport.class.getName());
		JavaReportFieldFactory.getJavaClasses().add(PendingProcessesReport.class.getName());
		JavaReportFieldFactory.getJavaClasses().add(PerformanceByUserGroupReport.class.getName());
		JavaReportFieldFactory.getJavaClasses().add(PerformanceByUserReport.class.getName());
		JavaReportFieldFactory.getJavaClasses().add(ActivityVolumeByUserGroup.class.getName());
		JavaReportFieldFactory.getJavaClasses().add(ActivityVolumeByUser.class.getName());
		
		ActivityFieldFactory.getJavaClasses().add(Create.class.getName());
		ActivityFieldFactory.getJavaClasses().add(Update.class.getName());
		ActivityFieldFactory.getJavaClasses().add(AssignSingleUser.class.getName());
		ActivityFieldFactory.getJavaClasses().add(AssignMulipleUsers.class.getName());
		ActivityFieldFactory.getJavaClasses().add(AssignSingleUserGroup.class.getName());
		ActivityFieldFactory.getJavaClasses().add(AssignMultipleUserGroups.class.getName());
		ActivityFieldFactory.getJavaClasses().add(Unassign.class.getName());
		ActivityFieldFactory.getJavaClasses().add(Transfer.class.getName());

		ValidationFieldFactory.getJavaClasses().add(Email.class.getName());
		ValidationFieldFactory.getJavaClasses().add(LongNumber.class.getName());
		ValidationFieldFactory.getJavaClasses().add(MinStringLength.class.getName());
		ValidationFieldFactory.getJavaClasses().add(MaxStringLength.class.getName());
		ValidationFieldFactory.getJavaClasses().add(Regexp.class.getName());
	}
	
	public void addOptions() {
		OptionsWindow.getListeners().add(listener);
	}
	
	@Override
	public boolean userCanAccess(enterpriseapp.hibernate.dto.User user) {
		return ((User) user).getUserGroup().getAccessBasicModule();
	}

	@Override
	public void add(MDIWindow ventanaPrincipal, enterpriseapp.hibernate.dto.User user) {
		this.mdiWindow = ventanaPrincipal;
		this.user = (User) user;
		
		List<Process> processes = InfodocContainerFactory.getProcessContainer().findByDisabled(false);
		
		for(Process process : processes) {
			addProcess(process);
		}
		
		ventanaPrincipal.addWorkbenchContent(new Dashboard(), null, new ThemeResource(InfodocTheme.iconHome), false, false);
		addSummary();
	}

	public void addSummary() {
		SummaryComponent summaryComponent = new SummaryComponent();
		summaryComponent.setWidth("340px");
		
		Toolbox toolbox = new Toolbox();
		toolbox.setOrientation(ORIENTATION.LEFT_CENTER);
		toolbox.addComponent(summaryComponent);
		toolbox.setAnimationTime(100);
		toolbox.setOverflowSize(6);
		toolbox.setToolboxVisible(true);
		
		mdiWindow.getWorkbenchAreaLayout().addComponent(toolbox);
	}
	
	public void addProcess(final Process process) {
		MenuItem processMenuItem = mdiWindow.getMenuBar().addItem(process.getName(), new ThemeResource(process.getIcon()), null);
		List<Activity> activities = InfodocContainerFactory.getActivityContainer().findByUserIdAndProcessId(user.getId(), process.getId());
		
		if(activities != null && activities.size() > 0) {
			for(Activity activity : activities) {
				MenuItem menuItem = addMenuItem(processMenuItem, activity.getName(), ActivityExecutorHelper.getIcon(activity, user), this, true);
				activityMap.put(menuItem, activity);
				processMap.put(menuItem, process);
			}
		}
		
		addSeparator(processMenuItem);
		addSearchOption(process, processMenuItem);
		addReports(process, processMenuItem);
		
		if(processMenuItem != null) {
			if(!processMenuItem.hasChildren()) {
				mdiWindow.getMenuBar().removeItem(processMenuItem);
			} else {
				removeEndingSeparator(processMenuItem);
			}
		}
	}

	public void addSearchOption(final Process process, MenuItem menuItem) {
		addMenuItem(menuItem, BasicConstants.uiSearch, new ThemeResource(InfodocTheme.iconSearch), new Command() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				ProcessInstanceSearchComponent searchComponent = new ProcessInstanceSearchComponent(process, user);
				searchComponent.setSizeFull();
				mdiWindow.addWorkbenchContent(searchComponent, BasicConstants.uiSearch + " (" + process.getName() + ")", null, true, false);
			}
			
		}, user.getUserGroup().getAccessSearchProcess().contains(process));
	}
	
	public void addReports(final Process process, MenuItem menuItem) {
		MenuItem reportsMenuItem = addMenuItem(menuItem, BasicConstants.uiJavaReports, new ThemeResource(InfodocTheme.iconReports), null, user.getUserGroup().getJavaReports() != null && !user.getUserGroup().getJavaReports().isEmpty());
		
		if(reportsMenuItem != null) {
			for(JavaReport report : user.getUserGroup().getJavaReports()) {
				if(report.getProcess().equals(process)) {
					try {
						Constructor<?> constructor = Class.forName(report.getJavaClass()).getConstructor(Process.class, JavaReport.class);
						InfodocReport component = (InfodocReport) constructor.newInstance(process, report);
						addReport(reportsMenuItem, report.getName(), new ThemeResource(report.getIcon()), component, process, true);
						
					} catch (Exception e) {
						logger.error("Error creating java report " + report.getJavaClass(), e);
						throw new RuntimeException(e);
					}
				}
			}
			
			addSeparator(reportsMenuItem);
			
			List<HqlReport> hqlReports = InfodocContainerFactory.getHqlReportContainer().listByUserGroupIdAndProcessId(user.getUserGroup().getId(), process.getId());
			
			for(HqlReport report : hqlReports) {
				addReport(reportsMenuItem, report.getName(), new ThemeResource(InfodocTheme.iconReport), new HqlReportViewer(report), process, true);
			}
			
			if(reportsMenuItem.getChildren() == null || reportsMenuItem.getChildren().isEmpty()) {
				menuItem.removeChild(reportsMenuItem);
			} else {
				removeEndingSeparator(reportsMenuItem);
			}
		}
	}

	private void addReport(MenuItem menuItem, final String title, final Resource icon, final InfodocReport report, final Process process, boolean canUserAccess) {
		addMenuItem(menuItem, title, icon, new Command() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				report.setSizeFull();
				mdiWindow.addWorkbenchContent(report, title + " (" + process.getName() + ")", null, true, false);
			}
			
		}, canUserAccess);
	}

	@Override
	public void menuSelected(MenuItem selectedItem) {
		Activity activity = activityMap.get(selectedItem);
		Process process = processMap.get(selectedItem);
		
		activity = InfodocContainerFactory.getActivityContainer().getEntity(activity.getId());
		process = InfodocContainerFactory.getProcessContainer().getEntity(process.getId());
		
		ActivityExecutorHelper.addExecuutorComponent(mdiWindow, activity, process, user);
	}

}
