package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocTheme;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ListSelect;

public class AssignMultipleUserGroups extends AbstractAssignUserGroups {

	private static final long serialVersionUID = 1L;
	
	public AssignMultipleUserGroups() {
		super();
	}

	public AssignMultipleUserGroups(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public AbstractSelect createSelectField() {
		ListSelect select = new ListSelect(BasicConstants.uiUserGroups);
		select.setMultiSelect(true);
		
		return select;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<UserGroup> getUserGroups(AbstractSelect select) {
		Collection<UserGroup> userGroups = (Collection<UserGroup>) select.getValue();
		return new HashSet<UserGroup>(userGroups);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpAssignMultipleUserGroupsActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityAssignUserGroup);
	}

}
