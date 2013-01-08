package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ProcessInstanceContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Property;
import infodoc.core.dto.Process;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.activity.ActivityExecutor;
import infodoc.core.ui.activity.ActivityExecutorHelper;
import infodoc.core.ui.processinstance.ProcessInstanceForm;

import java.util.Collection;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window.Notification;

import enterpriseapp.EnterpriseApplication;

public abstract class AbstractProcessInstances implements FieldFactory {

	public abstract AbstractSelect getField();
	
	public abstract List<ProcessInstance> getProcessInstances(ProcessInstanceContainer processInstanceContainer, User user, Long activityId);
	
	@Override
	public FieldType getType() {
		return FieldType.PROCESS_INSTANCES;
	}
	
	@Override
	public Field getField(Property property, ProcessInstanceForm form, Activity activity, Process process, final Application application) {
		final AbstractSelect select = getField();
		
		if(property.getParameter() != null && !property.getParameter().trim().isEmpty()) {
			ProcessInstanceContainer processInstanceContainer = InfodocContainerFactory.getProcessInstanceContainer();
			User user = (User) application.getUser();
			String[] params = property.getParameter().split(",");
			Boolean processInstancesInActivities = null;
			
			for(int i = 0; i < params.length; i++) {
				final String param = params[i].trim();
				
				if(param.equals("processInstancesInActivities")) {
					processInstancesInActivities = true;
					
				} else if(param.equals("automaticallyExecuteActivity")) {
					processInstancesInActivities = false;
					
				} else {
					if(processInstancesInActivities == null) {
						EnterpriseApplication.getInstance().getMainWindow().showNotification(BasicConstants.uiErrorIncorrectParam(property.toString()), Notification.TYPE_ERROR_MESSAGE);
						throw new RuntimeException("Incorrect parameter value (property: " + property + ", parameter: " + property.getParameter() + ")");
					}
					
					if(processInstancesInActivities) {
						List<ProcessInstance> instances = getProcessInstances(processInstanceContainer, user, Long.parseLong(param.trim()));
						
						for(ProcessInstance instance : instances) {
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
			Collection<?> processInstances = (Collection<?>) select.getValue();
			
			for(Object instance : processInstances) {
				executeActivity((ProcessInstance) instance, activityId, user);
			}
			
		} else {
			executeActivity((ProcessInstance) select.getValue(), activityId, user);
		}
	}
	
	public void executeActivity(ProcessInstance processInstance, Long activityId, User user) {
		Activity activity = InfodocContainerFactory.getActivityContainer().getEntity(activityId);
		ActivityExecutor executor = ActivityExecutorHelper.getActivityExecutorComponent(activity, user);
		InfodocContainerFactory.getProcessInstanceContainer().updateInstance(processInstance, null, executor.getNewActivityInstance(processInstance, BasicConstants.uiAutomaticallyExecutedActivity, null, null));
	}

	@Override
	public Field getSearchField(Property property, ProcessInstanceForm form, Activity activity, Process process, Application application) {
		return getField(property, form, activity, process, application);
	}
	
}
