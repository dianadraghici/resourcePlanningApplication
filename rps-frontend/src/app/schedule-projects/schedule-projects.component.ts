import {Component, OnInit} from '@angular/core';
import {Project} from "../models/project.model";
import {ProjectPosition} from "../models/project-position";
import {CalendarService} from "../calendar/calendar.service";
import {Calendar} from "../models/calendar.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Parameters} from "../models/parameters.model";
import {ParametersService} from "../parameters/parameters.service";
import {CommonServicesService} from "../utils/common-services.service";
import {ScheduleMembersService} from "../schedule-members/schedule-members.service";
import {ActivatedRoute} from "@angular/router";
import {MemberPositionViewEntity} from "../models/member-position-view-entity.model";
import {Constants} from "../utils/utils.constants";


@Component({
    selector: 'app-schedule-projects',
    templateUrl: './schedule-projects.component.html',
    styleUrls: ['./schedule-projects.component.css']
})
export class ScheduleProjectsComponent implements OnInit {
    calendar: Map<string, Array<Calendar>>;
    keysCalendar: any[];
    quarterYearModel: string;
    idColumnNumber: number = 1;
    sumGeneralPercentage: number = 0;
    searchText: string;
    percentList: Parameters[];
    projectPositions: any[] = [];
    projects: Project[];
    membersPosition: any[];
    projectsWithDistinctPositions: any[] = [];
    positionList: Parameters[];
    fullProjectPositionsList: ProjectPosition[] = [];
    fullMembersPositionList: MemberPositionViewEntity[];
    flattenCalendarList: Calendar[];
    projectsWithoutAnyPositions: ProjectPosition[];
    dataToDisplay: any[];
    finalCollectionDataToDisplay: any[];
    projectNameSet: Set<string> = new Set<string>();


    constructor(private modalService: NgbModal, private scheduleMembers: ScheduleMembersService, private calendarService: CalendarService,
                private parameterService: ParametersService, public commonService: CommonServicesService, private route: ActivatedRoute) {
    }


    ngOnInit() {
        const graphicsData = this.route.snapshot.data['projectsGraphicsData'];
        this.positionList = graphicsData.positionList;
        this.percentList = graphicsData.percentList;

        this.flattenCalendarList = graphicsData.flattenCalendarList;
        this.calendar = graphicsData.calendar;
        this.keysCalendar = Object.keys(this.calendar);
        this.quarterYearModel = this.keysCalendar[0];

        this.projectPositions = graphicsData.projectPositions; /* get list of the project positions */
        this.membersPosition = graphicsData.membersPosition;

        this.projects = graphicsData.projects; /* get list of projects */

        this.populateFullProjectPositionsList();
        this.getUniquePositionsByProject();
        this.populateFullMembersPositionList();
        this.getProjectsWithoutAnyPositions();
        this.getTheCollectionDataToBeDisplay();

    }

