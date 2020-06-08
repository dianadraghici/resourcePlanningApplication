package dania.app.web.utils;

public interface Constants {
    /* Calendar URL's */
    String CALENDAR_CONTROLLER = "/calendarController";

    /* health URL's */
    String HEALTH = "/health";

    /* Member URL's */
    String MEMBER_CONTROLLER = "/memberController";
    String DELETE_MEMBER = "/deleteMember/{id}";
    String DEACTIVATE_MEMBER = "/deactivateMember";
    String UPDATE_MEMBER = "/updateMember";
    String GET_INACTIVE_MEMBERS = "/getInactiveMembers";
    String GET_ACTIVE_MEMBERS = "/getActiveMembers";
    String FIND_MEMBER_BY_STAFF_NUMBER= "/findMemberByStaffNumber/{staffNumber}";
    String FIND_MEMBER_PAGINATED= "/paginated";
    String FIND_MEMBER_BY_FLAG_PAGINATED= "/active/{flag}/paginated";

    /* Member Position URL's */
    String MEMBER_POSITION_CONTROLLER = "/memberPositionController";
    String FIND_BY_ID_MEMBER_POSITION = "/findMemberPosition/{id}";
    String UPDATE_MEMBER_POSITION = "/updateMemberPosition";
    String DELETE_MEMBER_POSITION = "/deleteMemberPosition/{id}";

    /* Parameters URL's */
    String PARAMETER_CONTROLLER = "/parameterController";
    String GET_STATUS_PARAMETERS = "/getStatusParameters";
    String GET_PERCENT_PARAMETERS = "/getPercentParameters";
    String GET_TECHNOLOGY_PARAMETERS = "/getTechnologyParameters";
    String GET_POSITION_PARAMETERS = "/getPositionParameters";
    String STATUS_PARAMETERS_TYPE = "status";
    String PERCENT_PARAMETERS_TYPE = "percent";
    String TECHNOLOGY_PARAMETERS_TYPE = "tech";
    String POSITION_PARAMETERS_TYPE = "position";

    /* Project URL's */
    String PROJECT_CONTROLLER = "/projectController";
    String UPDATE_PROJECT_BY_ID = "/updateProject/{id}";
    String DELETE_PROJECT = "/deleteProject/{id}";
    String FIND_PROJECT_BY_CODE = "/findProjectByCode/{code}";

    /* Project Position URL's */
    String PROJECT_POSITION_CONTROLLER = "/projectPositionController";
    String FIND_BY_ID_PROJECT_POSITION = "/findProjectPosition/{id}";
    String UPDATE_PROJECT_POSITION = "/updateProjectPosition";
    String DELETE_PROJECT_POSITION = "/deleteProjectPosition/{id}";

    /* Schedule Member Controller URL's */
    String SCHEDULE_MEMBER_CONTROLLER = "/scheduleMemberController";
    String GET_MAP_CALENDAR_BY_QUARTER_YEAR = "/getMapCalendarByQuarterYear";
    String GET_MAP_POSITIONS_BY_MEMBER = "/getMapPositionsByMember";

    /* Schedule Project Controller URL's */
    String SCHEDULE_PROJECT_CONTROLLER = "/scheduleProjectController";
    String GET_MAP_POSITIONS_BY_PROJECT = "/getMapPositionsByProject";

    /* User Controller URL's */
    String USER_CONTROLLER = "/userController";

    /* Version Controller URL's */
    String VERSION_CONTROLLER = "/version";

    /* Pagination Requests */
    Integer MAX_ITEMS_PER_PAGE = 50;
}
