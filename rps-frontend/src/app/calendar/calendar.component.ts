import { Component, OnInit } from '@angular/core';
import {Calendar} from "../models/calendar.model";
import {CalendarService} from "../calendar/calendar.service";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

    constructor(public calendarService: CalendarService) { }

    public calendarList: Calendar[];

    ngOnInit() {
        this.calendarService.getCalendarList()
            .subscribe( data => {
                this.calendarList = data;
            });
    }
}
