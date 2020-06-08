import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Constants} from "../utils/utils.constants";
import {CalendarService} from "../calendar/calendar.service";

@Injectable({
  providedIn: 'root'
})
export class ScheduleMembersService {

  constructor(private http: HttpClient, private calendarService: CalendarService) { }

  private scheduleMemberUrl = Constants.SCHEDULE_MEMBER_CONTROLLER;

  public getCalendar() {
    return this.calendarService.getCalendarMap();
  }

  public getProjectPositionMembers(){
    return this.http.get<any[]>(this.scheduleMemberUrl + Constants.GET_MAP_POSITIONS_BY_MEMBER, {withCredentials: true});
  }

}
