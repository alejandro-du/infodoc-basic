package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.common.InfodocTheme;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ListSelect;

public class AssignMulipleUsers extends AbstractAssignUsers {

	private static final long serialVersionUID = 1L;
	
	public AssignMulipleUsers() {
		super();
	}

	public AssignMulipleUsers(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	public AbstractSelect createSelectField() {
		ListSelect select = new ListSelect(BasicConstants.uiUsers);
		select.setMultiSelect(true);
		
		return select;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<User> getUsers(AbstractSelect select) {
		Collection<User> users = (Collection<User>) select.getValue();
		return new HashSet<User>(users);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpAssignMultipleUsersActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityAssignUser);
	}

}
