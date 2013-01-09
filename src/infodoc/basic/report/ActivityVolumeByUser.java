package infodoc.basic.report;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.ActivityInstanceContainer;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;
import infodoc.core.dto.User;

import java.util.Collection;
import java.util.Date;

public class ActivityVolumeByUser extends AbstractActivityInstancesListReport {

	private static final long serialVersionUID = 1L;
	
	public ActivityVolumeByUser(Form form, JavaReport report) {
		super(form, report);
	}

	@Override
	public String getFirstColumnPropertyName() {
		return "user";
	}

	@Override
	public Class<?> getFirstColumnPropertyClass() {
		return String.class;
	}

	@Override
	public String getFirstColumnTitle() {
		return BasicConstants.uiUser;
	}

	@Override
	public Collection<?> getRowObjects() {
		return InfodocContainerFactory.getUserContainer().listAll();
	}
	
	@Override
	public Object getFirstColumnValue(Object rowObject) {
		User user = (User) rowObject;
		return user.getLogin();
	}

	@Override
	public Long getCount(Object rowObject, Object nextRowObject, Long activityId) {
		User user = (User) rowObject;
		ActivityInstanceContainer activityInstanceContainer = InfodocContainerFactory.getActivityInstanceContainer();
		
		return activityInstanceContainer.countByActivityIdAndUserId(activityId, user.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
	}

}
