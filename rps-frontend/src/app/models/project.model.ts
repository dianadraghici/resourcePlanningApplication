import {Calendar} from "./calendar.model";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";

export class Project{
    id: number;
    projectCode: string;
    projectName: string;
    statusId: number;
    percentId: number;
    startDateCalendarDTO: Calendar;
    endDateCalendarDTO: Calendar;
    statusDescription: string;
    percentDescription: string;
    /*start date and end date mapped as the following format yy-mm-dd*/
    startDateModel?: NgbDate;
    endDateModel?: NgbDate;
    /*bop and eop used for mapping the dates into strings for search method in project.component.ts*/
    bopString?: string;
    eopString?: string;
}