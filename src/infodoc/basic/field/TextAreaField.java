package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.Property;
import infodoc.core.dto.Form;
import infodoc.core.dto.Activity;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;

import com.vaadin.Application;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class TextAreaField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.STRING;
	}
	
	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		TextArea field = new TextArea();
		field.setNullRepresentation(null);
		return field;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		TextField field = new TextField();
		field.setNullRepresentation(null);
		return field;
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpTextAreaField;
	}

}
