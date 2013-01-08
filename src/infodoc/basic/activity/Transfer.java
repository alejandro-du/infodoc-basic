package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ProcessInstanceContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import java.util.HashSet;
import java.util.List;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

import enterpriseapp.ui.window.AuthWindow;

public class Transfer extends Update {

	private static final long serialVersionUID = 1L;
	
	private User theOtherUser;
	
	public Transfer() {
		super();
	}

	public Transfer(Activity activity, User user) {
		super(activity, user);
	}

	@Override
	public void execute(ProcessInstanceForm form) {
		form.validate();
		ProcessInstanceContainer processInstanceContainer = InfodocContainerFactory.getProcessInstanceContainer();
		
		List<ProcessInstance> instances = processInstanceContainer.findAvailableByUserIdAndNextActivityId(theOtherUser.getId(), getActivity().getId());
		
		if(instances.contains(form.getProcessInstance())) {
			HashSet<User> users = new HashSet<User>();
			users.add(theOtherUser);
			ProcessInstance processInstance = processInstanceContainer.getEntity(form.getProcessInstance().getId());
			processInstanceContainer.updateInstance(processInstance, form.getPropertyValues(), getNewActivityInstance(processInstance, form.getComments(), users, null));
		} else {
			throw new InvalidValueException(BasicConstants.uiErrorUserNotAllowedToExecuteActivity);
		}
		
		update();
	}
	
	@Override
	public List<ProcessInstance> getProcessInstances() {
		return InfodocContainerFactory.getProcessInstanceContainer().findAssignedToOtherUserByUserIdAndNextActivityId(getUser().getId(), getActivity().getId());
	}
	
	@Override
	public void executeForAllInstances() {
		showAuthWindow(null);
	}
	
	@Override
	public void executeForOneInstance(ProcessInstanceForm form) {
		showAuthWindow(form);
	}

	public void showAuthWindow(final ProcessInstanceForm form) {
		
		getWindow().addWindow(new AuthWindow(getActivity().getName(), getActivity().getName(), BasicConstants.uiUserLogin, BasicConstants.uiUserPassword, BasicConstants.uiDefaultLogin, BasicConstants.uiDefaultPassword) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void buttonClicked() {
				theOtherUser = InfodocContainerFactory.getUserContainer().getNoDisabledByLoginAndPassword(loginTf.getValue().toString(), passwordTf.getValue().toString());
				
				if(theOtherUser == null) {
					label.setCaption(BasicConstants.uiWrongCredentials);
					panel.setVisible(true);
				} else {
					if(form == null) {
						runExecuteForAllInstancesMethod();
					} else {
						runExecuteForInstanceMethod(form);
					}
					
					this.close();
					theOtherUser = null;
				}
			}
		});
		
	}
	
	public void runExecuteForAllInstancesMethod() {
		super.executeForAllInstances();
	}
	
	public void runExecuteForInstanceMethod(ProcessInstanceForm form) {
		super.executeForInstace(form);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpTransferActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityProcessAssignedWithOtherUser);
	}

}