    /** get all the members for the specified position for the current project
     * @param projectName - name of the project
     * @param positionDescription - description of the position for the current project
     * @param cal - calendar object of  current column (date) iterated
     * @param currentIdColumn - the current column id
      * */
    getTooltipDetailsForProjects(projectName: string, positionDescription: string, cal : any, currentIdColumn: string){
        /*reset tooltip details: at the beginning of every position*/
        cal.tooltipDetailsForProjects = "";
        /*the current member given by projectName and positionDescription*/
        let membersForCurrentPosition = this.getAllOccupiedPositionsFor(projectName, positionDescription);

        /*if the project has no member assigned, but has positions then return 'no member position'*/
        if(Object.keys(membersForCurrentPosition).length == 0) {
            cal.tooltipDetailsForProjects = Constants.NO_MEMBER_ASSIGNED;
        }else {
            /*for every position assigned to a project display all the members assigned*/
            membersForCurrentPosition.forEach((memberForCurrentPosition => {
                /*verify the start/end date for member position to bew between calendar header date*/
                if (memberForCurrentPosition.idCalendarStartDateFk <= cal.id && memberForCurrentPosition.idCalendarEndDateFk >= cal.id) {
                    cal.tooltipDetailsForProjects = cal.tooltipDetailsForProjects + " " +
                        memberForCurrentPosition.firstName + " " + memberForCurrentPosition.lastName + " - " +
                        memberForCurrentPosition.percentMember + Constants.PERCENT_SYMBOL;
                }
            }));
        }
        /*verify start/end date for position is not the same with start/end date for member position*/
        membersForCurrentPosition.forEach((memberForCurrentPosition => {
            if ((memberForCurrentPosition.occupied_projectPosition.startDateCalendarDTO.id < memberForCurrentPosition.idCalendarStartDateFk
                || memberForCurrentPosition.occupied_projectPosition.endDateCalendarDTO.id > memberForCurrentPosition.idCalendarEndDateFk) &&
                cal.tooltipDetailsForProjects == ""){
                cal.tooltipDetailsForProjects = Constants.NO_POSITION_ASSIGNED;
            }
        }));

        let columnId = document.getElementById(currentIdColumn);
        /*wait for columnId to be load*/
         if(columnId != undefined){
            /*test white rows*/
            if(columnId.style.backgroundColor == "rgb(255, 255, 255)"){
                /*reset tooltip details: set tooltip with null value when the project is over*/
                cal.tooltipDetailsForProjects = "";
            }

            /*if a row is grey means that there is no position assigned for the current project*/
            if(columnId.style.backgroundColor == "rgb(145, 145, 145)"){
                cal.tooltipDetailsForProjects = Constants.NO_POSITION_ASSIGNED_TO_PROJECT;
            }
        }
        return cal.tooltipDetailsForProjects;
    }

    /** @return the array list to be display when searching.*/
    search() {
        if (!this.searchText) {
            this.finalCollectionDataToDisplay = this.dataToDisplay;
        }
        if (this.searchText) {
            const searchList = this.fullProjectPositionsList.filter(projectPosition => projectPosition.projectName.toLocaleLowerCase().includes(this.searchText.toLocaleLowerCase()));
            const searchResultFromCommonService = this.commonService.filterIt(searchList, this.searchText);
            const finalResult = Array.from(new Set(searchResultFromCommonService.map(projectPosition => projectPosition.projectName)))
            // @ts-ignore
                .map(projectName => [projectName, searchResultFromCommonService.filter(projectPosition => projectPosition.projectName == projectName)]).flat(2)
                .filter((projectNameOrPosition, index, filteringArr) => !(typeof projectNameOrPosition === 'string' || projectNameOrPosition instanceof String) ? filteringArr.map(pp => `${pp.projectName}_${pp.positionDescription}`)
                    .indexOf(`${projectNameOrPosition.projectName}_${projectNameOrPosition.positionDescription}`) === index : projectNameOrPosition);

            this.finalCollectionDataToDisplay = finalResult.length != 0 ? finalResult : this.projects.map(project => project.projectName.toLocaleUpperCase())
                .filter(projectName => projectName.toLocaleLowerCase().includes(this.searchText.toLocaleLowerCase()));
        }

    }

    /** Allows the positions of the given project to be display/hide when the project name is clicked.
     * @param projectName - given project name (string).
     * */
    displayPositionsOnClickProject(projectName: string) {
        if (this.projectNameSet.has(projectName)) {
            this.projectNameSet.delete(projectName);
        } else {
            this.projectNameSet.add(projectName);
        }
        this.finalCollectionDataToDisplay = this.dataToDisplay.map(givenProjectName =>
            (this.projectNameSet.has(givenProjectName))
                ? [givenProjectName, this.projectsWithDistinctPositions.filter(ppd => ppd.id > 0 && ppd.projectName.toLocaleLowerCase() == givenProjectName.toLocaleLowerCase())]
                : givenProjectName)
        // @ts-ignore
            .flat(2);
    }

