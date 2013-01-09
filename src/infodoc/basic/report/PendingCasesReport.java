package infodoc.basic.report;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;

import java.util.Date;
import java.util.List;

public class PendingCasesReport extends AbstractCasesListReport {
	
	private static final long serialVersionUID = 1L;
	
	public PendingCasesReport(Form form, JavaReport report) {
		super(form, report);
	}
	
	@Override
	public boolean getDefalutColumnCheckBox(String property) {
		if(
			property.equals(FIRST_ACTIVITY_NAME)
			|| property.equals(FIRST_ACTIVITY_USER)
		) {
			return false;
		}
		
		return super.getDefalutColumnCheckBox(property);
	}
	
	public List<Case> getCases() {
		return InfodocContainerFactory.getCaseContainer().findPendingByFormId(form.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
	}
	
}
