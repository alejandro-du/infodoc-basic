package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.InfodocConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.container.PropertyValueContainer;
import infodoc.core.dto.Activity;
import infodoc.core.dto.Case;
import infodoc.core.dto.Form;
import infodoc.core.dto.Property;
import infodoc.core.dto.PropertyValue;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;
import infodoc.core.ui.common.InfodocTheme;

import java.util.List;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;

public class ReferencingCasesField implements FieldFactory {

	@Override
	public FieldType getType() {
		return null;
	}

	@Override
	public Field getField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		return getTable(property, form);
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		return null;
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiReferencingCasesField;
	}
	
	public Table getTable(Property property, CaseForm form) {
		String[] paramsArray = property.getParameter().split(",");
		long referencingPropertyId = Long.parseLong(paramsArray[0].trim());
		long formId = Long.parseLong(paramsArray[1].trim());
		Form referencingForm = InfodocContainerFactory.getFormContainer().getEntity(formId);
		PropertyValueContainer propertyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
		List<Case> cases = InfodocContainerFactory.getCaseContainer().findReferencingCases(referencingPropertyId, form.getCase().getId(), formId);
		Table table = null;
		
		if(cases != null && !cases.isEmpty()) {
			table = new Table();
			table.setWidth("100%");
			table.setColumnReorderingAllowed(true);
			table.setColumnCollapsingAllowed(true);
			table.setCaption(property.getName());
			
			table.addContainerProperty(InfodocConstants.uiFormNumber, String.class, "");
			table.setColumnIcon(InfodocConstants.uiFormNumber, new ThemeResource(InfodocTheme.iconNumber));
			
			for(Property p : referencingForm.getProperties()) {
				table.addContainerProperty(p.getName(), String.class, "");
				
				if(p.getIcon() != null && !p.getIcon().isEmpty()) {
					table.setColumnIcon(p.getName(), new ThemeResource(p.getIcon()));
				}
			}
			
			for(Case c : cases) {
				String[] cells = new String[referencingForm.getProperties().size() + 1];
				
				cells[0] = "" + c.getNumber();
				
				for(int i = 1; i < cells.length; i++) {
					for(PropertyValue pv : c.getPropertyValues()) {
						if(pv.getProperty().equals(referencingForm.getProperties().toArray()[i - 1])) {
							cells[i] = propertyValueContainer.getStringValue(pv); 
						}
					}
				}
				
				table.addItem(cells, c.getId());
			}
		}
		
		
		return table;
	}

}
