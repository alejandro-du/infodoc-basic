package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ClassificationValueContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Property;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import com.vaadin.Application;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window.Notification;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.ui.crud.EntitySetField;

public abstract class AbstractClassifications implements FieldFactory {

	protected Long classificationId;
	
	public abstract EntitySetField getEntitySetField(ClassificationValueContainer classifacationValueContainer);
	
	@Override
	public FieldType getType() {
		return FieldType.CLASSIFICATION_VALUES;
	}
	
	@Override
	public Field getField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application) {
		ClassificationValueContainer classificationValueContainer = InfodocContainerFactory.getClassificationValueContainer();
		
		if(property.getParameter() == null || property.getParameter().trim().isEmpty()) {
			EnterpriseApplication.getInstance().getMainWindow().showNotification(BasicConstants.uiErrorIncorrectParam(property.toString()), Notification.TYPE_ERROR_MESSAGE);
			throw new RuntimeException("Incorrect property parameter (property: " + property + ", parameter: " + property.getParameter() + ")");
		}
		
		String[] params = property.getParameter().split(",");
		boolean allowNew = false;
		
		for(String param : params) {
			param = param.trim();
			
			if(param.toLowerCase().equals("allowNew".toLowerCase())) {
				allowNew = true && !form.isShowShearchProperties(); 
			} else {
				classificationValueContainer.addContainerFilter("classification.id", param, false, true);
				classificationId = new Long(param);
			}
		}
		
		EntitySetField entitySetField = getEntitySetField(classificationValueContainer);
		entitySetField.getSelect().setNewItemsAllowed(allowNew);
		
		return entitySetField;
	}

	@Override
	public Field getSearchField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application) {
		return getField(property, form, activity, process, application);
	}
	
}
