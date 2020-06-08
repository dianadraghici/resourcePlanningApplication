import {TestBed} from '@angular/core/testing';
import {CommonServicesService} from './common-services.service';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";

describe('CommonServicesService', () => {
    const mockCookieService = jasmine.createSpyObj(['check', 'delete']);
    const mockRouter = jasmine.createSpyObj(['navigate']);
    const DUMMY_DATA = [
        {email: 'toma.popescu@cgi.com', password: 'toma12345'},
        {email: 'pascal.richard@test.com', password: 'pascal.cgi.12345'},
        {email: 'mario.platini@test.com', password: 'platini.12345', partners: ['FACEBOOK', 'IBM', 'GOOGLE']},
        {email: 'luiza.georgescu@test.com', password: 'luiza12345', partners: ['GOOGLE', 'NASA', 'CGI']}
    ];

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                {provide: CookieService, useValue: mockCookieService},
                {provide: Router, useValue: mockRouter},
            ]
        });
    });

    it('should create an instance of CommonServicesService', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        //Assert
        expect(service).toBeTruthy();
    });

    it('should objetsExported array contains only \'true\' values and has the same length of the passing array', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        const passingArray = ['one', 'two', 'three', 'ect'];

        //Act
        service.enableExportFor(passingArray);

        //Assert
        expect(service.objetsExported.length).toBe(passingArray.length);
        expect(service.objetsExported).toEqual([true, true, true, true]);
    });

    it('should return an array with only the elements having the search key word \'cgi\'', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        const arrayToFilter = DUMMY_DATA;

        //Act
        const result = service.filterIt(arrayToFilter, 'cgi');

        //Assert
        expect(result.length).toBe(3);
        expect(result).toEqual([arrayToFilter[0], arrayToFilter[1],arrayToFilter[3]]);
    });

    it('should paginate the given array', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        const arrayToSplit = DUMMY_DATA;
        //Act
        const firstPage = service.applyPageDataSource(1, 2,  arrayToSplit);
        const secondPage = service.applyPageDataSource(2, 2,  arrayToSplit);
        //Assert
        expect(firstPage.length).toBe(2);
        expect(secondPage.length).toBe(2);
        expect(firstPage).toEqual([arrayToSplit[0], arrayToSplit[1]]);
        expect(secondPage).toEqual([arrayToSplit[2], arrayToSplit[3]]);
    });

    it('should convert the given NgDate to a normal Date', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        const ngDateToConvert = new NgbDate(2019, 1, 31);
        //Act
        const result = service.converToDate(ngDateToConvert);
        //Assert
        expect(result).toEqual(new Date(2019, 1, 31));
        expect(result.getTime()).toBeCloseTo(new Date(2019, 1, 31).getTime());
    });

    it('should convert the given NgDate to a string Date formatted like yyyy-MM-dd', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        //Act
        const dateOne = service.mapNgDateToString(new NgbDate(2019, 1, 31));
        const dateTwo = service.mapNgDateToString(new NgbDate(2019, 10, 1));
        //Assert
        expect(dateOne).toEqual('2019-01-31');
        expect(dateTwo).toEqual('2019-10-01');
    });


    it('should check and delete the JSESSIONID cookie if exists and redirect to login page', () => {
        //Arrange
        const service: CommonServicesService = TestBed.get(CommonServicesService);
        mockCookieService.check.and.returnValue(true);
        //Act
        service.deleteCookies();
        //Assert
        expect(mockCookieService.check).toHaveBeenCalledWith('JSESSIONID');
        expect(mockCookieService.delete).toHaveBeenCalledWith('JSESSIONID');
        expect(mockRouter.navigate).toHaveBeenCalledWith(['login']);
    });

});
