package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.dto.User;
import infodoc.core.dto.UserGroup;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;

import java.util.List;

import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

public class SingleUserGroupField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.STRING;
	}
	
	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, User user) {
		Select field = new Select();
		List<UserGroup> userGroups = InfodocContainerFactory.getUserGroupContainer().listAll();
		
		for(UserGroup g : userGroups) {
			field.addItem(g.toString());
		}
		
		return field;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, User user) {
		return getField(property, form, activity, formDto, user);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpSingleUserGroupField;
	}

}
