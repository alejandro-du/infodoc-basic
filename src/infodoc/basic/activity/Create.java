package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutor;
import infodoc.core.ui.cases.CaseForm;
import infodoc.core.ui.cases.CasesList;
import infodoc.core.ui.common.InfodocTheme;

import java.util.HashSet;
import java.util.List;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import enterpriseapp.hibernate.Db;

public class Create extends ActivityExecutor implements ClickListener {

	private static final long serialVersionUID = 1L;
	
	protected Button createButton;
	protected CaseForm form;
	protected CasesList instancesListComponent;
	protected CheckBox sendToCheckBox;
	protected boolean useNumeration = true;
	
	public Create() {
		super();
	}
	
	public Create(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public void initLayout() {
		Label leftSpacer = new Label();
		leftSpacer.setWidth("100%");
		
		createButton = new Button(getActivity().getName());
		createButton.addListener(this);
		
		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.addComponent(leftSpacer);
		footer.setExpandRatio(leftSpacer, 1);
		footer.addComponent(createButton);
		footer.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);
		
		Case newCase = new Case();
		newCase.setForm(getForm());
		
		form = new CaseForm(newCase, getActivity(), getUser(), true);
		form.setImmediate(true);
		form.setWidth("100%");
		form.setFooter(footer);
		
		Panel panel = new Panel(BasicConstants.uiForm);
		panel.setWidth("100%");
		panel.addComponent(form);
		
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.setMargin(true);
		leftLayout.addComponent(panel);
		
		instancesListComponent = new CasesList();
		
		VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setMargin(true);
		rightLayout.addComponent(instancesListComponent);
		
		HorizontalSplitPanel layout = new HorizontalSplitPanel();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setFirstComponent(leftLayout);
		layout.setSecondComponent(rightLayout);
		
		setCompositionRoot(layout);
	}
	
	@Override
	public void attach() {
		super.attach();
		addSendTo();
	}

	private void addSendTo() {
		if(getActivity().getParameter() != null && !getActivity().getParameter().isEmpty()) {
			HashSet<User> users = new HashSet<User>();
			HashSet<UserGroup> groups = new HashSet<UserGroup>();
			parseParams(getActivity().getParameter(), users, groups);
			
			String recipients = users.toString().replace("[", "").replace("]", "");
			
			if(!recipients.isEmpty() && !groups.isEmpty()) {
				recipients += ", ";
			}
			
			recipients += groups.toString().replace("[", "").replace("]", "");
			
			if(!recipients.isEmpty()) {
				sendToCheckBox = new CheckBox(BasicConstants.uiAssignTo(recipients));
				sendToCheckBox.setValue(true);
				sendToCheckBox.setIcon(new ThemeResource(InfodocTheme.iconUserGroup));
				form.addField("sendTo", sendToCheckBox);
			}
		}
	}
	
	public void parseParams(String parameter, HashSet<User> users, HashSet<UserGroup> userGroups) {
		String[] params = parameter.split(",");
		Boolean assignUsers = false;
		Boolean assignUserGroups = false;
		Boolean dontAssign = false;
		
		for(int i = 0; i < params.length; i++) {
			String param = params[i].trim();
			
			if(param.toLowerCase().equals("assignUsers".toLowerCase())) {
				assignUsers = true;
				assignUserGroups = false;
				dontAssign = false;
				
			} else if(param.toLowerCase().equals("assignGroups".toLowerCase())) {
				assignUserGroups = true;
				assignUsers = false;
				dontAssign = false;
				
			} else if(param.toLowerCase().equals("dontAssign".toLowerCase())) {
				dontAssign = true;
				assignUsers = false;
				assignUserGroups = false;
				
			} else {
				if(assignUsers) {
					User u = InfodocContainerFactory.getUserContainer().getEntity(new Long(param));
					
					if(u == null) {
						throw new RuntimeException("User " + getActivity().getParameter() + ", configured in parameter of activity " + getActivity() + ", does not exist.");
					}
					
					users.add(u);
					
				} else if(assignUserGroups) {
					UserGroup g = InfodocContainerFactory.getUserGroupContainer().getEntity(new Long(param));
					
					if(g == null) {
						throw new RuntimeException("User group " + getActivity().getParameter() + ", configured in parameter of activity " + getActivity() + ", does not exist.");
					}
					
					userGroups.add(g);
					
				} else if(dontAssign && users.isEmpty() && userGroups.isEmpty()) {
					
				} else {
					throw new RuntimeException("Wrong parameter for activity " + getActivity().toString() + ": " + parameter);
				}
			}
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(createButton.equals(event.getButton())) {
			form.setComponentError(null);
			
			try {
				form.validate();
				List<PropertyValue> propertyValuesToSave = form.getPropertyValues();
				
				if(!beforeSaveCase(propertyValuesToSave)) {
					return;
				}
				
				Case caseDto = saveCase(propertyValuesToSave);
				
				if(!afterSaveCase(propertyValuesToSave)) {
					return;
				}
				
				Db.commitTransaction();
				caseDto = InfodocContainerFactory.getCaseContainer().getEntity(caseDto.getId());
				form.clear();
				addSendTo();
				instancesListComponent.addAtBeginning(caseDto);
				getWindow().showNotification(BasicConstants.uiActivityExecuted);
				
			} catch (InvalidValueException e) {
				form.setComponentError(new UserError(e.getMessage()));
			}
		}
	}
	
	public boolean beforeSaveCase(List<PropertyValue> propertyValuesToSave) {
		return true;
	}

	public boolean afterSaveCase(List<PropertyValue> propertyValuesToSave) {
		return true;
	}

	public Case saveCase(List<PropertyValue> propertyValuesToSave) {
		HashSet<User> users = new HashSet<User>();
		HashSet<UserGroup> userGroups = new HashSet<UserGroup>();
		
		if(sendToCheckBox != null && sendToCheckBox.booleanValue()) {
			if(getActivity().getParameter() != null && !getActivity().getParameter().trim().isEmpty()) {
				parseParams(getActivity().getParameter(), users, userGroups);
			}
		} else {
			users.add(getUser());
		}
		
		if(users.isEmpty()) {
			users = null;
		}
		
		if(userGroups.isEmpty()) {
			userGroups = null;
		}
		
		return InfodocContainerFactory.getCaseContainer().saveInstace(form.getCase(), propertyValuesToSave, getNewActivityInstance(form.getCase(), form.getComments(), users, userGroups), useNumeration);
	}
	
	@Override
	public Long countAvailableCases() {
		return null;
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpCreateActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityCreate);
	}

}
