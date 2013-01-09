package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;

import com.vaadin.Application;
import com.vaadin.ui.Field;

import enterpriseapp.Utils;

public class DateField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.DATE;
	}
	
	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		com.vaadin.ui.DateField field = new com.vaadin.ui.DateField();
		field.setDateFormat(Utils.getDateFormatPattern());
		field.setResolution(com.vaadin.ui.DateField.RESOLUTION_DAY);
		field.setParseErrorMessage(BasicConstants.uiInvalidDateValue);
		
		return field;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		return getField(property, form, activity, formDto, application);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpDateField;
	}

}
