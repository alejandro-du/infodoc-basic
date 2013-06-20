package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.CaseContainer;
import infodoc.core.dto.Case;
import infodoc.core.dto.User;

import java.util.List;

public class MultipleMyCases extends MultipleCases {
	
	@Override
	public List<Case> getCases(CaseContainer formContainer, User user, Long activityId) {
		return formContainer.findAvailableByUserIdAndActivityId(user.getId(), activityId);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpMultipleMyCasesField;
	}
	
}
