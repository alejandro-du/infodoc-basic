package infodoc.basic.activity;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityListExecutorTemplate;
import infodoc.core.ui.comun.InfodocTheme;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import java.util.Set;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect;

public abstract class AbstractAssignUserGroups extends ActivityListExecutorTemplate {

	private static final long serialVersionUID = 1L;
	
	public AbstractAssignUserGroups() {
		super();
	}

	public AbstractAssignUserGroups(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public ProcessInstanceForm createForm(ProcessInstance processInstance) {
		AbstractSelect select = createSelectField();
		select.setWidth("100%");
		select.setIcon(new ThemeResource(InfodocTheme.iconUserGroup));
		select.setRequired(true);
		
		for(UserGroup userGroup : getUser().getUserGroup().getCanAssignToUserGroups()) {
			select.addItem(userGroup);
		}
		
		ProcessInstanceForm form = super.createForm(processInstance);
		form.addField(processInstance, select);
		
		return form;
	}

	public abstract AbstractSelect createSelectField();
	
	public abstract Set<UserGroup> getUserGroups(AbstractSelect select);

	@Override
	public void execute(ProcessInstanceForm form) {
		Set<UserGroup> userGroups = getUserGroups((AbstractSelect) form.getField(form.getProcessInstance()));
		
		if(userGroups != null && !userGroups.isEmpty()) {
			form.validate();
			
			if(getActivity().getParameter() != null && !getActivity().getParameter().isEmpty()) {
				String[] params = getActivity().getParameter().split(",");
				for(String param : params) {
					UserGroup g = InfodocContainerFactory.getUserGroupContainer().getEntity(new Long(param.trim()));
					if(g == null) {
						throw new RuntimeException("Group " + getActivity().getParameter() + ", configured in parameter of activity " + getActivity() + ", does not exist.");
					}
					userGroups.add(g);
				}
			}
			
			ProcessInstance processInstance = InfodocContainerFactory.getProcessInstanceContainer().getEntity(form.getProcessInstance().getId());
			InfodocContainerFactory.getProcessInstanceContainer().updateInstance(processInstance, form.getPropertyValues(), getNewActivityInstance(processInstance, form.getComments(), null, userGroups));
			update();
		}
		
	}

}