    /** get the color of project/positions column.
     * @param currentDisplayed - may be the current displayed project name (string) or the current displayed project position (ProjectPosition)
     * @param positionName - the positionDescription of the current displayed project position (ProjectPosition) if displayed or null/undefined if not.
     * @param cal - calendar object of the current column iterated.
     * @param currentIdColumn - id of the current column iterated.
     * */
    getColor(currentDisplayed: any, positionName: string, cal: any, currentIdColumn: string) {
        const idColumn = document.getElementById(currentIdColumn);
        idColumn.style.backgroundColor = "#FFFFFF";

        if (!!positionName) {
            idColumn.style.backgroundColor = "#92D050";
            cal = ({...cal, bop: new Date(cal.bop), eop: new Date(cal.eop)});
            if (this.isGivenPosition_Exists_OnTheGivenProject(currentDisplayed.projectName, positionName)) {
                const MPVEntities = this.getAllOccupiedPositionsFor(currentDisplayed.projectName, positionName);
                const projectPosition = this.projectPositions[currentDisplayed.projectName].filter(projectPosition => projectPosition.positionDescription == positionName)[0];

                if ((projectPosition.startDateCalendarDTO.id == cal.id || projectPosition.startDateCalendarDTO.id < cal.id) && projectPosition.endDateCalendarDTO.id >= cal.id
                    && this.projectPositions[currentDisplayed.projectName].statusDescription != "ABANDONED") {
                    this.sumGeneralPercentage = this.finalRatioOfOccupatiedPosition(currentDisplayed.projectName, positionName);
                }

                this.setColorWithinTheCurrentColumn(currentDisplayed.projectName, positionName, idColumn, cal, projectPosition, MPVEntities);
                this.setColorOutOfProjectDuration(idColumn, cal, projectPosition, MPVEntities);

            } else {
                idColumn.style.backgroundColor = "#872599";  //we should never see this color
            }

        } else {
            const givenProject = this.projects.filter(proj => proj.projectName.toLocaleLowerCase() == currentDisplayed.toLocaleLowerCase())[0];
            const projectPositionsColorSet = this.getColorSetOfProjectPositions(givenProject.projectName);
            if ((givenProject.startDateCalendarDTO.id == cal.id || givenProject.startDateCalendarDTO.id < cal.id) && givenProject.endDateCalendarDTO.id >= cal.id) {
                if (this.getAllPositionsFor(givenProject.projectName).length == 0) {
                    idColumn.style.backgroundColor = "#919191"; /*No position for the current project*/
                    this.contorId();
                } else if (projectPositionsColorSet.has('#FF0000')) {
                    idColumn.style.backgroundColor = '#FF0000';
                    this.contorId();
                } else if (projectPositionsColorSet.has('#FFC000')) {
                    idColumn.style.backgroundColor = '#FFC000';
                    this.contorId();
                } else if (projectPositionsColorSet.has('#FFFF00')) {
                    idColumn.style.backgroundColor = '#FFFF00';
                    this.contorId();
                } else if (projectPositionsColorSet.has('#00B0F0')) {
                    idColumn.style.backgroundColor = '#00B0F0';
                    this.contorId();
                } else if (projectPositionsColorSet.has('#0070C0')) {
                    idColumn.style.backgroundColor = '#0070C0';
                    this.contorId();
                } else if (projectPositionsColorSet.has('#000000')) {
                    idColumn.style.backgroundColor = '#000000';
                    this.contorId();
                } else if (projectPositionsColorSet.has('#92D050')) {
                    idColumn.style.backgroundColor = '#92D050';
                    this.contorId();
                }
            }
        }

        this.contorId();
        this.resetIntervalPercentage();
    }

    /** reset the idColumnNumber*/
    resetId() {
        this.idColumnNumber = 1;
    }

    /** Check if given object is a string or not.
     * @param givenObject - string candidate.
     * @return true if the givenObject is of type string/String, false otherwise.
     * */
    isInstanceofStr(givenObject: any) {
        return typeof (givenObject) === 'string' || givenObject instanceof String;
    }

    /** Create an id for the given object.
     * @param givenObject - the object which will get the created id.
     * @return the created id for the givenObject.
     * */
    getIdOfTheDisplayedColumn(givenObject: any): string {
        return this.isInstanceofStr(givenObject)
            ? `${this.idColumnNumber}-${givenObject}`
            : `${this.idColumnNumber}-${givenObject.projectName}-${givenObject.positionDescription}`;
    }

    /** reset the sumGeneralPercentage*/
    private resetIntervalPercentage() {
        this.sumGeneralPercentage = 0;
    }

    /** increments the idColumnNumber*/
    private contorId() {
        this.idColumnNumber++;
    }


