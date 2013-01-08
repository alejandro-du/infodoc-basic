package infodoc.basic.report;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Process;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Year;

import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.chart.builder.DJTimeSeriesChartBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;

import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Select;

public class CapacityReport extends AbstractActivityInstancesListReport {

	private static final long serialVersionUID = 1L;
	
	private static final String DATE = "DATE";
	
	private Select dateResolutionSelect = new Select(BasicConstants.uiDateResolution);

	public CapacityReport(Process process, JavaReport report) {
		super(process, report);
	}
	
	@Override
	public String getFirstColumnPropertyName() {
		return DATE;
	}

	@Override
	public Class<?> getFirstColumnPropertyClass() {
		return Date.class;
	}

	@Override
	public String getFirstColumnTitle() {
		return BasicConstants.uiDate;
	}

	@Override
	public Collection<?> getRowObjects() {
		List<Date> dates = InfodocContainerFactory.getActivityInstanceContainer().findDatesHavingActivityInstances(process.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
		LinkedHashSet<Date> rowObjects = new LinkedHashSet<Date>();
		int resolution = (Integer) dateResolutionSelect.getValue();
		
		for(Date date : dates) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			cal.set(Calendar.MILLISECOND, 0);
			
			if(resolution == DateField.RESOLUTION_YEAR) {
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
			} else if(resolution == DateField.RESOLUTION_MONTH) {
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
			} else if(resolution == DateField.RESOLUTION_DAY) {
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
			} else if(resolution == DateField.RESOLUTION_HOUR) {
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
			} else if(resolution == DateField.RESOLUTION_MIN) {
				cal.set(Calendar.SECOND, 0);
			}
			
			rowObjects.add(cal.getTime());
		}
		
		return rowObjects;
	}

	@Override
	public Object getFirstColumnValue(Object rowObject) {
		return rowObject;
	}

	@Override
	public Long getCount(Object rowObject, Object nextRowObject, Long activityId) {
		Date from = (Date) rowObject;
		Date to = nextRowObject == null ? new Date(Long.MAX_VALUE) : (Date) nextRowObject;
		
		return InfodocContainerFactory.getActivityInstanceContainer().countByActivityId(activityId, from, to);
	}

	@Override
	public void addCharts(DynamicReportBuilder reportBuilder) {
		PropertyColumn timePeriodColumn = (PropertyColumn) reportBuilder.getColumn(0);
		
		DJTimeSeriesChartBuilder chartBuilder = new DJTimeSeriesChartBuilder();
		chartBuilder.setTimePeriod(timePeriodColumn);
		
		int resolution = (Integer) dateResolutionSelect.getValue();
		
		if(resolution == DateField.RESOLUTION_YEAR) {
			chartBuilder.setTimePeriodClass(Year.class);
		} else if(resolution == DateField.RESOLUTION_MONTH) {
			chartBuilder.setTimePeriodClass(Month.class);
		} else if(resolution == DateField.RESOLUTION_DAY) {
			chartBuilder.setTimePeriodClass(Day.class);
		} else if(resolution == DateField.RESOLUTION_HOUR) {
			chartBuilder.setTimePeriodClass(Hour.class);
		} else if(resolution == DateField.RESOLUTION_MIN) {
			chartBuilder.setTimePeriodClass(Minute.class);
		}
		
		for(int i = 1; i < reportBuilder.getColumns().size(); i++) {
			AbstractColumn seriesColumn = reportBuilder.getColumn(i);
			chartBuilder.addSerie(seriesColumn);
		}
		
		reportBuilder.addChart(chartBuilder.build());
	}
	
	@Override
	public FormLayout getParametersComponent() {
		dateResolutionSelect.addItem(DateField.RESOLUTION_YEAR);
		dateResolutionSelect.addItem(DateField.RESOLUTION_MONTH);
		dateResolutionSelect.addItem(DateField.RESOLUTION_DAY);
		dateResolutionSelect.addItem(DateField.RESOLUTION_HOUR);
		dateResolutionSelect.addItem(DateField.RESOLUTION_MIN);
		
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_YEAR, BasicConstants.uiYear);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_MONTH, BasicConstants.uiMonth);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_DAY, BasicConstants.uiDay);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_HOUR, BasicConstants.uiHour);
		dateResolutionSelect.setItemCaption(DateField.RESOLUTION_MIN, BasicConstants.uiMinute);
		
		dateResolutionSelect.setValue(DateField.RESOLUTION_MONTH);
		dateResolutionSelect.setNullSelectionAllowed(false);
		
		FormLayout component = super.getParametersComponent();
		component.addComponent(dateResolutionSelect);
		
		return component;
	}

}
