package infodoc.basic.report;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.FormLayout;

public class FinalizedCasesReport extends AbstractCasesListReport {

	private static final long serialVersionUID = 1L;

	public FinalizedCasesReport(Form form, JavaReport report) {
		super(form, report);
	}

	@Override
	public boolean getDefalutColumnCheckBox(String property) {
		if(
			property.equals(FIRST_ACTIVITY_EXECUTION_TIME)
			|| property.equals(FIRST_ACTIVITY_NAME)
			|| property.equals(FIRST_ACTIVITY_USER)
		) {
			return false;
		}
		
		return super.getDefalutColumnCheckBox(property);
	}
	
	@Override
	public List<Case> getCases() {
		return InfodocContainerFactory.getCaseContainer().findFinishedByFormId(form.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
	}

	@Override
	public FormLayout getParametersComponent() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, -1);
		fromDateField.setValue(calendar.getTime());
		
		return super.getParametersComponent();
	}
}
