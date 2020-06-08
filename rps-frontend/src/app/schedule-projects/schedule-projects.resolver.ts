import {Injectable} from "@angular/core";
import {Resolve} from "@angular/router";
import {Observable, forkJoin} from "rxjs";
import {map} from 'rxjs/operators';
import {ParametersService} from "../parameters/parameters.service";
import {ScheduleProjectsService} from "./schedule-projects.service";
import {ScheduleMembersService} from "../schedule-members/schedule-members.service";
import {ProjectService} from "../project/project.service";


@Injectable()
export class ScheduleProjectsResolver implements Resolve<any> {

    constructor(private parametersService: ParametersService, private scheduleProjects: ScheduleProjectsService,
                private scheduleMembers: ScheduleMembersService, private projectService: ProjectService) {
    }

    resolve(): Observable<any> {
        return forkJoin([
            this.parametersService.getPositionParameters(),
            this.parametersService.getPercentParameters(),
            this.scheduleProjects.getCalendar(),
            this.scheduleProjects.getListProjectPositions(),
            this.scheduleMembers.getProjectPositionMembers(),
            this.projectService.getProjects()
        ]).pipe(map(backendData => {
            return {
                positionList: backendData[0],
                percentList: backendData[1],
                calendar: backendData[2],
                flattenCalendarList: Object.entries(backendData[2])
                    .map(mapArray => mapArray[1])
                    .reduce((accumulator, currentArray) => [...accumulator, ...currentArray], []),
                projectPositions: backendData[3],
                membersPosition: backendData[4],
                projects: backendData[5]
            };
        }));
    }


}
