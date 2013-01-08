package infodoc.basic.report;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Process;

import java.util.Date;
import java.util.List;

public class PendingProcessesReport extends AbstractProcessInstancesListReport {
	
	private static final long serialVersionUID = 1L;
	
	public PendingProcessesReport(Process process, JavaReport report) {
		super(process, report);
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
	
	public List<ProcessInstance> getProcessInstances() {
		return InfodocContainerFactory.getProcessInstanceContainer().findPendingByProcessId(process.getId(), (Date) fromDateField.getValue(), (Date) toDateField.getValue());
	}
	
}
