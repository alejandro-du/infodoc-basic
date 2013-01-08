package infodoc.basic.component;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Process;
import infodoc.core.dto.ActivityInstance;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutorHelper;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.processinstance.ProcessInstanceBox;

import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import enterpriseapp.Utils;

public class Dashboard extends CustomComponent {
	
	private static final long serialVersionUID = 1L;
	
	private HorizontalSplitPanel split = new HorizontalSplitPanel();
	private Accordion accordion = new Accordion();
	private VerticalLayout myInstancesLayout = new VerticalLayout();
	private VerticalLayout activityHistoryLayout = new VerticalLayout();
	private OptionGroup processSelect;

	private User user;
	private int numberOfLastActivities = BasicConstants.infodocBasicNumberOfLastActivities;

	public Dashboard() {
		split.setSplitPosition(50);
		setCompositionRoot(split);
	}
	
	@Override
	public void attach() {
		user = (User) getApplication().getUser();
		
		myInstancesLayout.setMargin(true);
		myInstancesLayout.setSpacing(true);
		
		activityHistoryLayout.setMargin(true);
		activityHistoryLayout.setSpacing(true);
		
		accordion.setSizeFull();
		accordion.setStyleName(InfodocTheme.ACCORDION_OPAQUE);
		
		if(user.getUserGroup().getAccessBasicModule()) {
			accordion.addTab(myInstancesLayout, BasicConstants.uiMyProcessInstances);
		}
		
		if(user.getUserGroup().getAccessLastActivityInstances()) {
			accordion.addTab(activityHistoryLayout, BasicConstants.uiLastActivityInstances);
		}
		
		if(accordion.getComponentCount() == 0) {
			split.setSplitPosition(0);
		} else {
			split.setFirstComponent(accordion);
		}
		
		processSelect = new OptionGroup();
		processSelect.setStyleName("horizontal");
		processSelect.setImmediate(true);
		processSelect.addListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				updateMyInstances();
			}
		});
				
		updateMyInstances();
		
		if(user.getUserGroup().getAccessLastActivityInstances()) {
			updateActivityHistory(numberOfLastActivities);
		}
		
		split.setSecondComponent(getEmbeddedWebPage());
	}
	
	public void updateMyInstances() {
		updateProcessFilter();
		
		myInstancesLayout.removeAllComponents();
		
		Button refreshButton = new Button();
		refreshButton.setDescription(BasicConstants.uiRefresh);
		refreshButton.setIcon(new ThemeResource(InfodocTheme.iconUpdate));
		
		refreshButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				updateMyInstances();
			}
		});
		
		HorizontalLayout toolBarLayout = new HorizontalLayout();
		toolBarLayout.setWidth("100%");
		
		toolBarLayout.addComponent(processSelect);
		toolBarLayout.addComponent(refreshButton);
		toolBarLayout.setComponentAlignment(refreshButton, Alignment.TOP_RIGHT);
		
		myInstancesLayout.addComponent(toolBarLayout);
		
		List<Process> processes = InfodocContainerFactory.getProcessContainer().findByUserId(user.getId());
		
		for(Process process : processes) {
			List<ProcessInstance> instances = InfodocContainerFactory.getProcessInstanceContainer().findMyProcessInstances(user.getId(), process.getId());
			
			if(instances.isEmpty()) {
				myInstancesLayout.addComponent(new Label(BasicConstants.uiEmptyProcessInstancesList, Label.CONTENT_XHTML));
			} else {
				for(ProcessInstance instance : instances) {
					addProcessInstance(instance);
				}
			}
		}
		
	}
	
	public void updateProcessFilter() {
		List<Process> processes = InfodocContainerFactory.getProcessContainer().findByUserId(user.getId());
		
		if(processes.size() > 1) {
			for(Process process : processes) {
				int total = InfodocContainerFactory.getProcessInstanceContainer().findMyProcessInstances(user.getId(), process.getId()).size();

				processSelect.addItem(process);
				processSelect.setItemCaption(process, process.getName() + " (" + total + ")");
			}
		} else {
			processSelect.setVisible(false);
		}
	}
	
	public void addProcessInstance(final ProcessInstance instance) {
		addActivityInstance(InfodocContainerFactory.getProcessInstanceContainer().getLastActivityInstance(instance), myInstancesLayout, false, true, false, true, true);
	}
	
	public void updateActivityHistory(final int count) {
		activityHistoryLayout.removeAllComponents();
		
		Button refreshButton = new Button();
		refreshButton.setDescription(BasicConstants.uiRefresh);
		refreshButton.setIcon(new ThemeResource(InfodocTheme.iconUpdate));
		
		refreshButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				numberOfLastActivities = BasicConstants.infodocBasicNumberOfLastActivities;
				updateActivityHistory(numberOfLastActivities);
			}
		});
		
		activityHistoryLayout.addComponent(refreshButton);
		activityHistoryLayout.setComponentAlignment(refreshButton, Alignment.TOP_RIGHT);
		
		List<ActivityInstance> activityInstances = InfodocContainerFactory.getActivityInstanceContainer().findLast(count);
		
		if(activityInstances.isEmpty()) {
			activityHistoryLayout.addComponent(new Label(BasicConstants.uiEmptyActivitiesList, Label.CONTENT_XHTML));
		} else {
			for(ActivityInstance instance : activityInstances) {
				addActivityInstance(instance, activityHistoryLayout, true, false, true, true, false);
			}
		}
		
		Button showMoreButton = new Button(BasicConstants.uiSeeMore);
		showMoreButton.setStyleName(InfodocTheme.BUTTON_LINK);
		
		showMoreButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				numberOfLastActivities += BasicConstants.infodocBasicNumberOfLastActivities;
				updateActivityHistory(numberOfLastActivities);
			}
		});
		
		activityHistoryLayout.addComponent(showMoreButton);
		activityHistoryLayout.setComponentAlignment(showMoreButton, Alignment.BOTTOM_CENTER);
	}
	
	public void addActivityInstance(final ActivityInstance instance, Layout layout, boolean showUser, boolean showActions, boolean showActivity, boolean showDate, boolean bold) {
		Button button = new Button(instance.getProcessInstance().toString());
		button.setIcon(new ThemeResource(instance.getProcessInstance().getProcess().getIcon()));
		
		if(bold) {
			button.setStyleName(InfodocTheme.BUTTON_LINK_BOLD);
		} else {
			button.setStyleName(InfodocTheme.BUTTON_LINK);
		}
		
		button.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void buttonClick(ClickEvent event) {
				showProcessInstance(instance.getProcessInstance());
			}
			
		});
		
		HorizontalLayout activityInstanceLayout = new HorizontalLayout();
		activityInstanceLayout.setSpacing(true);
		
		if(showUser) {
			Label userLabel = new Label("<span class='" + InfodocTheme.CLASS_INITIAL_USERS + "'>" + instance.getUser().getLogin() + "<span>", Label.CONTENT_XHTML);
			
			if(instance.getComments() != null && !instance.getComments().isEmpty()) {
				userLabel.setValue(userLabel.getValue().toString() + ": <span class='" + InfodocTheme.CLASS_COMMENTS + "'>&quot;" + instance.getComments().trim() + "&quot;</span>");
			}
			
			activityInstanceLayout.addComponent(userLabel);
		}
		
		activityInstanceLayout.addComponent(button);
		
		if(showActivity) {
			activityInstanceLayout.addComponent(new Embedded(null, ActivityExecutorHelper.getIcon(instance.getActivity(), user)));
			activityInstanceLayout.addComponent(new Label("<b>" + instance.getActivity().getName() + "</b>", Label.CONTENT_XHTML));
		}
		
		if(showDate) {
			Label dateLabel = new Label("<small>" + Utils.dateTimeToString(instance.getExecutionTime()) + "</small>", Label.CONTENT_XHTML);
			activityInstanceLayout.addComponent(dateLabel);
		}
		
		if(showActions) {
			Panel panel = new Panel();
			panel.addComponent(activityInstanceLayout);
			panel.addComponent(ActivityExecutorHelper.getAvailableActivitiesLayout(instance.getProcessInstance(), user));
			
			layout.addComponent(panel);
			
		} else {
			layout.addComponent(activityInstanceLayout);
		}
	}
	
	public void showProcessInstance(ProcessInstance instance) {
		instance = InfodocContainerFactory.getProcessInstanceContainer().getEntity(instance.getId());
		
		Window window = new Window(instance.getProcess().getName());
		window.setWidth("680px");
		window.setHeight("480px");
		window.setModal(true);
		window.addComponent(new ProcessInstanceBox(instance));
		
		getApplication().getMainWindow().addWindow(window);
	}

	public Embedded getEmbeddedWebPage() {
		String url = user.getDashboardUrl();
		
		if(url == null || url.isEmpty()) {
			url = BasicConstants.infodocBasicDefaultDashboardUrl;
		}
		
		Embedded page = new Embedded("", new ExternalResource(url));
		page.setType(Embedded.TYPE_BROWSER);
		page.setSizeFull();
		
		return page;
	}

}
