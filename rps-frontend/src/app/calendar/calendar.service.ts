import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Calendar} from "../models/calendar.model";
import {Constants} from "../utils/utils.constants";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {CommonServicesService} from "../utils/common-services.service";


@Injectable({
  providedIn: 'root'
})
export class CalendarService {

    constructor(private http: HttpClient, private commonService : CommonServicesService) {
        this.getCalendarList().subscribe( data => this.calendarList = data );
    }

    private calendarUrl = Constants.CALENDAR_CONTROLLER;
    private calendarList: Calendar[] = [];

    public getCalendarList() {
        return this.http.get<Calendar[]>(this.calendarUrl, {withCredentials: true});
    }

    public getCalendarMap() {
        return this.http.get<Map<string,any[]>>(this.calendarUrl + Constants.GET_MAP_CALENDAR_BY_QUARTER_YEAR, {withCredentials: true});
    }

    dateBeforeOrEqual(date : NgbDate, other : NgbDate) {
        return this.commonService.converToDate(date) <= this.commonService.converToDate(other);
    }

    dateAfterOrEqual(date : NgbDate, other : NgbDate) {
        return this.commonService.converToDate(date) >= this.commonService.converToDate(other);
    }

    mapDateToCalendarFK(date: NgbDate): string {
        const filtered = this.calendarList.filter((c: Calendar) =>
            this.dateBeforeOrEqual(date, new NgbDate(+c.eop.substring(0, 4), +c.eop.substring(5, 7), +c.eop.substring(8, 10)))
            &&
            this.dateAfterOrEqual(date, new NgbDate( +c.bop.substring(0, 4), +c.bop.substring(5, 7), +c.bop.substring(8, 10)))
        );
        if(filtered.length === 0) {
            return null;
        } else {
            return filtered[0].id;
        }
    }

    mapCalendarFKToDate(calendarFK: string, isStartDate: boolean): NgbDate {
        const filtered = this.calendarList.filter((c: Calendar) => c.id == calendarFK );
        if(filtered.length === 0) {
            return null;
        } else {
            let c: string = "";
            if(isStartDate) {
                c = filtered[0].bop;
            }else {
                c = filtered[0].eop;
            }
            return new NgbDate(+c.substring(0,4), +c.substring(5, 7), +c.substring(8,10));
        }
    }
}
