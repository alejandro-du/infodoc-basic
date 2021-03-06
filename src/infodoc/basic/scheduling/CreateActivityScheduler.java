package infodoc.basic.scheduling;

import infodoc.basic.activity.Schedulable;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Case;
import infodoc.core.ui.activity.ActivityExecutorHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.Utils;

public class CreateActivityScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateActivityScheduler.class);
	
	public static void schedulePending() throws ParseException {
		try {
			List<Activity> activities = InfodocContainerFactory.getActivityContainer().listAll();
		
			for(Activity activity : activities) {
				Class<?> clazz = Class.forName(activity.getJavaClass());
				
				if(Schedulable.class.isAssignableFrom(clazz)) {
					
					Schedulable schedulable = (Schedulable) ActivityExecutorHelper.getActivityExecutorComponent(activity, null);
					schedulable.parseParams();
					
					List<Case> cases = InfodocContainerFactory.getCaseContainer().findByLastActivityId(activity.getId());
					
					for(Case caseDto : cases) {
						String cronExpression = schedulable.getCronExpression(caseDto.getPropertyValues());
						Long scheduleActivityId = schedulable.getScheduleActivityId();
						schedule(caseDto.getId(), scheduleActivityId, cronExpression, caseDto.getActivityInstances().iterator().next().getUser().getId());
					}
					
				}
			}
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void schedule(Long caseId, Long scheduleActivityId, String cronExpression, Long userId) throws ParseException {
		try {
			logger.info("Scheduling job for case " + caseId + " and activity " + scheduleActivityId + " (" + cronExpression + ")");
			
			JobKey key = new JobKey(CreateActivityExecutionJob.class.getSimpleName() + "_" + caseId + "_" + scheduleActivityId);
			
			if(EnterpriseApplication.getScheduler().checkExists(key)) {
				EnterpriseApplication.getScheduler().deleteJob(key);
				logger.info("Job for case " + caseId + " and activity " + scheduleActivityId + " (" + cronExpression + ") deleted");
			}
			
			JobDetail jobDetail = JobBuilder.newJob(CreateActivityExecutionJob.class)
					.withIdentity(key)
					.usingJobData("activityId", scheduleActivityId)
					.usingJobData("caseId", caseId)
					.usingJobData("userId", userId)
					.build();
			
			CronTriggerImpl trigger = new CronTriggerImpl();
			trigger.setName(CreateActivityScheduler.class.getSimpleName() + "_" + caseId + "_" + scheduleActivityId);
			trigger.setCronExpression(cronExpression);
		
			Date date = EnterpriseApplication.getScheduler().scheduleJob(jobDetail, trigger);
			logger.info("Job for case " + caseId + " and activity " + scheduleActivityId + "(" + cronExpression + ") starting at " + Utils.dateTimeToString(date));
			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void unschedule(Long caseId, Long scheduleActivityId) {
		try {
			logger.info("Unscheduling job for case " + caseId + " and activity " + scheduleActivityId);
			JobKey key = new JobKey(CreateActivityExecutionJob.class.getSimpleName() + "_" + caseId + "_" + scheduleActivityId);
			EnterpriseApplication.getScheduler().deleteJob(key);
			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

}
