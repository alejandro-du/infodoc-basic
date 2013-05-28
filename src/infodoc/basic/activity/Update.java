package infodoc.basic.activity;

import infodoc.basic.BasicConstants;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Activity;
import infodoc.core.dto.User;
import infodoc.core.ui.activity.ActivityListExecutorTemplate;
import infodoc.core.ui.cases.CaseForm;
import infodoc.core.ui.common.InfodocTheme;

import java.util.HashSet;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;

public class Update extends ActivityListExecutorTemplate {

	private static final long serialVersionUID = 1L;
	
	protected boolean dontAssign = false;
	
	public Update() {
		super();
	}

	public Update(Activity activity, User user) {
		super(activity, user);
		parseParams(activity.getParameter());
	}
	
	public void parseParams(String parameter) {
		if(getActivity().getParameter() == null || getActivity().getParameter().isEmpty()) {
			return;
		}
		
		String[] params = getActivity().getParameter().split(",");
		int i = 0;
		
		while(i < params.length) {
			String param = params[i].trim();
			
			if(param.toLowerCase().equals("dontAssign".toLowerCase())) {
				i = dontAssign(params, i + 1);
				
			} else {
				i = parseOther(params, i);
			}
		}
	}
	
	protected int parseOther(String[] params, int startPosition) {
		throw new RuntimeException("Wrong parameter for activity " + getActivity().toString() + ": " + params[startPosition]);
	}
	
	protected int dontAssign(String[] params, int startPosition) {
		dontAssign = true;
		return startPosition;
	}

	@Override
	public void execute(CaseForm form) {
		form.validate();
		HashSet<User> users = new HashSet<User>();
		
		if(!dontAssign) {
			users.add(getUser());
		}
		
		if(!beforeSaveCase(form)) {
			return;
		}
		
		if(!afterSaveCase(form)) {
			return;
		}
		
		InfodocContainerFactory.getCaseContainer().updateInstance(form.getCase(), form.getPropertyValues(), getNewActivityInstance(form.getCase(), form.getComments(), users, null));
		update();
	}

	public boolean beforeSaveCase(CaseForm form) {
		return true;
	}

	public boolean afterSaveCase(CaseForm form) {
		return true;
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpUpdateActivity;
	}

	@Override
	public Resource getIcon() {
		return new ThemeResource(InfodocTheme.iconActivityUpdate);
	}

}
