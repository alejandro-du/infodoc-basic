package infodoc.basic.report;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.ActivityInstanceContainer;
import infodoc.core.dto.UserGroup;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Process;

import java.util.Collection;
import java.util.Date;

import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

public class ActivityVolumeByUserGroup extends AbstractActivityInstancesListReport {

	private static final long serialVersionUID = 1L;
	
	public ActivityVolumeByUserGroup(Process process, JavaReport report) {
		super(process, report);
	}

	@Override
	public void configureColumn(String property, AbstractColumn column, DynamicReportBuilder reportBuilder) {
		// override to avoid showing totals
	}
	
	@Override
	public String getFirstColumnPropertyName() {
		return "group";
	}

	@Override
	public Class<?> getFirstColumnPropertyClass() {
		return String.class;
	}

	@Override
	public String getFirstColumnTitle() {
		return BasicConstants.uiUserGroup;
	}

	@Override
	public Collection<?> getRowObjects() {
		return InfodocContainerFactory.getUserGroupContainer().listAll();
	}

	@Override
	public Object getFirstColumnValue(Object rowObject) {
		UserGroup userGroup = (UserGroup) rowObject;
		return userGroup.getName();
	}
	
	@Override
	public Long getCount(Object rowObject, Object nextRowObject, Long activityId) {
		UserGroup userGroup = (UserGroup) rowObject;
		ActivityInstanceContainer activityInstanceContainer = InfodocContainerFactory.getActivityInstanceContainer();
		
		return activityInstanceContainer.countByActivityIdAndGroupId(activityId, userGroup.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
	}

}
