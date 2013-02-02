package infodoc.basic.report;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;
import infodoc.core.dto.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.chart.builder.DJStackedBar3DChartBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;

public abstract class AbstractActivityInstancesListReport extends AbstractDateIntervalReport {

	private static final long serialVersionUID = 1L;
	
	public static final String ACTIVITY = "activity";

	public AbstractActivityInstancesListReport(Form form, JavaReport report) {
		super(form, report);
	}
	
	public abstract String getFirstColumnPropertyName();

	public abstract Class<?> getFirstColumnPropertyClass();

	public abstract String getFirstColumnTitle();
	
	public abstract Collection<?> getRowObjects();
	
	public abstract Object getFirstColumnValue(Object rowObject);
	
	public abstract Long getCount(Object rowObject, Object nextRowObject, Long activityId);

	@Override
	public void buildColumns(DynamicReportBuilder reportBuilder) {
		super.buildColumns(reportBuilder);
		reportBuilder.setGrandTotalLegend(BasicConstants.uiTotal);
		addCharts(reportBuilder);
	}

	public void addCharts(DynamicReportBuilder reportBuilder) {
		PropertyColumn keyColumn = (PropertyColumn) reportBuilder.getColumn(0);
		
		DJStackedBar3DChartBuilder chartBuilder1 = new DJStackedBar3DChartBuilder();
		chartBuilder1.setCategory(keyColumn);
		
		DJStackedBar3DChartBuilder chartBuilder2 = new DJStackedBar3DChartBuilder();
		chartBuilder2.setCategory(keyColumn);
		chartBuilder2.setUseSeriesAsCategory(true);
		
		for(int i = 1; i < reportBuilder.getColumns().size(); i++) {
			AbstractColumn seriesColumn = reportBuilder.getColumn(i);
			chartBuilder1.addSerie(seriesColumn);
			chartBuilder2.addSerie(seriesColumn);
		}
		
		reportBuilder.addChart(chartBuilder1.build());
		reportBuilder.addChart(chartBuilder2.build());
	}
	
	@Override
	public void configureColumn(String property, AbstractColumn column, DynamicReportBuilder reportBuilder) {
		if(property.startsWith(ACTIVITY)) {
			super.configureColumn(property, column, reportBuilder);
			Style grandTotalStyle = new StyleBuilder(true).setPadding(0).setFont(Font.ARIAL_MEDIUM_BOLD).setHorizontalAlign(HorizontalAlign.CENTER).build();
			reportBuilder.addGlobalFooterVariable(column, DJCalculation.SUM, grandTotalStyle);
		}
	};

	@Override
	public String[] getColumnProperties() {
		ArrayList<String> names = new ArrayList<String>();
		names.add(getFirstColumnPropertyName());
		names.addAll(getActivityColumnsProperties());
		
		return ListToStringArray(names);
	}

	@Override
	public Class<?>[] getColumnClasses() {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(getFirstColumnPropertyClass());
		classes.addAll(getActivityColumnsClasses());
		
		return ListToClassArray(classes);
	}

	@Override
	public String[] getColumnTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		titles.add(getFirstColumnTitle());
		titles.addAll(getActivityColumnsTitles());
		
		return ListToStringArray(titles);
	}

	public List<String> getActivityColumnsProperties() {
		ArrayList<String> names = new ArrayList<String>();
		
		if(form.getActivities() != null) {
			for(Activity activity : form.getActivities()) {
				if(!activity.isDisabled()) {
					names.add(ACTIVITY + activity.getId());
				}
			}
		}
		
		return names;
	}

	public List<Class<?>> getActivityColumnsClasses() {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		
		if(form.getActivities() != null) {
			for(Activity activity : form.getActivities()) {
				if(!activity.isDisabled()) {
					classes.add(Long.class);
				}
			}
		}
		
		return classes;
	}

	public List<String> getActivityColumnsTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		form = InfodocContainerFactory.getFormContainer().getEntity(form.getId());
		
		if(form.getActivities() != null) {
			for(Activity activity : form.getActivities()) {
				if(!activity.isDisabled()) {
					titles.add(activity.getName());
				}
			}
		}
		
		return titles;
	}
	
	public DynaProperty[] getDynaProperties() {
		ArrayList<DynaProperty> properties = new ArrayList<DynaProperty>();
		properties.add(new DynaProperty(getFirstColumnPropertyName(), getFirstColumnPropertyClass()));
		
		if(form.getActivities() != null) {
			for(Activity activity: form.getActivities()) {
				if(!activity.isDisabled()) {
					properties.add(new DynaProperty(ACTIVITY + activity.getId(), Long.class));
				}
			}
		}
		
		DynaProperty[] propertiesArray = new DynaProperty[properties.size()];
		
		for(int i = 0; i < properties.size(); i++) {
			propertiesArray[i] = properties.get(i);
		}
		
		return propertiesArray;
	}
	
	@Override
	public Collection<?> getData() {
		ArrayList<DynaBean> data = new ArrayList<DynaBean>();
		Collection<?> rowObjects = getRowObjects();		
		Object[] rowObjectsArray = rowObjects.toArray();
		DynaProperty[] dynaProperties = getDynaProperties();
		
		for(int i = 0; i < rowObjects.size(); i++) {
			Object next = null;
			if(i < rowObjects.size() - 1) {
				next = rowObjectsArray[i + 1];
			}
			data.add(getDynaBean(rowObjectsArray[i], next, dynaProperties));
		}
		
		return data;
	}

	public DynaBean getDynaBean(Object rowObject, Object nextRowObject, DynaProperty[] properties) {
		BasicDynaClass volumenClass = new BasicDynaClass("volumen", BasicDynaBean.class, properties);
		DynaBean dynaBean = null;
		
		try {
			dynaBean = volumenClass.newInstance();
			dynaBean.set(getFirstColumnPropertyName(), getFirstColumnValue(rowObject));
			
			if(form.getActivities() != null) {
				for(Activity activity : form.getActivities()) {
					Long count = getCount(rowObject, nextRowObject, activity.getId());
					dynaBean.set(ACTIVITY + activity.getId(), count);
				}
			}
			
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		
		return dynaBean;
	}
	
	@Override
	public void initLayout() {
		super.initLayout();
		columnsCheckBoxes[0].setEnabled(false);
	}

}
