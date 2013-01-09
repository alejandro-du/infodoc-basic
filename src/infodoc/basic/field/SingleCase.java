package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.CaseContainer;
import infodoc.core.dto.Case;
import infodoc.core.dto.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Select;

public class SingleCase extends AbstractCases {
	
	@Override
	public AbstractSelect getField() {
		return new Select() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValue() {
				Object value = super.getValue();
				Set<Case> caseDtos = null;
				
				if(value != null) {
					caseDtos = new HashSet<Case>();
					caseDtos.add((Case) value);
				}
				
				return caseDtos;
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public void setValue(Object newValue) {
				Set<Case> caseDtos = (Set<Case>) newValue;
				
				if(caseDtos != null && !caseDtos.isEmpty()) {
					super.setValue(caseDtos.toArray()[0]);
				} else {
					super.setValue(null);
				}
			}
			
			@Override
		    public void validate() throws InvalidValueException {
		    	super.validate();
		    }

		};
	}
	
	@Override
	public List<Case> getCases(CaseContainer caseDtoContainer, User user, Long activityId) {
		return caseDtoContainer.findByActivityId(activityId);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpSingleCaseField;
	}

}
