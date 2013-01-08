package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocTheme;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Select;

public class AssignSingleUserGroup extends AbstractAssignUserGroups {

	private static final long serialVersionUID = 1L;
	
	public AssignSingleUserGroup() {
		super();
	}

	public AssignSingleUserGroup(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public AbstractSelect createSelectField() {
		return new Select(BasicConstants.uiUserGroup);
	}

	@Override
	public Set<UserGroup> getUserGroups(AbstractSelect select) {
		HashSet<UserGroup> userGroups = new HashSet<UserGroup>();
		userGroups.add((UserGroup) select.getValue());
		
		return userGroups;
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpAssignSingleUserGroupActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityAssignUserGroup);
	}

}
