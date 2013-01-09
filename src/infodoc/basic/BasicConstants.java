package infodoc.basic;

import infodoc.core.InfodocConstants;
import enterpriseapp.Utils;


public class BasicConstants extends InfodocConstants {
	
	public static final Integer infodocBasicNumberOfLastActivities = Integer.parseInt(Utils.getProperty("infodoc.basic.numberOfLastActivities"));
	public static final String infodocBasicDefaultDashboardUrl = Utils.getProperty("infodoc.basic.defaultDashboardUrl");
	public static final String uiActivityExecuted = Utils.getProperty("ui.activityExecuted");
	public static final String uiAutomaticallyExecutedActivity = Utils.getProperty("ui.automaticallyExecutedActivity");
	public static final String uiAssignTo(String destinos) { return Utils.getProperty("ui.assignTo", new String[] {destinos}); }
	public static final String uiEmptySummary = Utils.getProperty("ui.emptySummary");
	public static final String uiEmptyCasesList = Utils.getProperty("ui.emptyCasesList");
	public static final String uiEmptyActivitiesList = Utils.getProperty("ui.emptyActivitiesList");
	public static final String uiSeeMore = Utils.getProperty("ui.seeMore", "");
	public static final String uiMyCases = Utils.getProperty("ui.myCases");
	
	public static final String uiHelpFileField = Utils.getProperty("ui.help.fileField");
	public static final String uiHelpTextAreaField = Utils.getProperty("ui.help.textAreaField");
	public static final String uiHelpBooleanField = Utils.getProperty("ui.help.booleanField");
	public static final String uiHelpMultipleClassificationsField = Utils.getProperty("ui.help.multipleClassificationsField");
	public static final String uiHelpSingleClassificationField = Utils.getProperty("ui.help.singleClassificationField");
	public static final String uiHelpSingleAssignedByMeCaseField = Utils.getProperty("ui.help.singleAssignedByMeCaseField");
	public static final String uiHelpMultipleAssignedByMeCasesField = Utils.getProperty("ui.help.multipleAssignedByMeCasesField");
	public static final String uiHelpMultipleCasesField = Utils.getProperty("ui.help.multipleCasesField");
	public static final String uiHelpSingleCaseField = Utils.getProperty("ui.help.singleCaseField");
	public static final String uiHelpDateField = Utils.getProperty("ui.help.dateField");
	public static final String uiHelpSingleUserGroupField = Utils.getProperty("ui.help.singleUserGroupField");
	public static final String uiHelpTextField = Utils.getProperty("ui.help.textField");
	public static final String uiHelpSingleUserField = Utils.getProperty("ui.help.singleUserField");
	
	public static final String uiHelpAssignSingleUserGroupActivity = Utils.getProperty("ui.help.assignSingleUserGroupActivity");
	public static final String uiHelpAssignMultipleUserGroupsActivity = Utils.getProperty("ui.help.assignMultipleUserGroupsActivity");
	public static final String uiHelpAssignSingleUserActivity = Utils.getProperty("ui.help.assignSingleUserActivity");
	public static final String uiHelpAssignMultipleUsersActivity = Utils.getProperty("ui.help.assignMultipleUsersActivity");
	public static final String uiHelpCreateActivity = Utils.getProperty("ui.help.createActivity");
	public static final String uiHelpUpdateActivity = Utils.getProperty("ui.help.updateActivity");
	public static final String uiHelpUnassignActivity = Utils.getProperty("ui.help.unassignActivity");
	public static final String uiHelpTransferActivity = Utils.getProperty("ui.help.transferActivity");
	
	public static final String uiHelpEmailValidator = Utils.getProperty("ui.help.EmailValidator");
	public static final String uiHelpLongNumberValidator = Utils.getProperty("ui.help.LongNumberValidator");
	public static final String uiHelpMinStringLengthValidator = Utils.getProperty("ui.help.MinStringLengthValidator");
	public static final String uiHelpMaxStringLengthValidator = Utils.getProperty("ui.help.MaxStringLengthValidator");
	public static final String uiHelpRegexpValidator = Utils.getProperty("ui.help.RegexpValidator");
	
	public static final String uiErrorUserNotAllowedToExecuteActivity = Utils.getProperty("ui.errorUserNotAllowedToExecuteActivity");
	
}
