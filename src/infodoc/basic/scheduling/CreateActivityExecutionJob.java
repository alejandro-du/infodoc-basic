package infodoc.basic.scheduling;

import infodoc.basic.activity.Create;
import infodoc.core.container.CaseContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.PropertyValueContainer;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Case;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityExecutorHelper;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Field;

import enterpriseapp.job.TransactionalJob;

public class CreateActivityExecutionJob extends TransactionalJob {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateActivityExecutionJob.class);
	
	private Long activityId;
	
	private Long caseId;
	
	private Long userId;
	
	@Override
	public void executeJob(JobExecutionContext context) throws JobExecutionException {
		CaseContainer caseContainer = InfodocContainerFactory.getCaseContainer();
		Case caseDto = caseContainer.getEntity(caseId);
		
		if(caseContainer.isPending(caseDto)) {
			logger.info("Executing job for activity " + activityId + " and case " +  caseId);
			executeActivity(caseDto, activityId);
			
		} else {
			logger.info("Skiping execution of job for activity " + activityId + " and case " +  caseId);
		}
	}

	public void executeActivity(Case caseDto, Long activityId2) {
		User user = InfodocContainerFactory.getUserContainer().getEntity(userId);
		
		Activity activity = InfodocContainerFactory.getActivityContainer().getEntity(activityId);
		Create create = (Create) ActivityExecutorHelper.getActivityExecutorComponent(activity, user);
		create.initLayout();
		create.attach();
		create.getCaseForm().attach();
		
		PropertyValueContainer propertyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
		
		for(PropertyValue propertyValue : caseDto.getPropertyValues()) {
			Field field = create.getCaseForm().getField(propertyValue.getProperty().getName());
			
			if(field != null) {
				field.setValue(propertyValueContainer.getValue(propertyValue));
			}
		}
		
		create.execute();
	}
	
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public void setCaseId(Long caseId) {
		this.caseId = caseId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
