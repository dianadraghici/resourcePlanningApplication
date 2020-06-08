import {Injectable} from "@angular/core";

@Injectable()
export class ErrorMessages {

    /** Global error messages*/
    public UNSATISFIED_MIN_LENGTH = "Unsatisfied number of characters";
    public UNSATISFIED_PERCENT_ID = "Unsatisfied certitude with the chosen status";
    public UNSATISFIED_STATUS_ID = "Unsatisfied status with the chosen certitude";
    public INVALID_FORMAT = "Invalid Format";

    /** Project error messages*/
    public PROJECT_CODE = "Code is mandatory";
    public PROJECT_STATUS = "Status is mandatory";
    public PROJECT_NAME = "Name is mandatory";
    public PROJECT_CERTITUDE = "Certitude is mandatory";
    public PROJECT_START_DATE = "Start Date is mandatory";
    public PROJECT_END_DATE = "End Date is mandatory";

    /** Member error messages */
    public MEMBER_STAFF = "Staff number is mandatory";
    public MEMBER_STAFF_ALREADY_REGISTERED = "Staff number is already registered";
    public MEMBER_LAST_NAME = "Last Name is mandatory";
    public MEMBER_FIRST_NAME = "First Name is mandatory";
    public MEMBER_TECHNOLOGY = "Technology is mandatory";
    public MEMBER_SAVED_ERROR = "Something wrong happened while processing the saving request";

    /** Project position error messages*/
    public PROJECT_POSITION_DATE_BEFORE_PROJECT = "Project Position Start Date cannot be before Project Start Date";
    public PROJECT_POSITION_DATE_AFTER_PROJECT = "Project Position End Date cannot be after Project End Date";
    public PROJECT_POSITION_PERCENT = "Percent is mandatory";
    public PROJECT_POSITION_POSITION = "Position is mandatory";
    public PROJECT_POSITION_NUMBER = "Number positions is mandatory";
    public PROJECT_POSITION_START_DATE = "Start Date is mandatory";
    public PROJECT_POSITION_END_DATE = "End Date is mandatory";


    /** Date error messages*/
    public START_DATE_ERROR_MESSAGE = "Start Date cannot be greater than End Date";
    public END_DATE_ERROR_MESSAGE = "End Date cannot be lower than Start Date";
    public NULL_START_DATE_ID = "Start Date does not exist";
    public NULL_END_DATE_ID = "End Date does not exist";
    public NULL_DATE_ID = "Date does not exist";



}
