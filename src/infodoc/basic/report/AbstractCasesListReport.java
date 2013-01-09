package infodoc.basic.report;

import infodoc.basic.BasicConstants;
import infodoc.core.container.PropertyValueContainer;
import infodoc.core.container.CaseContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Property;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.Case;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;
import infodoc.core.dto.ActivityInstance;

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
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import enterpriseapp.Utils;

public abstract class AbstractCasesListReport extends AbstractDateIntervalReport {

	private static final long serialVersionUID = 1L;
	
	private static final String PROPERTY = "property";
	
	public static final String NUMBER = "number";
	public static final String FIRST_ACTIVITY_EXECUTION_TIME = "firstActivityExecutionTime";
	public static final String FIRST_ACTIVITY_NAME = "firstActivityName";
	public static final String FIRST_ACTIVITY_USER = "firstActivityUser";
	public static final String LAST_ACTIVITY_EXECUTION_TIME = "lastActivityExecutionTime";
	public static final String LAST_ACTIVITY_NAME = "lastActivityName";
	public static final String LAST_ACTIVITY_USER = "lastActivityUser";
	
	public AbstractCasesListReport(Form form, JavaReport report) {
		super(form, report);
	}
	
	public abstract List<Case> getCases();

	@Override
	public String[] getColumnProperties() {
		ArrayList<String> names = new ArrayList<String>();
		
		names.add(NUMBER);
		names.add(FIRST_ACTIVITY_EXECUTION_TIME);
		names.add(FIRST_ACTIVITY_NAME);
		names.add(FIRST_ACTIVITY_USER);
		names.add(LAST_ACTIVITY_EXECUTION_TIME);
		names.add(LAST_ACTIVITY_NAME);
		names.add(LAST_ACTIVITY_USER);
		
		if(form.getProperties() != null) {
			for(Property property: form.getProperties()) {
				if(!property.getDisabled()) {
					names.add(PROPERTY + property.getId());
				}
			}
		}
		
		return ListToStringArray(names);
	}
	
	@Override
	public Class<?>[] getColumnClasses() {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		
		classes.add(String.class);
		classes.add(String.class);
		classes.add(String.class);
		classes.add(String.class);
		classes.add(String.class);
		classes.add(String.class);
		classes.add(String.class);
		
		if(form.getProperties() != null) {
			for(Property property : form.getProperties()) {
				if(!property.getDisabled()) {
					classes.add(String.class);
				}
			}
		}
		
		return ListToClassArray(classes);
	}

	@Override
	public String[] getColumnTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		
		titles.add(BasicConstants.uiFormNumber);
		titles.add(BasicConstants.uiDate + " " + BasicConstants.uiFirstActivityInstance.toLowerCase());
		titles.add(BasicConstants.uiFirstActivityInstance);
		titles.add(BasicConstants.uiUser + " " + BasicConstants.uiFirstActivityInstance.toLowerCase());
		titles.add(BasicConstants.uiDate + " " + BasicConstants.uiLastActivityInstance.toLowerCase());
		titles.add(BasicConstants.uiLastActivityInstance);
		titles.add(BasicConstants.uiUser + " " + BasicConstants.uiLastActivityInstance.toLowerCase());
		
		form = InfodocContainerFactory.getFormContainer().getEntity(form.getId());
		
		if(form.getProperties() != null) {
			for(Property property: form.getProperties()) {
				if(!property.getDisabled()) {
					titles.add(property.getName());
				}
			}
		}
		
