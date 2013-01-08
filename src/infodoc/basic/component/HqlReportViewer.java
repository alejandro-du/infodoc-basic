package infodoc.basic.component;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.HqlReport;
import infodoc.core.dto.HqlReportParameter;
import infodoc.core.dto.User;
import infodoc.core.ui.comun.InfodocReport;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import enterpriseapp.EnterpriseApplication;
import enterpriseapp.hibernate.Db;

public class HqlReportViewer extends InfodocReport {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(HqlReportViewer.class);

	private HqlReport report;
	private Query query;
	private Map<HqlReportParameter, Field> paramsFields = new HashMap<HqlReportParameter, Field>();
	
	public HqlReportViewer(HqlReport report) {
		super(report.getProcess(), null);
		this.report = report;
		Db.beginTransaction();

		query = Db.getCurrentSession().createQuery(getParsedQuery(report.getHqlQuery()));
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		query.setMaxResults(1);

		Db.commitTransaction();
	}

	@Override
	public String getTitle() {
		return report.getName();
	}

	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String[] getColumnProperties() {
		return query.getReturnAliases();
	}

	@Override
	public Class<?>[] getColumnClasses() {
		Type[] returnTypes = query.getReturnTypes();
		Class<?>[] classes = new Class[returnTypes.length];
		
		for(int i = 0; i < returnTypes.length; i++) {
			classes[i] = returnTypes[i].getReturnedClass();
		}
		
		return classes;
	}

	@Override
	public String[] getColumnTitles() {
		return query.getReturnAliases();
	}

	@Override
	public Collection<?> getData() {
		String[] paramNames = new String[paramsFields.size()];
		Object[] params = new Object[paramsFields.size()];
		
		try {
			int i = 0;
			
			for(Entry<HqlReportParameter, Field> entry : paramsFields.entrySet()) {
				paramNames[i] = entry.getKey().getPlaceholder();
				
				Constructor<?> constructor = Class.forName(entry.getKey().getJavaClass()).getConstructor(String.class);
				String stringValue = entry.getValue().getValue().toString();
				
				if(stringValue != null && !stringValue.isEmpty()) {
					params[i] = constructor.newInstance(stringValue);
				}
				
				i++;
			}
			
		} catch (Exception e) {
			logger.error("Error getting parameter value. Chack that the configured 'javaClass' implements a constructor from String", e);
			throw new RuntimeException(e);
		}
		
		return InfodocContainerFactory.getHqlReportContainer().getQueryResult(getParsedQuery(report.getHqlQuery()), getColumnProperties(), getColumnClasses(), paramNames, params);
	}

	@Override
	public Component getParametersComponent() {
		report = InfodocContainerFactory.getHqlReportContainer().getEntity(report.getId());
		FormLayout layout = new FormLayout();
		paramsFields.clear();
		
		for(HqlReportParameter param : report.getHqlParameters()) {
			TextField field = new TextField(param.getLabel());
			layout.addComponent(field);
			paramsFields.put(param, field);
		}
		
		return layout;
	}

	private String getParsedQuery(String query) {
		return query
			.replace("${processId}", "" + report.getProcess().getId())
			.replace("${userId}", "" + ((User) EnterpriseApplication.getInstance().getUser()).getId());
	}

}
