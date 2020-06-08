export class ProjectExport{
    projectCode: string;
    projectName: string;
    statusDescription: string;
    percentDescription: string;
    startDateCalendarDTO: string;
    endDateCalendarDTO: string;

    constructor(projectCode, projectName, statusDescription, percentDescription, startDateCalendarDTO, endDateCalendarDTO){
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.statusDescription = statusDescription;
        this.percentDescription = percentDescription;
        this.startDateCalendarDTO = startDateCalendarDTO;
        this.endDateCalendarDTO = endDateCalendarDTO;
    }
}