package infodoc.basic.validator;

import infodoc.basic.BasicConstants;
import infodoc.core.ui.validator.ValidatorFactory;

import com.vaadin.Application;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;

public class MaxStringLength implements ValidatorFactory {

	@Override
	public Validator getValidator(String errorMessage, String param, Application application) {
		return new StringLengthValidator(errorMessage, 0, Integer.parseInt(param), true);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpMaxStringLengthValidator;
	}

}
