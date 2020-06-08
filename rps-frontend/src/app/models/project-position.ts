import {Project} from "./project.model";
import {Calendar} from "./calendar.model";

export class ProjectPosition {
    id: number;
    positionId: number;
    numberPositions: number;
    percentId: number;
    projectDTO: Project;
    projectName?: string;
    startDateCalendarDTO: Calendar;
    endDateCalendarDTO: Calendar;
    positionDescription: string;
    percentDescription: string;
    idCalendarStartDateFk: string;
    idCalendarEndDateFk: string;
}
