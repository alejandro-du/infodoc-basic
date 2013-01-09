package infodoc.basic.field;

import infodoc.basic.BasicConstants;
import infodoc.core.container.CaseContainer;
import infodoc.core.dto.Case;
import infodoc.core.dto.User;

import java.util.List;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ListSelect;

public class MultipleCases extends AbstractCases {

	@Override
	public AbstractSelect getField() {
		ListSelect select = new ListSelect();
		select.setMultiSelect(true);
		
		return select;
	}

	@Override
	public List<Case> getCases(CaseContainer formContainer, User user, Long activityId) {
		return formContainer.findByActivityId(activityId);
	}
	
	@Override
	public String getHelp() {
		return BasicConstants.uiHelpMultipleCasesField;
	}

}