		return ListToStringArray(titles);
	}
	
	@Override
	public void configureColumn(String property, AbstractColumn column, DynamicReportBuilder reportBuilder) {
		super.configureColumn(property, column, reportBuilder);
		Style grandTotalStyle = new StyleBuilder(true).setPadding(0).setFont(Font.ARIAL_MEDIUM_BOLD).setHorizontalAlign(HorizontalAlign.CENTER).build();
		reportBuilder.addGlobalFooterVariable(column, DJCalculation.COUNT, grandTotalStyle);
	};
	
	@Override
	public Collection<?> getData() {
		ArrayList<DynaProperty> properties = new ArrayList<DynaProperty>();
		properties.add(new DynaProperty(NUMBER, String.class));
		properties.add(new DynaProperty(FIRST_ACTIVITY_EXECUTION_TIME, String.class));
		properties.add(new DynaProperty(FIRST_ACTIVITY_NAME, String.class));
		properties.add(new DynaProperty(FIRST_ACTIVITY_USER, String.class));
		properties.add(new DynaProperty(LAST_ACTIVITY_EXECUTION_TIME, String.class));
		properties.add(new DynaProperty(LAST_ACTIVITY_NAME, String.class));
		properties.add(new DynaProperty(LAST_ACTIVITY_USER, String.class));
		properties.addAll(getDynaProperties());
		
		DynaProperty[] propertiesArray = new DynaProperty[properties.size()];
		
		for(int i = 0; i < properties.size(); i++) {
			propertiesArray[i] = properties.get(i);
		}
		
		ArrayList<DynaBean> data = new ArrayList<DynaBean>();
		List<Case> instances = getCases();
		
		for(Case instance : instances) {
			data.add(getDynaBean(instance, propertiesArray));
		}
		
		return data;
	}
	
	@Override
	public boolean getDefalutColumnCheckBox(String property) {
		if(form.getProperties() != null) {
			for(Property p: form.getProperties()) {
				if(!p.getDisabled()) {
					if(property.equals(PROPERTY + p.getId()) && !p.getShowInReports()) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	@Override
	public DynamicReportBuilder getReportBuilder() {
		DynamicReportBuilder reportBuilder = super.getReportBuilder();
		reportBuilder.setGrandTotalLegend(BasicConstants.uiTotal);
		
		return reportBuilder;
	}
	
	public DynaBean getDynaBean(Case caseDto, DynaProperty[] properties) {
		BasicDynaClass caseDtoClass = new BasicDynaClass("caseDto", BasicDynaBean.class, properties);
		DynaBean dynaBean = null;
		
		try {
			CaseContainer caseDtoContainer = InfodocContainerFactory.getCaseContainer();
			ActivityInstance firstActivity = caseDtoContainer.getFisrtActivityInstance(caseDto);
			ActivityInstance lastActivity = caseDtoContainer.getLastActivityInstance(caseDto);
			dynaBean = caseDtoClass.newInstance();
			
			dynaBean.set(NUMBER, caseDto.toString());
			
			if(firstActivity != null) {
				dynaBean.set(FIRST_ACTIVITY_EXECUTION_TIME, Utils.dateTimeToString(firstActivity.getExecutionTime()));
				dynaBean.set(FIRST_ACTIVITY_NAME, firstActivity.getActivity().getName());
				dynaBean.set(FIRST_ACTIVITY_USER, firstActivity.getUser().toString());
			}
			
			if(lastActivity != null) {
				dynaBean.set(LAST_ACTIVITY_EXECUTION_TIME, Utils.dateTimeToString(lastActivity.getExecutionTime()));
				dynaBean.set(LAST_ACTIVITY_NAME, lastActivity.getActivity().getName());
				dynaBean.set(LAST_ACTIVITY_USER, lastActivity.getUser().toString());
			}
			
			PropertyValueContainer proprtyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
			
			if(caseDto.getPropertyValues() != null) {
				for(PropertyValue value: caseDto.getPropertyValues()) {
					if(form.getProperties().contains(value.getProperty())) {
						dynaBean.set(PROPERTY + value.getProperty().getId(), proprtyValueContainer.getStringValue(value));
					}
				}
			}
			
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		
		return dynaBean;
	}
	
	public List<DynaProperty> getDynaProperties() {
		ArrayList<DynaProperty> properties = new ArrayList<DynaProperty>();
		
		if(form.getProperties() != null) {
			for(Property property: form.getProperties()) {
				if(!property.getDisabled()) {
					properties.add(new DynaProperty(PROPERTY + property.getId(), String.class));
				}
			}
		}
		
		return properties;
	}

}
