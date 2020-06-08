import { TestBed} from '@angular/core/testing';
import { CalendarService } from './calendar.service';
import {CommonServicesService} from "../utils/common-services.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Constants} from "../utils/utils.constants";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {Calendar} from "../models/calendar.model";

describe('CalendarService', () => {
  let httpClientMock;
  let mockCommonService;

  const CALENDAR_LIST:  any[] = [
    {id:"FY19-P01-W01", fiscalYear:2019, period:1, week:1, bop:"2018-09-23", eop:"2018-09-29", quarter:"Q1"},
    {id:"FY19-P02-W06", fiscalYear:2019, period:2, week:6, bop:"2018-10-28", eop:"2018-11-03", quarter:"Q1"},
    {id:"FY19-P02-W07", fiscalYear:2019, period:2, week:7, bop:"2018-11-04", eop:"2018-11-10", quarter:"Q1"}];

  beforeEach(() => {
     TestBed.configureTestingModule({
      providers: [CalendarService, {provide: CommonServicesService, useValue: jasmine.createSpyObj(['converToDate'])}],
      imports: [HttpClientTestingModule]
     });
    mockCommonService = TestBed.get(CommonServicesService);
  });


    it('should call getCalendarList() method when creating an instance of CalendarService', () => {
        httpClientMock = TestBed.get(HttpTestingController);
        const service = TestBed.get(CalendarService);
        expect(service).toBeTruthy();

        service.getCalendarList().subscribe(list => {
            expect(list).toEqual(CALENDAR_LIST[2]);
            expect(service.calendarList).toEqual([CALENDAR_LIST[0], CALENDAR_LIST[1]]);
        });
    });

    it('should get the map of calendar by quater year', () => {
        //Arrange
        httpClientMock = TestBed.get(HttpTestingController);
        const service: CalendarService = TestBed.get(CalendarService);
        const calendarMap: Map<string, Array<Calendar>> = new Map();
        calendarMap.set('2019/Q1', [
            {...CALENDAR_LIST[0]},
            {
                id: "FY19-P01-W02",
                fiscalYear: 2019,
                period: 1,
                week: 2,
                bop: "2018-09-30",
                eop: "2018-10-06",
                quarter: "Q1"
            }
        ]);
        //Act
        service.getCalendarMap().subscribe(calMap => {
            expect(calMap).toEqual(calendarMap);
        });
        //Assert
        const result = httpClientMock.expectOne(Constants.CALENDAR_CONTROLLER + Constants.GET_MAP_CALENDAR_BY_QUARTER_YEAR);
        expect(result.request.method).toBe('GET');
        expect(result.request['withCredentials']).toBeTruthy();
        result.flush(calendarMap);
    });

  it('should return true if the date is before or equal', () => {
    const service: CalendarService = TestBed.get(CalendarService);
    const first: Date = new Date(2018, 9, 20);
    const second: Date = new Date(2018, 9, 23);

    mockCommonService.converToDate.and.returnValues(first,second);

    const expected = service.dateBeforeOrEqual(new NgbDate(first.getFullYear(),first.getMonth(),first.getDay()), new NgbDate(second.getFullYear(),second.getMonth(),second.getDay()));
    expect(expected).toBe(true);
  });

  it('should return true if the date is after or equal', () => {
    const service : CalendarService = TestBed.get(CalendarService);
    const first: Date = new Date(2018, 6, 27);
    const second: Date = new Date(2018, 6, 23);

    mockCommonService.converToDate.and.returnValues(first,second);

    const expected = service.dateAfterOrEqual(new NgbDate(first.getFullYear(),first.getMonth(),first.getDay()), new NgbDate(second.getFullYear(),second.getMonth(),second.getDay()));
    expect(expected).toBe(true);
  });

  it('should map a date to a calendarFk', () => {
    const service = TestBed.get(CalendarService);
    service.calendarList = [CALENDAR_LIST[0]];

    let first: Date = new Date(2018, 9, 23);
    let second: Date = new Date(2018, 9, 29);
    mockCommonService.converToDate.and.returnValues(first,second, first, first );
    const result = service.mapDateToCalendarFK(new NgbDate(2018, 9, 23));

    expect(result).toEqual(CALENDAR_LIST[0].id);
  });

  it('should map a calendarFk to a date', () => {
    const service = TestBed.get(CalendarService);
    service.calendarList = CALENDAR_LIST;
       const expectedBop =  new NgbDate(
        +CALENDAR_LIST[0].bop.substring(0, 4),
        +CALENDAR_LIST[0].bop.substring(5, 7),
        +CALENDAR_LIST[0].bop.substring(8, 10));

    const result = service.mapCalendarFKToDate(CALENDAR_LIST[0].id, true);

    expect(result).toEqual(expectedBop);
  });

  it( 'should  map a calendarFk to a Bop Date', () => {
    const service = TestBed.get(CalendarService);
    service.calendarList = CALENDAR_LIST;
    const expectedBop =  new NgbDate(
        +CALENDAR_LIST[2].bop.substring(0, 4),
        +CALENDAR_LIST[2].bop.substring(5, 7),
        +CALENDAR_LIST[2].bop.substring(8, 10));

    const result = service.mapCalendarFKToDate(CALENDAR_LIST[2].id, true);

    expect(result).toEqual(expectedBop);
  });

  it( 'should  map a calendarFk to a Eop Date', () => {
    const service = TestBed.get(CalendarService);
    service.calendarList = CALENDAR_LIST;
    const expectedEop =  new NgbDate(
        +CALENDAR_LIST[2].eop.substring(0, 4),
        +CALENDAR_LIST[2].eop.substring(5, 7),
        +CALENDAR_LIST[2].eop.substring(8, 10));

    const result = service.mapCalendarFKToDate(CALENDAR_LIST[2].id, false);

    expect(result).toEqual(expectedEop);
  });

});
