package infodoc.basic.validator;

import com.vaadin.Application;
import com.vaadin.data.Validator;

public interface ValidatorFactory {
	
	Validator getValidator(String errorMessage, String param, Application application);
	
	String getHelp();

}