    /** Get a color set for the givenProjectName.
     * @param givenProjectName - given project name (string).
     * @return a Set of hex colors for the givenProjectName.
     * */
    private getColorSetOfProjectPositions(givenProjectName: string): Set<any> {
        const getColorByPositionRatio = (ratio: number) => {
            if (ratio == 0) {
                return "#FF0000";
            } else if (ratio > 0 && ratio <= 0.25) {
                return "#FFC000";
            } else if (ratio > 0.25 && ratio <= 0.50) {
                return "#FFFF00";
            } else if (ratio > 0.50 && ratio <= 0.75) {
                return "#00B0F0";
            } else if (ratio > 0.75 && ratio < 1) {
                return "#0070C0";
            } else if (ratio == 1) {
                return "#92D050";
            } else if (ratio > 1) {
                return "#000000";
            }
        };

        return new Set(this.projectsWithDistinctPositions.filter(projectPosition => projectPosition.projectName == givenProjectName)
            .map(projectPosition => ({
                position: projectPosition.positionDescription,
                color: getColorByPositionRatio(this.finalRatioOfOccupatiedPosition(projectPosition.projectName, projectPosition.positionDescription))
            }))
            .map(givenObject => givenObject.color));

    }

    /** Set a color based a final ratio for the iterated position within the current column.
     * @param projectName - given project name.
     * @param positionName - given position name.
     * @param idColumn - id of the current iterated column.
     * @param cal - calendar object of the current column iterated.
     * @param projectPosition - given projectPosition iterated.
     * @param MPVEntities - collection of the assigned positions for the given position of the given project.
     * */
    private setColorWithinTheCurrentColumn(projectName: string, positionName: string, idColumn: HTMLElement, cal: any, projectPosition: ProjectPosition, MPVEntities: any[]) {
        if (this.sumGeneralPercentage == 0) {
            idColumn.style.backgroundColor = "#919191";  /*No position for the current project*/
            this.contorId();
        } else if (this.sumGeneralPercentage > 0 && this.sumGeneralPercentage <= 0.25) {
            idColumn.style.backgroundColor = "#FFC000";
            this.contorId();
        } else if (this.sumGeneralPercentage > 0.25 && this.sumGeneralPercentage <= 0.50) {
            idColumn.style.backgroundColor = "#FFFF00";
            this.contorId();
        } else if (this.sumGeneralPercentage > 0.50 && this.sumGeneralPercentage <= 0.75) {
            idColumn.style.backgroundColor = "#00B0F0";
            this.contorId();
        } else if (this.sumGeneralPercentage > 0.75 && this.sumGeneralPercentage < 1) {
            idColumn.style.backgroundColor = "#0070C0";
            this.contorId();
        } else if (this.sumGeneralPercentage == 1) {
            if (this.isGivenPosition_Exists_OnTheGivenProject(projectName, positionName)) {

                this.projectPositions[projectName].filter(projectPosition => projectPosition.positionDescription == positionName)
                    .forEach(projectPosition => {
                        if ((projectPosition.startDateCalendarDTO.id == cal.id || projectPosition.startDateCalendarDTO.id < cal.id)
                            && projectPosition.endDateCalendarDTO.id >= cal.id
                            && this.projectPositions[projectName].statusDescription == "ABANDONED") {
                            idColumn.style.backgroundColor = "#0070C0";
                            this.contorId();
                        }
                    });
            }
        } else if (this.sumGeneralPercentage > 1) {
            idColumn.style.backgroundColor = "#000000";
            this.contorId();
        }

        if (MPVEntities.length == 0) {
            if ((projectPosition.startDateCalendarDTO.id == cal.id || projectPosition.startDateCalendarDTO.id < cal.id)
                && projectPosition.endDateCalendarDTO.id >= cal.id) {
                idColumn.style.backgroundColor = "#FF0000"; /*No one assigned on the project*/
            }
        }

    }

