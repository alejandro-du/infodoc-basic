package infodoc.basic.report;

import infodoc.basic.BasicConstants;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Form;
import infodoc.core.ui.common.InfodocReport;

import java.util.Calendar;
import java.util.Date;

import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.InlineDateField;

import enterpriseapp.Utils;

public abstract class AbstractDateIntervalReport extends InfodocReport {

	private static final long serialVersionUID = 1L;

	protected InlineDateField fromDateField = new InlineDateField(BasicConstants.uiFrom);
	protected InlineDateField toDateField = new InlineDateField(BasicConstants.uiTo);
	
	public AbstractDateIntervalReport(Form form, JavaReport report) {
		super(form, report);
	}
	
	@Override
	public String getSubtitle() {
		return BasicConstants.uiFrom + " " + Utils.dateToString((Date) fromDateField.getValue()) + " " + BasicConstants.uiTo.toLowerCase() + " " + Utils.dateToString((Date) toDateField.getValue());
	}
	
	@Override
	public FormLayout getParametersComponent() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		toDateField.setResolution(DateField.RESOLUTION_DAY);
		toDateField.setImmediate(true);
		toDateField.setDateFormat(Utils.getDateTimeFormatPattern());
		toDateField.setValue(calendar.getTime());
		
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		fromDateField.setResolution(DateField.RESOLUTION_DAY);
		fromDateField.setImmediate(true);
		fromDateField.setDateFormat(Utils.getDateTimeFormatPattern());
		fromDateField.setValue(calendar.getTime());
		
		FormLayout layout = new FormLayout();
		layout.setSpacing(true);
		layout.addComponent(fromDateField);
		layout.addComponent(toDateField);
		
		return layout;
	}

}
