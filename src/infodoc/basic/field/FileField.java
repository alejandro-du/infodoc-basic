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
import com.vaadin.ui.Upload.FinishedEvent;

import enterpriseapp.ui.DownloadField;

public class FileField implements FieldFactory {

	@Override
	public FieldType getType() {
		return FieldType.BYTE_ARRAY;
	}
	
	@Override
	public Field getField(final Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		DownloadField field = new DownloadField(application) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void uploadFinishedEvent(FinishedEvent event) {
				super.uploadFinishedEvent(event);
				property.setParameter(getFileName());
			}
		};
		
		field.setReadOnly(false);
		
		return field;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		return getField(property, form, activity, formDto, application);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpFileField;
	}

}
