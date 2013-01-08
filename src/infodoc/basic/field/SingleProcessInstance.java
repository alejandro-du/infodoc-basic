package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ProcessInstanceContainer;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Select;

public class SingleProcessInstance extends AbstractProcessInstances {
	
	@Override
	public AbstractSelect getField() {
		return new Select() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValue() {
				Object value = super.getValue();
				Set<ProcessInstance> processInstances = null;
				
				if(value != null) {
					processInstances = new HashSet<ProcessInstance>();
					processInstances.add((ProcessInstance) value);
				}
				
				return processInstances;
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public void setValue(Object newValue) {
				Set<ProcessInstance> processInstances = (Set<ProcessInstance>) newValue;
				
				if(processInstances != null && !processInstances.isEmpty()) {
					super.setValue(processInstances.toArray()[0]);
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
	public List<ProcessInstance> getProcessInstances(ProcessInstanceContainer processInstanceContainer, User user, Long activityId) {
		return processInstanceContainer.findByActivityId(activityId);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpSingleProcessInstanceField;
	}

}
