package infodoc.basic.activity;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityListExecutorTemplate;
import infodoc.core.ui.cases.CaseForm;
import infodoc.core.ui.common.InfodocTheme;

import java.util.Set;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect;

public abstract class AbstractAssignUsers extends ActivityListExecutorTemplate {

	private static final long serialVersionUID = 1L;
	
	public AbstractAssignUsers() {
		super();
	}

	public AbstractAssignUsers(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public CaseForm createForm(Case caseDto) {
		AbstractSelect select = createSelectField();
		select.setWidth("100%");
		select.setIcon(new ThemeResource(InfodocTheme.iconUser));
		select.setRequired(true);
		
		for(User user : getUser().getUserGroup().getCanAssignToUsers()) {
			select.addItem(user);
		}
		
		CaseForm form = super.createForm(caseDto);
		form.addField(caseDto, select);
		
		return form;
	}

	public abstract AbstractSelect createSelectField();
	
	public abstract Set<User> getUsers(AbstractSelect select);

	@Override
	public void execute(CaseForm form) {
		Set<User> users = getUsers((AbstractSelect) form.getField(form.getCase()));
		
		if(users != null && !users.isEmpty()) {
			form.validate();
			
			if(getActivity().getParameter() != null && !getActivity().getParameter().trim().isEmpty()) {
				String[] params = getActivity().getParameter().split(",");
				
				for(String param : params) {
					User u = InfodocContainerFactory.getUserContainer().getEntity(Long.parseLong(param.trim()));
					
					if(u == null) {
						throw new RuntimeException("User " + getActivity().getParameter() + ", configured in parameter of activity " + getActivity() + ", does not exist.");
					}
					users.add(u);
				}
			}
			
			Case caseDto = InfodocContainerFactory.getCaseContainer().getEntity(form.getCase().getId());
			InfodocContainerFactory.getCaseContainer().updateInstance(caseDto, form.getPropertyValues(), getNewActivityInstance(caseDto, form.getComments(), users, null));
			update();
		}
		
	}

}
