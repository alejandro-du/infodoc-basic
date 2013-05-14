package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.dto.User;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;

import com.vaadin.ui.Field;

import enterpriseapp.Utils;

public class DateField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.DATE;
	}
	
	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, User user) {
		com.vaadin.ui.DateField field = new com.vaadin.ui.DateField();
		field.setDateFormat(Utils.getDateFormatPattern());
		field.setResolution(com.vaadin.ui.DateField.RESOLUTION_DAY);
		field.setParseErrorMessage(BasicConstants.uiInvalidDateValue);
		
		return field;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, User user) {
		return getField(property, form, activity, formDto, user);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpDateField;
	}

}
