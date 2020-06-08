import {ProjectPosition} from "./project-position";
import {Calendar} from "./calendar.model";
import {Member} from "./member.model";

export class MemberPosition {
    id: number;
    percentId: number;
    projectPositionDTO: ProjectPosition;
    memberDTO: Member;
    startDateCalendarDTO: Calendar;
    endDateCalendarDTO: Calendar;
    positionDescription: string;
    percentDescription: string;
}
