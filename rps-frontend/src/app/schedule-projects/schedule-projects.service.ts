import {Injectable} from '@angular/core';
import {Constants} from "../utils/utils.constants";
import {HttpClient} from "@angular/common/http";
import {CalendarService} from "../calendar/calendar.service";
import {ParametersService} from "../parameters/parameters.service";
import {switchMap, tap} from "rxjs/operators";
import {ProjectPosition} from "../models/project-position";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ScheduleProjectsService {

    private scheduleProjectUrl = Constants.SCHEDULE_PROJECT_CONTROLLER;

    constructor(private http: HttpClient, private calendarService: CalendarService, private parameterService: ParametersService) {
    }

    public getCalendar() {
        return this.calendarService.getCalendarMap();
    }

    public getListProjectPositions(): Observable<{string: ProjectPosition[]}> {
        return this.parameterService.getPositionParameters()
            .pipe(switchMap(positionList =>
                this.http.get<{string: ProjectPosition[]}>(this.scheduleProjectUrl + Constants.GET_MAP_POSITIONS_BY_PROJECT, {withCredentials: true})
                    .pipe(tap(projectPositionsMap => {
                        Object.keys(projectPositionsMap)
                            .map(key => projectPositionsMap[key]
                                .map(projectPosition => {
                                    const {description} = positionList.find(pos => pos.id == projectPosition.positionId);
                                    projectPosition.positionDescription = description;
                                    return projectPosition;
                                })
                            );
                    }))
            ))
    }
}
