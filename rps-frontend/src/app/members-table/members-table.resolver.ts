import {Injectable} from "@angular/core";
import {Resolve} from "@angular/router";
import {Observable, forkJoin} from "rxjs";
import {map} from 'rxjs/operators';
import {ParametersService} from "../parameters/parameters.service";
import {ProjectService} from "../project/project.service";
import {MembersTableService} from "./members-table.service";
import {ScheduleProjectsService} from "../schedule-projects/schedule-projects.service";


@Injectable()
export class MembersTableResolver implements Resolve<{}> {

    constructor(private parameterService: ParametersService,
                private scheduleProjects: ScheduleProjectsService,
                private memberTableService: MembersTableService,
                private projectService: ProjectService) {
    }

    resolve(): Observable<{}> {
        return forkJoin([
            this.memberTableService.getActiveMembers(),
            this.projectService.getProjects(),
            this.parameterService.getTechnologyParameters(),
            this.parameterService.getPercentParameters(),
            this.scheduleProjects.getListProjectPositions()
        ]).pipe(map(backendData => {
            return {
                activeMembers: backendData[0],
                projectsList: backendData[1],
                technologyList: backendData[2],
                percentList: backendData[3],
                projectPositionsMappedByProject: backendData[4],
            };
        }));
    }
}
