import { CalendarComponent } from './calendar.component';
import {of} from "rxjs";

describe('CalendarComponent', () => {
  let mockCalendarService;
  let component: CalendarComponent;

  const CALENDAR_LIST:  any[] = [
    {id:"FY19-P01-W01", fiscalYear:2019, period:1, week:1, bop:"2018-09-23", eop:"2018-09-29", quarter:"Q1"},
    {id:"FY19-P02-W06", fiscalYear:2019, period:2, week:6, bop:"2018-10-28", eop:"2018-11-03", quarter:"Q1"},
    {id:"FY19-P02-W07", fiscalYear:2019, period:2, week:7, bop:"2018-11-04", eop:"2018-11-10", quarter:"Q1"}];


  beforeEach(() => {
    mockCalendarService = jasmine.createSpyObj(['getCalendarList']);
  });

  it('should create calendar component', () => {
    //act
    component = new CalendarComponent(mockCalendarService);

    //assert
    expect(component).toBeTruthy();
  });

  it('should get a list of calendars', async() => {
    //arrange
    mockCalendarService.getCalendarList.and.returnValue(of(CALENDAR_LIST));
    component = new CalendarComponent(mockCalendarService);

    //act
    component.ngOnInit();

    //assert
    expect(component.calendarList).toEqual(CALENDAR_LIST);
  });

});
