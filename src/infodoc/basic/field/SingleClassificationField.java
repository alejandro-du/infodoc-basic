package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ClassificationValueContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ClassificationValue;

import java.util.HashSet;
import java.util.Set;

import enterpriseapp.ui.crud.EntityField;
import enterpriseapp.ui.crud.EntitySetField;

public class SingleClassificationField extends AbstractClassifications {
	
	@Override
	public EntitySetField getEntitySetField(ClassificationValueContainer classificationValueContainer) {
		return new EntityField(ClassificationValue.class, classificationValueContainer) {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValue() {
				Set<ClassificationValue> classificationValues = new HashSet<ClassificationValue>();
				Object value = super.getValue();
				
				if(value != null) {
					if(value instanceof String) {
						value = getClassificationValue(value.toString());
					}
					
					classificationValues.add((ClassificationValue) value);
				}
				
				return classificationValues;
			}
			
			private ClassificationValue getClassificationValue(String name) {
				ClassificationValueContainer classificationValueContainer = InfodocContainerFactory.getClassificationValueContainer();
				ClassificationValue classificationValue = classificationValueContainer.getByNameAndClassificationId(name, classificationId);
				
				if(classificationValue == null) {
					classificationValue = new ClassificationValue();
					classificationValue.setName(name);
					classificationValue.setClassification(InfodocContainerFactory.getClassificationContainer().getEntity(classificationId));
					classificationValue.setId(classificationValueContainer.saveEntity(classificationValue));
					classificationValue = classificationValueContainer.getEntity(classificationValue.getId());
				}
				
				return classificationValue;
			}

			@Override
			@SuppressWarnings("unchecked")
			public void setValue(Object newValue) {
				Set<ClassificationValue> classificationValues = (Set<ClassificationValue>) newValue;
				
				if(classificationValues != null && !classificationValues.isEmpty()) {
					super.setValue(classificationValues.toArray()[0]);
				} else {
					super.setValue(null);
				}
				
			}
		};
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpSingleClassificationField;
	}

}