    /** Set a color specific color out of the whole duration of the iterated project.
     * @param idColumn - id of the current iterated column.
     * @param cal - calendar object of the current column iterated.
     * @param projectPosition - given projectPosition iterated.
     * @param MPVEntities - collection of the assigned positions for the given position.
     * */
    private setColorOutOfProjectDuration(idColumn: HTMLElement, cal: Calendar, projectPosition: any, MPVEntities: any[]) {
        const currentProjectDTO = projectPosition.projectDTO;
        if ((MPVEntities.length != 0)
            && (currentProjectDTO.startDate.getTime() <= new Date(cal.bop).getTime() || currentProjectDTO.startDate.getTime() == new Date(cal.bop).getTime())
            && currentProjectDTO.endDate.getTime() < new Date(cal.eop).getTime()) {
            idColumn.style.backgroundColor = "#FFFFFF";
        }

        if (new Date(currentProjectDTO.startDateCalendarDTO.bop).getTime() > new Date(cal.bop).getTime()) {
            idColumn.style.backgroundColor = "#FFFFFF";
        } else if (new Date(currentProjectDTO.endDateCalendarDTO.eop).getTime() < new Date(cal.eop).getTime()) {
            idColumn.style.backgroundColor = "#FFFFFF";
        }
    }

    /** @return a collection of projectPositions without duplication of positions on each project. */
    private getUniquePositionsByProject() {
        return this.projectsWithDistinctPositions = this.fullProjectPositionsList.filter((projectPosition, index) =>
            this.fullProjectPositionsList
                .map(pp => `${pp.projectName}_${pp.positionDescription}`)
                .indexOf(`${projectPosition.projectName}_${projectPosition.positionDescription}`) === index)
    }

    /** Set on this.fullProjectPositionsList the collection of ProjectPosition for the fullProjectPositionsList. */
    private populateFullProjectPositionsList() {
        this.fullProjectPositionsList = Array.from(Object.entries(this.projectPositions))
            .map(mapArray => mapArray[1])
            .reduce((accumulator, currentArray) => [...accumulator, ...currentArray], [])
            .map(projectPos => {
                projectPos.positionDescription = this.positionList.filter(tech => tech.id == projectPos.positionId)[0].description; // set PositionDescription
                projectPos.percentDescription = this.percentList.filter(percent => percent.id == projectPos.percentId)[0].description; // set PercentDescription
                return projectPos;
            });
    }

    /** Set on this.fullMembersPositionList the collection of MemberPositionViewEntity for the fullMembersPositionList. */
    private populateFullMembersPositionList() {
        this.fullMembersPositionList = Object.entries(this.membersPosition)
            .map(fullArray => fullArray[1])
            .reduce((accumulator, currentArray) => [...accumulator, ...currentArray], [])
            .map(entity => {
                const mpve = new MemberPositionViewEntity(entity);
                if (!!entity.idProjectPositionFk) {
                    mpve.setOccupiedProjectPositionData(entity.idProjectPositionFk, this.fullProjectPositionsList);
                }
                if (!!entity.idCalendarStartDateFk && !!entity.idCalendarEndDateFk) {
                    mpve.setAssignmentIntervalDate(entity, this.flattenCalendarList);
                }
                return mpve;
            });
    }

    /** Set on this.projectsWithoutAnyPositions the collection of Project without any positions. */
    private getProjectsWithoutAnyPositions() {
        const displayProjIds = Array.from(new Set(this.projectsWithDistinctPositions
            .map(pp => pp.projectDTO)
            .map(proj => proj.id)
            .reduce((acc, curr) => [...acc, curr], [])));

        this.projectsWithoutAnyPositions = this.projects.filter(project => !displayProjIds.includes(project.id))
            .map((project, index) => {
                const pp = new ProjectPosition();
                pp.id = -(index + 1);
                pp.projectName = project.projectName;
                pp.projectDTO = project;
                return pp;
            });
    }

    /** Set on this.finalCollectionDataToDisplay the data to be display. */
    private getTheCollectionDataToBeDisplay() {
        this.projectsWithDistinctPositions = [...this.projectsWithDistinctPositions, ...this.projectsWithoutAnyPositions];
        this.finalCollectionDataToDisplay = this.dataToDisplay = Array.from(new Set(this.projectsWithDistinctPositions
            .map((projectPosition, idx, mappedArr) =>
                [projectPosition.projectName, mappedArr.filter(ppToDos => ppToDos.id > 0 && ppToDos.projectName == projectPosition.projectName)])
            .map(arr => arr[0])
            .map(givenProjectName => givenProjectName.toLocaleUpperCase())
            .sort()));
    }

