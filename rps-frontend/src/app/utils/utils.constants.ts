import {environment} from "../../environments/environment"

export class Constants {
    public static BASE_URL = environment.BACKEND_BASE_URL;
    public static CALENDAR_CONTROLLER = `${Constants.BASE_URL}calendarController`;
    public static MEMBER_CONTROLLER = `${Constants.BASE_URL}memberController`;
    public static MEMBER_POSITION_CONTROLLER = `${Constants.BASE_URL}memberPositionController`;
    public static DEACTIVATE_MEMBER = "/deactivateMember";
    public static DELETE_MEMBER_POSITION = "/deleteMemberPosition/";
    public static UPDATE_MEMBER = "/updateMember";
    public static PROJECT_POSITION_CONTROLLER = `${Constants.BASE_URL}projectPositionController`;
    public static FIND_BY_ID_MEMBER_POSITION = "/findMemberPosition/";
    public static DELETE_PROJECT_POSITION = "/deleteProjectPosition/";
    public static FIND_MEMBER_BY_STAFF_NUMBER = "/findMemberByStaffNumber/";
    public static GET_ACTIVE_MEMBERS = "/getActiveMembers";
    public static GET_INACTIVE_MEMBERS = "/getInactiveMembers";
    private static PAGINATED_ENDPOINT = "/paginated";
    public static GET_PAGINATED_ACTIVE_MEMBERS = "/active/1" + Constants.PAGINATED_ENDPOINT;
    public static GET_PAGINATED_INACTIVE_MEMBERS = "/active/0" + Constants.PAGINATED_ENDPOINT;

    public static PARAMETER_CONTROLLER = `${Constants.BASE_URL}parameterController`;
    public static GET_STATUS_PARAMETERS = "/getStatusParameters";
    public static GET_PERCENT_PARAMETERS = "/getPercentParameters";
    public static GET_TECHNOLOGY_PARAMETERS = "/getTechnologyParameters";
    public static FIND_TECHNOLOGY_BY_DESCRIPTION_PARAMETER = "/findTechnologyByDescriptionParameter";
    public static GET_POSITION_PARAMETERS = "/getPositionParameters";

    public static FIND_BY_ID_PROJECT_POSITION = "/findProjectPosition/";
    public static PROJECT_CONTROLLER = `${Constants.BASE_URL}projectController`;
    public static UPDATE_PROJECT_BY_ID = "/updateProject/";
    public static FIND_PROJECT_BY_CODE = "/findProjectByCode/";
    public static UPDATE_PROJECT_POSITION = "/updateProjectPosition";
    public static UPDATE_MEMBER_POSITION = "/updateMemberPosition";
    public static DELETE_PROJECT = "/deleteProject/";
    public static GET_PAGINATED_PROJECTS = Constants.PAGINATED_ENDPOINT;

    public static SCHEDULE_MEMBER_CONTROLLER = `${Constants.BASE_URL}scheduleMemberController`;
    public static SCHEDULE_PROJECT_CONTROLLER = `${Constants.BASE_URL}scheduleProjectController`;
    public static GET_MAP_CALENDAR_BY_QUARTER_YEAR = "/getMapCalendarByQuarterYear";
    public static GET_MAP_POSITIONS_BY_MEMBER = "/getMapPositionsByMember";

    public static GET_MAP_POSITIONS_BY_PROJECT = "/getMapPositionsByProject";

    public static USER_CONTROLLER = `${Constants.BASE_URL}userController`;
    public static LOGIN_CONTROLLER = `${Constants.BASE_URL}login`;
    public static LOGOUT_CONTROLLER = `${Constants.BASE_URL}logout`;

    public static SORT_ORDER_ACTIVE_MEMBERS = "sortOrderActiveMembers";
    public static SORT_ORDER_INACTIVE_MEMBERS = "sortOrderInactiveMembers";
    public static SORT_ORDER_PROJECT = "sortOrderProject";
    public static SORT_ORDER_MEMBER_ASCENDING_FIRSTNAME = "ascendingFirstName";
    public static SORT_ORDER_MEMBER_DESCENDING_FIRSTNAME = "descendingFirstName";
    public static SORT_ORDER_MEMBER_UNSORTED_FIRSTNAME = "unsortedFirstName";
    public static SORT_ORDER_MEMBER_ASCENDING_LASTNAME = "ascendingLastName";
    public static SORT_ORDER_MEMBER_DESCENDING_LASTNAME = "descendingLastName";
    public static SORT_ORDER_MEMBER_UNSORTED_LASTNAME = "unsortedLastName";
    public static SORT_ORDER_PROJECT_ASCENDING_CODE = "ascendingProjectCode";
    public static SORT_ORDER_PROJECT_DESCENDING_CODE = "descendingProjectCode";
    public static SORT_ORDER_PROJECT_UNSORTED_CODE = "unsortedProjectCode";
    public static SORT_ORDER_PROJECT_ASCENDING_NAME = "ascendingProjectName";
    public static SORT_ORDER_PROJECT_DESCENDING_NAME = "descendingProjectName";
    public static SORT_ORDER_PROJECT_UNSORTED_NAME = "unsortedProjectName";

    public static MAX_ITEMS_PER_PAGE_FOR_MAIN_PAGINATION = 50;
    public static MAX_ITEMS_PER_PAGE_FOR_SEARCH_PAGINATION = 50;
    public static PERCENT_SYMBOL = "%";
    public static NO_MEMBER_ASSIGNED = "No member assigned for this position";
    public static NO_POSITION_ASSIGNED = "No position assigned to a member";
    public static NO_POSITION_ASSIGNED_TO_PROJECT = "No position assigned to project";

    public static MEMBER_SAVED = "The member is successfully saved";
    public static PROJECT_CREATED = "Project is successfully created";
    public static POSITION_ASSIGNED = "Position is successfully assigned";
    public static ADD_POSITION_TO_PROJECT = "Position is successfully added to project";

    public static IN_BETWEEN_ASSIGNMENTS = "In between assignments";
}
