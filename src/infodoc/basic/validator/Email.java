package infodoc.basic.validator;

import infodoc.basic.BasicConstants;
import infodoc.core.ui.validator.ValidatorFactory;

import com.vaadin.Application;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;

public class Email implements ValidatorFactory {

	@Override
	public Validator getValidator(String errorMessage, String param, Application application) {
		return new EmailValidator(errorMessage);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpEmailValidator;
	}

}
