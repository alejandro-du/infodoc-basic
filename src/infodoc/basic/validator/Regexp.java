package infodoc.basic.validator;

import infodoc.basic.BasicConstants;
import infodoc.core.ui.validator.ValidatorFactory;

import com.vaadin.Application;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.RegexpValidator;

public class Regexp implements ValidatorFactory {

	@Override
	public Validator getValidator(String errorMessage, String param, Application application) {
		return new RegexpValidator(param, errorMessage);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpRegexpValidator;
	}

}