    /** Get all the ProjectPosition for the given project.
     * @param projectName - name of the given project.
     * @return the ProjectPosition collection found.
     * */
    private getAllPositionsFor(projectName: string): ProjectPosition[] {
        return this.fullProjectPositionsList.filter(projectPosition => projectPosition.projectName.toLocaleLowerCase() == projectName.toLocaleLowerCase());
    }

    /** Get the number of the given position on the given project.
     * @param projectName - name of the given project.
     * @param positionName - given position name.
     * @return the number total of the given position found on the given project.
     * */
    private getTotalOfProjectPositionsFor(projectName: string, positionName: string): number {
        return this.getAllPositionsFor(projectName)
            .filter(projectPosition => projectPosition.positionDescription == positionName)
            .map(projectPosition => projectPosition.numberPositions)
            .reduce((accumulator, currentNumberPosition) => accumulator + currentNumberPosition, 0);
    }

    /** Get the total percent of the given position on the given project.
     * @param projectName - name of the given project.
     * @param positionName - given position name.
     * @return the percent total of the given position found on the given project.
     * */
    private getTotalOfProjectPositionsPercent(projectName: string, positionName: string): number {
        return this.getAllPositionsFor(projectName)
            .filter(projectPosition => projectPosition.positionDescription == positionName)
            .map(projectPosition => [{
                numberPositions: projectPosition.numberPositions,
                positionPercent: parseInt(projectPosition.percentDescription)
            }])
            .map(projectPosition => projectPosition[0].numberPositions * projectPosition[0].positionPercent)
            .reduce((accumulator, currentPositionPercent) => accumulator + currentPositionPercent, 0);
    }


    /** Get the collection of all assigned positions of the given position on the given project.
     * @param projectName - name of the given project.
     * @param positionName - given position name.
     * @return the MemberPositionViewEntity collection representing all assigned positions.
     * */
    private getAllOccupiedPositionsFor(projectName: string, positionName: string): MemberPositionViewEntity[] {
        return this.fullMembersPositionList
            .filter(memberPosition => !!memberPosition.projectName)
            .filter(memberPosition => memberPosition.projectName.toLocaleLowerCase() == projectName.toLocaleLowerCase())
            .filter(memberPosition => memberPosition.occupied_projectPosition.positionDescription.toLocaleLowerCase() == positionName.toLocaleLowerCase())
            .filter(memberPosition =>
                ((memberPosition.assignment_startDate.getTime() >= memberPosition.occupied_projectPosition.needed_startDate.getTime()))
                && ((memberPosition.assignment_endDate.getTime() <= memberPosition.occupied_projectPosition.needed_endDate.getTime()))
            );
    }

    /** Get the total percent of all assigned positions of the given position on the given project.
     * @param projectName - name of the given project.
     * @param positionName - given position name.
     * @return the total percent of all assigned positions of the given position found on the given project.
     * */
    private getTotalPercentForAllOccupiedPositions(projectName: string, positionName: string): number {
        return this.getAllOccupiedPositionsFor(projectName, positionName)
            .map(mpev => mpev.percentMember)
            .reduce((accumulator, currentPercentMember) => accumulator + currentPercentMember, 0);
    }

    /** Get the final ratio of the whole occupied positions of the given position on the given project.
     * @param projectName - name of the given project.
     * @param positionName - given position name.
     * @return the final ratio of the whole occupied positions.
     * */
    private finalRatioOfOccupatiedPosition(projectName: string, positionName: string): number {
        const totalPercentNeeded = this.getTotalOfProjectPositionsPercent(projectName, positionName);
        const totalPercentAllocated = this.getTotalPercentForAllOccupiedPositions(projectName, positionName);
        return ((100 * totalPercentAllocated) / totalPercentNeeded) / 100;
    }


    /** Checks if the given position exists on the given project.
     * @param projectName - name of the given project.
     * @param positionName - given position name.
     * @return true if the given position exists on the given project, false otherwise.
     * */
    private isGivenPosition_Exists_OnTheGivenProject(projectName: string, positionName: string) {
        return this.getTotalOfProjectPositionsFor(projectName, positionName) > 0;
    }

}



