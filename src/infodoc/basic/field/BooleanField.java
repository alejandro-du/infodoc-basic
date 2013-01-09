package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.PropertyContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Property;
import infodoc.core.dto.Form;
import infodoc.core.dto.Activity;
import infodoc.core.field.FieldFactory;
import infodoc.core.field.FieldType;
import infodoc.core.ui.cases.CaseForm;

import java.util.ArrayList;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window.Notification;

import enterpriseapp.EnterpriseApplication;

public class BooleanField implements FieldFactory {
	
	private class BooleanoCheckBox extends CheckBox {
		
		private static final long serialVersionUID = 1L;
		
		private CaseForm form;
		private ArrayList<Property> propertiesToShowIfTrue;
		private ArrayList<Property> propertiesToShowIfFalse;
		
		public BooleanoCheckBox(CaseForm form) {
			this.form = form;
			addListener((ValueChangeListener) this);
			setImmediate(true);
		}
		
		@Override
		public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			updateProperties();
		}

		public void updateProperties() {
			if(propertiesToShowIfTrue != null) {
				for(Property property : propertiesToShowIfTrue) {
					updateProperty(property, booleanValue());
				}
			}
			
			if(propertiesToShowIfFalse != null) {
				for(Property property : propertiesToShowIfFalse) {
					updateProperty(property, !booleanValue());
				}
			}
		}
		
		public void updateProperty(Property property, boolean visible) {
			Field field = form.getField(property.getName());
			
			if(field != null) {
				field.setVisible(visible);
				field.setRequired(!form.isShowShearchProperties() && visible && property.getRequired());
			}
		}

		public void setPropertiesToShowIfTrue(ArrayList<Property> propertiesToShowIf) {
			this.propertiesToShowIfTrue = propertiesToShowIf;
		}

		public void setPropertiesToShowIfFalse(ArrayList<Property> propertiesToShowIfFalse) {
			this.propertiesToShowIfFalse = propertiesToShowIfFalse;
		}
	}

	@Override
	public FieldType getType() {
		return FieldType.BOOLEAN;
	}
	
	@Override
	public Field getField(Property property, final CaseForm form, Activity activity, Form formDto, Application application) {
		final BooleanoCheckBox checkBox = new BooleanoCheckBox(form);
		
		if(property.getParameter() != null && !property.getParameter().trim().isEmpty()) {
			String[] params = property.getParameter().split(",");
			Boolean ifTrueThenShow = null;
			final ArrayList<Property> propertiesToShowIfTrue = new ArrayList<Property>();
			final ArrayList<Property> propertiesToShowIfFalse = new ArrayList<Property>();
			PropertyContainer propertyContainer = InfodocContainerFactory.getPropertyContainer();
			
			for(String param : params) {
				param = param.trim();
				if(param.toLowerCase().equals("ifTrueThenShow".toLowerCase())) {
					ifTrueThenShow = true;
				} else if(param.toLowerCase().equals("ifFalseThenShow".toLowerCase())) {
					ifTrueThenShow = false;
				} else {
					if(ifTrueThenShow == null) {
						EnterpriseApplication.getInstance().getMainWindow().showNotification(BasicConstants.uiErrorIncorrectParam(property.toString()), Notification.TYPE_ERROR_MESSAGE);
						throw new RuntimeException("Incorrect param value (property: " + property + ", parameter: " + property.getParameter() + ")");
					}
					
					if(ifTrueThenShow) {
						propertiesToShowIfTrue.add(propertyContainer.getEntity(new Long(param)));
					} else {
						propertiesToShowIfFalse.add(propertyContainer.getEntity(new Long(param)));
					}
				}
			}
			
			checkBox.setPropertiesToShowIfFalse(propertiesToShowIfFalse);
			checkBox.setPropertiesToShowIfTrue(propertiesToShowIfTrue);
			
			form.addListener((ValueChangeListener) checkBox);
		}
		
		return checkBox;
	}

	@Override
	public Field getSearchField(Property property, CaseForm form, Activity activity, Form formDto, Application application) {
		return getField(property, form, activity, formDto, application);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpBooleanField;
	}

}
