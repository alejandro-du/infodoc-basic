package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.CaseContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;
import infodoc.core.dto.Property;
import infodoc.core.dto.Form;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.activity.ActivityExecutor;
import infodoc.core.ui.activity.ActivityExecutorHelper;
import infodoc.core.ui.cases.CaseForm;

import java.util.Collection;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window.Notification;

import enterpriseapp.EnterpriseApplication;

public abstract class AbstractCases implements FieldFactory {

	public abstract AbstractSelect getField();
	
	public abstract List<Case> getCases(CaseContainer caseDtoContainer, User user, Long activityId);
	
	@Override
	public FieldType getType() {
		return FieldType.PROCESS_INSTANCES;
	}
	
	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, final Application application) {
		final AbstractSelect select = getField();
		
		if(property.getParameter() != null && !property.getParameter().trim().isEmpty()) {
			CaseContainer caseDtoContainer = InfodocContainerFactory.getCaseContainer();
			User user = (User) application.getUser();
			String[] params = property.getParameter().split(",");
			Boolean caseDtosInActivities = null;
			
			for(int i = 0; i < params.length; i++) {
				final String param = params[i].trim();
				
				if(param.equals("casesInActivities")) {
					caseDtosInActivities = true;
					
				} else if(param.equals("automaticallyExecuteActivity")) {
					caseDtosInActivities = false;
					
				} else {
					if(caseDtosInActivities == null) {
						EnterpriseApplication.getInstance().getMainWindow().showNotification(BasicConstants.uiErrorIncorrectParam(property.toString()), Notification.TYPE_ERROR_MESSAGE);
						throw new RuntimeException("Incorrect parameter value (property: " + property + ", parameter: " + property.getParameter() + ")");
					}
					
					if(caseDtosInActivities) {
						List<Case> instances = getCases(caseDtoContainer, user, Long.parseLong(param.trim()));
						
						for(Case instance : instances) {
							if(select.getItem(instance) == null) {
								select.addItem(instance);
							}
						}
					} else {
						form.addListener(new ValueChangeListener() {
							private static final long serialVersionUID = 1L;
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								executeActivity(select, new Long(param), (User) application.getUser());
							}
						});
					}
				}
			}
			
		} else {
			EnterpriseApplication.getInstance().getMainWindow().showNotification(BasicConstants.uiErrorIncorrectParam(property.toString()), Notification.TYPE_ERROR_MESSAGE);
			throw new RuntimeException("Incorrect parameter value (property: " + property + ", parameter: " + property.getParameter() + ")");
		}
		
		return select;
	}

	public void executeActivity(AbstractSelect select, Long activityId, User user) {
		if(select.getValue() == null) {
			return;
		}
		
		if(select.isMultiSelect()) {
			Collection<?> caseDtos = (Collection<?>) select.getValue();
			
			for(Object instance : caseDtos) {
				executeActivity((Case) instance, activityId, user);
			}
			
		} else {
			executeActivity((Case) select.getValue(), activityId, user);
		}
	}
	
	public void executeActivity(Case caseDto, Long activityId, User user) {
		Activity activity = InfodocContainerFactory.getActivityContainer().getEntity(activityId);
		ActivityExecutor executor = ActivityExecutorHelper.getActivityExecutorComponent(activity, user);
		InfodocContainerFactory.getCaseContainer().updateInstance(caseDto, null, executor.getNewActivityInstance(caseDto, BasicConstants.uiAutomaticallyExecutedActivity, null, null));
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		return getField(property, form, activity, formDto, application);
	}
	
}
