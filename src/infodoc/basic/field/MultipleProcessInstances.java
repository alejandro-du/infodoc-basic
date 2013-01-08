package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ProcessInstanceContainer;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.User;

import java.util.List;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ListSelect;

public class MultipleProcessInstances extends AbstractProcessInstances {

	@Override
	public AbstractSelect getField() {
		ListSelect select = new ListSelect();
		select.setMultiSelect(true);
		
		return select;
	}

	@Override
	public List<ProcessInstance> getProcessInstances(ProcessInstanceContainer processContainer, User user, Long activityId) {
		return processContainer.findByActivityId(activityId);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpMultipleProcessInstancesField;
	}

}
