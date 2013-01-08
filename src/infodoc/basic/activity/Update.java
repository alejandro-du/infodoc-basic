package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityListExecutorTemplate;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import java.util.HashSet;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

public class Update extends ActivityListExecutorTemplate {

	private static final long serialVersionUID = 1L;
	
	public Update() {
		super();
	}

	public Update(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public void execute(ProcessInstanceForm form) {
		form.validate();
		HashSet<User> users = new HashSet<User>();
		users.add(getUser());
		ProcessInstance processInstance = InfodocContainerFactory.getProcessInstanceContainer().getEntity(form.getProcessInstance().getId());
		InfodocContainerFactory.getProcessInstanceContainer().updateInstance(processInstance, form.getPropertyValues(), getNewActivityInstance(processInstance, form.getComments(), users, null));
		update();
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpUpdateActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityProcess);
	}

}
