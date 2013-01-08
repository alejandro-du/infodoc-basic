package infodoc.basic.component;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutor;
import infodoc.core.ui.activity.ActivityExecutorHelper;
import infodoc.core.ui.comun.InfodocTheme;

import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import enterpriseapp.ui.window.MDIWindow;

public class SummaryComponent extends CustomComponent implements ClickListener {

	private static final long serialVersionUID = 1L;
	
	private VerticalLayout layout = new VerticalLayout();
	private VerticalLayout summaryLayout = new VerticalLayout();
	
	public SummaryComponent() {
		Button refreshButton = new Button();
		refreshButton.setDescription(BasicConstants.uiRefresh);
		refreshButton.setIcon(new ThemeResource(InfodocTheme.iconUpdate));
		refreshButton.addListener(this);
		
		VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.setWidth("100%");
		titleLayout.addComponent(refreshButton);
		titleLayout.setComponentAlignment(refreshButton, Alignment.TOP_RIGHT);
		
		summaryLayout.setSpacing(true);
		summaryLayout.addStyleName(InfodocTheme.CLASS_SUMMARY);
		
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(titleLayout);
		layout.addComponent(summaryLayout);
		
		setCompositionRoot(layout);
	}
	
	@Override
	public void attach() {
		updateSummary();
	}
	
	public boolean addActivity(final Activity activity, final Process process) {
		boolean activityAdded = false;
		
		final User user = (User) getApplication().getUser();
		ActivityExecutor executorComponent = ActivityExecutorHelper.getActivityExecutorComponent(activity, user);
		Long count = executorComponent.countAvailableProcessInstances();
		
		if(count != null) {
			String countStyle = "";
			String activityStyle = "";
			
			if(count > 0) {
				countStyle = "color: red; font-weight: bold;";
				activityStyle = "font-weight: bold;";
			}
			
			Label spacer1 = new Label();
			spacer1.setWidth("15px");
			
			Button executeButton = new Button();
			executeButton.setIcon(executorComponent.getIcon());
			executeButton.addStyleName(InfodocTheme.BUTTON_DEFAULT);
			executeButton.addListener(new ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					ActivityExecutorHelper.addExecuutorComponent((MDIWindow) getApplication().getMainWindow(), activity, process, user);
				}
			});
			
			Label label = new Label("&nbsp; <span style='" + activityStyle + "'>" + activity.getName() + "</span>: <span style='" + countStyle + "'>" + count + "</span> " + BasicConstants.uiMatches, Label.CONTENT_XHTML);
			label.addStyleName(InfodocTheme.COOL_FONT);
			
			HorizontalLayout activityLayout = new HorizontalLayout();
			activityLayout.addComponent(spacer1);
			activityLayout.addComponent(executeButton);
			activityLayout.addComponent(label);
			activityLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
			
			summaryLayout.addComponent(activityLayout);
			activityAdded = true;
		}
			
		return activityAdded;
	}

	public boolean addProcess(Process process) {
		Label name = new Label(process.getName());
		name.addStyleName(InfodocTheme.LABEL_H3);
		name.addStyleName(InfodocTheme.LABEL_COLOR);
		summaryLayout.addComponent(name);
		boolean processAdded = false;
		
		if(process.getActivities() != null) {
			User user = (User) getApplication().getUser();
			user = InfodocContainerFactory.getUserContainer().getEntity(user.getId());
			
			for(Activity activity : process.getActivities()) {
				if(user.getUserGroup().getActivities().contains(activity)) {
					if(addActivity(activity, process)) {
						processAdded = true;
					}
				}
			}
		}
		
		if(!processAdded) {
			summaryLayout.removeComponent(name);
		}
		
		return processAdded;
	}
	
	public void updateSummary() {
		summaryLayout.removeAllComponents();
		List<Process> processes = InfodocContainerFactory.getProcessContainer().findByDisabled(false);
		boolean emptySummary = true;
		
		for(Process process : processes) {
			if(addProcess(process)) {
				emptySummary = false;
			}
		}
		
		if(emptySummary) {
			Label label = new Label(BasicConstants.uiEmptySummary, Label.CONTENT_XHTML);
			label.addStyleName(InfodocTheme.COOL_FONT);
			summaryLayout.addComponent(label);
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		updateSummary();
	}

}
