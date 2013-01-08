package infodoc.basic.validator;

import infodoc.basic.BasicConstants;
import infodoc.core.container.validator.LongValidator;
import infodoc.core.ui.validator.ValidatorFactory;

import com.vaadin.Application;
import com.vaadin.data.Validator;

public class LongNumber implements ValidatorFactory {

	@Override
	public Validator getValidator(String errorMessage, String param, Application application) {
		return new LongValidator(errorMessage);
	}

	@Override
	public String getHelp() {
		return BasicConstants.uiHelpLongNumberValidator;
	}

}
