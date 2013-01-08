package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Property;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import java.util.List;

import com.vaadin.Application;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

public class SingleUserGroupField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.STRING;
	}
	
	@Override
	public Field getField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application) {
		Select field = new Select();
		List<UserGroup> userGroups = InfodocContainerFactory.getUserGroupContainer().listAll();
		
		for(UserGroup g : userGroups) {
			field.addItem(g.toString());
		}
		
		return field;
	}

	@Override
	public Field getSearchField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application) {
		return getField(property, form, activity, process, application);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpSingleUserGroupField;
	}

}
