package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.dto.User;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

public class SingleUserField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.STRING;
	}
	
	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, User user) {
		List<User> users = new ArrayList<User>();
		
		if(property.getParameter() != null && !property.getParameter().isEmpty()) {
			String[] params = property.getParameter().split(",");
			
			for(String param : params) {
				users.addAll(InfodocContainerFactory.getUserContainer().findByUserGroupId(new Long(param)));
			}
			
		} else {
			users = InfodocContainerFactory.getUserContainer().listAll();
		}
		
		Select field = new Select();
		
		for(User u : users) {
			field.addItem(u.toString());
		}
		
		return field;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, User user) {
		return getField(property, form, activity, formDto, user);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpSingleUserField;
	}

}
