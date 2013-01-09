package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.common.InfodocTheme;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Select;

public class AssignSingleUser extends AbstractAssignUsers {

	private static final long serialVersionUID = 1L;
	
	public AssignSingleUser() {
		super();
	}

	public AssignSingleUser(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public AbstractSelect createSelectField() {
		return new Select(BasicConstants.uiUser);
	}

	@Override
	public Set<User> getUsers(AbstractSelect select) {
		HashSet<User> users = new HashSet<User>();
		users.add((User) select.getValue());
		
		return users;
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpAssignSingleUserActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityAssignUser);
	}

}
