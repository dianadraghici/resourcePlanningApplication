import {ProjectPosition} from "./project-position";
import {Calendar} from "./calendar.model";

export class MemberPositionViewEntity {

    id: string;
    lastName: string;
    firstName: string;
    occupied_projectPosition = null;
    percentPosition: number;
    percentMember: number;
    percentProject: number;
    statusProject: string;
    assignment_startDate: Date;
    assignment_endDate: Date;
    projectName: string;
    idCalendarStartDateFk: string;
    idCalendarEndDateFk: string;

    constructor(viewEntityFromBackend) {
        this.id = viewEntityFromBackend.id;
        this.lastName = viewEntityFromBackend.lastName;
        this.firstName = viewEntityFromBackend.firstName;
        this.percentPosition = parseInt(viewEntityFromBackend.percentPosition);
        this.percentMember = parseInt(viewEntityFromBackend.percentMember);
        this.percentProject = parseInt(viewEntityFromBackend.percentProject);
        this.statusProject = viewEntityFromBackend.statusProject;
        this.projectName = viewEntityFromBackend.projectName;
        this.idCalendarStartDateFk = viewEntityFromBackend.idCalendarStartDateFk;
        this.idCalendarEndDateFk = viewEntityFromBackend.idCalendarEndDateFk;
    }

    setOccupiedProjectPositionData(idProjectPositionFk: number, fullProjectPositionsList: ProjectPosition[]) {
        this.occupied_projectPosition = fullProjectPositionsList.filter(projectPosition => projectPosition.id == idProjectPositionFk)[0];

        this.occupied_projectPosition.needed_startDate = new Date(this.occupied_projectPosition.startDateCalendarDTO.bop);
        this.occupied_projectPosition.needed_endDate = new Date(this.occupied_projectPosition.endDateCalendarDTO.eop);

        this.occupied_projectPosition.projectDTO.startDate = new Date(this.occupied_projectPosition.projectDTO.startDateCalendarDTO.bop);
        this.occupied_projectPosition.projectDTO.endDate = new Date(this.occupied_projectPosition.projectDTO.endDateCalendarDTO.eop);
    }

    setAssignmentIntervalDate(entityFromBackend, calendarArr: Calendar[]) {
        this.assignment_startDate = new Date(calendarArr.filter(cal => cal.id == entityFromBackend.idCalendarStartDateFk)[0].bop);
        this.assignment_endDate = new Date(calendarArr.filter(cal => cal.id == entityFromBackend.idCalendarEndDateFk)[0].eop);
    }

}