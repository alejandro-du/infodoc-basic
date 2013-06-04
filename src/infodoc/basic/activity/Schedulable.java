package infodoc.basic.activity;

import infodoc.core.dto.PropertyValue;

import java.util.Collection;

public interface Schedulable {
	
	void parseParams();
	
	String getCronExpression(Collection<PropertyValue> propertyValues);
	
	Long getScheduleActivityId();

}
