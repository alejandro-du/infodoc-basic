package infodoc.basic.activity;

import infodoc.basic.scheduling.CreateActivityScheduler;
import infodoc.core.dto.Activity;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.User;
import infodoc.core.ui.cases.CaseForm;

import java.text.ParseException;
import java.util.Collection;

import com.vaadin.terminal.UserError;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.hibernate.Db;

public class UpdateAndScheduleCreate extends Update implements Schedulable {

	private static final long serialVersionUID = 1L;
	
	protected Long scheduleActivityId;
	
	protected Long cronExpressionPropertyId;
	
	public UpdateAndScheduleCreate() {
		super();
	}

	public UpdateAndScheduleCreate(Activity activity, User user) {
		super(activity, user);
	}
	
	@Override
	protected int parseOther(String[] params, int startPosition) {
		if(params.length >= startPosition + 2 && "scheduleActivity".toLowerCase().equals(params[startPosition].toLowerCase().trim())) {
			scheduleActivityId = Long.parseLong(params[startPosition + 1].trim());
			return startPosition + 2;
		}
		
		if(params.length >= startPosition + 2 && "cronExpressionProperty".toLowerCase().equals(params[startPosition].toLowerCase().trim())) {
			cronExpressionPropertyId = Long.parseLong(params[startPosition + 1].trim());
			return startPosition + 2;
		}
		
		return super.parseOther(params, startPosition);
	}

	@Override
	public boolean afterSaveCase(CaseForm form) {
		try {
			CreateActivityScheduler.schedule(
				form.getCase().getId(),
				scheduleActivityId,
				getCronExpression(form.getPropertyValues()),
				((User) EnterpriseApplication.getInstance().getUser()).getId()
			);
		} catch (ParseException e) {
			Db.rollBackTransaction();
			form.setComponentError(new UserError(e.getMessage()));
			throw new RuntimeException(e);
		}
		
		return true;
	}
	
	public String getCronExpression(Collection<PropertyValue> propertyValues) {
		String cronExpression = null;
		
		for(PropertyValue pv : propertyValues) {
			if(cronExpressionPropertyId.equals(pv.getProperty().getId())) {
				cronExpression = pv.getStringValue();
				break;
			}
		}
		
		return cronExpression;
	}

	public Long getScheduleActivityId() {
		return scheduleActivityId;
	}

}
