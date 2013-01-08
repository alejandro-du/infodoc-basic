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

import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Select;

public class PerformanceByUserGroupReport extends AbstractActivityInstancesListReport {

	private static final long serialVersionUID = 1L;

	private Select dateResolutionSelect = new Select(BasicConstants.uiDateResolution);

	public PerformanceByUserGroupReport(Process process, JavaReport report) {
		super(process, report);
	}

	@Override
	public void configureColumn(String property, AbstractColumn column, DynamicReportBuilder reportBuilder) {
		// override to avoid showing totals
	}
	
	@Override
	public String getFirstColumnPropertyName() {
		return "userGroup";
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
		Long time = activityInstanceContainer.getAverageTimeByActivityIdAndUserGroupId(activityId, userGroup.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
		
		int resolution = (Integer) dateResolutionSelect.getValue();
		
		if(resolution == DateField.RESOLUTION_YEAR) {
			time /= (60 * 60 * 24 * 30 * 12);
		} else if(resolution == DateField.RESOLUTION_MONTH) {
			time /= (60 * 60 * 24 * 30);
		} else if(resolution == DateField.RESOLUTION_DAY) {
			time /= (60 * 60 * 24);
		} else if(resolution == DateField.RESOLUTION_HOUR) {
			time /= (60 * 60);
		} else if(resolution == DateField.RESOLUTION_MIN) {
			time /= 60;
		}
		
		return time;
	}
	
	@Override
	public FormLayout getParametersComponent() {
		dateResolutionSelect.addItem(DateField.RESOLUTION_YEAR);
		dateResolutionSelect.addItem(DateField.RESOLUTION_MONTH);
		dateResolutionSelect.addItem(DateField.RESOLUTION_DAY);
		dateResolutionSelect.addItem(DateField.RESOLUTION_HOUR);
		dateResolutionSelect.addItem(DateField.RESOLUTION_MIN);
		dateResolutionSelect.addItem(DateField.RESOLUTION_SEC);
		
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_YEAR, BasicConstants.uiYear);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_MONTH, BasicConstants.uiMonth);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_DAY, BasicConstants.uiDay);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_HOUR, BasicConstants.uiHour);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_MIN, BasicConstants.uiMinute);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_SEC, BasicConstants.uiSecond);
		
		dateResolutionSelect.setValue(DateField.RESOLUTION_HOUR);
		dateResolutionSelect.setNullSelectionAllowed(false);
		
		FormLayout component = super.getParametersComponent();
		component.addComponent(dateResolutionSelect);
		
		return component;
	}

}
