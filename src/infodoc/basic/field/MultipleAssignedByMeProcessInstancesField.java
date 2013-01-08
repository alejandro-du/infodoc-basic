package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.ProcessInstanceContainer;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.User;

import java.util.List;

public class MultipleAssignedByMeProcessInstancesField extends MultipleProcessInstances {

	@Override
	public List<ProcessInstance> getProcessInstances(ProcessInstanceContainer processContainer, User user, Long activityId) {
		return processContainer.findAssignedByUserIdAndCurrentActivityId(user.getId(), activityId);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpMultipleAssignedByMeProcessInstancesField;
	}

}
