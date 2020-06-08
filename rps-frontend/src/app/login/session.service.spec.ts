import {TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {SessionService} from "./session.service";
import {CookieService} from "ngx-cookie-service";
import {CustomErrorHandlerService} from "../global-error-handler.service";
import {Router} from "@angular/router";
import {MembersTableService} from "../members-table/members-table.service";
import {Constants} from "../utils/utils.constants";
import {Login} from "../models/login.model";
import {of} from "rxjs";
import {CommonServicesService} from "../utils/common-services.service";
import {HttpErrorResponse} from "@angular/common/http";

describe('SessionService', () => {

    let httpMock: HttpTestingController;
    const mockRouter = jasmine.createSpyObj(['navigate']);
    const mockCookieService = jasmine.createSpyObj(['check']);
    const mockCommonServicesService = jasmine.createSpyObj(['deleteCookies']);
    const mockCustomErrorHandlerService = jasmine.createSpyObj(['handleError']);
    const mockMembersTableService = jasmine.createSpyObj(['checkBackendHealth']);

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [
                SessionService,
                {provide: Router, useValue: mockRouter},
                {provide: CookieService, useValue: mockCookieService},
                {provide: CommonServicesService, useValue: mockCommonServicesService},
                {provide: CustomErrorHandlerService, useValue: mockCustomErrorHandlerService},
                {provide: MembersTableService, useValue: mockMembersTableService},
            ]
        });
        httpMock = TestBed.get(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
        TestBed.get(SessionService).pingBackendSide('UNSUBSCRIBE');
    });

    it('should create an instance of SessionService', () => {
        const sessionService = TestBed.get(SessionService);
        expect(sessionService).toBeTruthy();
        expect(sessionService.startPingBackendSideSubscription).toBeTruthy();
    });

    it(`should the session be 'true', the checkInterval be '60000' and the startPingBackendSideSubscription be 'truthy' at the service initialization`, () => {
        const {alive, interval, startPingBackendSideSubscription} = TestBed.get(SessionService);
        expect(alive).toBe(true);
        expect(interval).toBe(60000);
        expect(startPingBackendSideSubscription.closed).toBeFalsy();
    });

    it(`should check if 'userUrlLogin = Constants.LOGIN_CONTROLLER' and 'userUrlLogout = Constants.LOGOUT_CONTROLLER'`, () => {
        const {userUrlLogin, userUrlLogout} = TestBed.get(SessionService);
        expect(userUrlLogin).toBe(Constants.LOGIN_CONTROLLER);
        expect(userUrlLogout).toBe(Constants.LOGOUT_CONTROLLER);
    });

    it('should successfully log in the given user', () => {
        //Arrange
        const sessionService: SessionService = TestBed.get(SessionService);
        const userAttemptingLogin: Login = {user: 'gigel.popescu', credential: 'abcd123456'};
        const userLoggedReturnFromBackend = {
            id: 123,
            email: `${userAttemptingLogin.user}@cgi.com`, ...userAttemptingLogin
        };
        //Act
        sessionService.login(userAttemptingLogin).subscribe(givenUser => {
            expect(givenUser).toEqual(userLoggedReturnFromBackend);
            expect((sessionService as any).alive).toEqual(true);
            expect((sessionService as any).startPingBackendSideSubscription.closed).toBeFalsy();
        });
        //Assert
        const req = httpMock.expectOne(Constants.LOGIN_CONTROLLER);
        expect(req.request.method).toBe("POST");
        expect(req.request.withCredentials).toBe(true);

        req.flush(userLoggedReturnFromBackend);
    });

    it('should successfully logout the loggedIn user', () => {
        //Arrange
        const sessionService: SessionService = TestBed.get(SessionService);
        //Act
        sessionService.logout().subscribe(() => {
            expect(mockCommonServicesService.deleteCookies).toHaveBeenCalled();
            expect((sessionService as any).alive).toBeFalsy();
            expect((sessionService as any).startPingBackendSideSubscription.closed).toBeTruthy();
        });
        //Assert
        const req = httpMock.expectOne(Constants.LOGOUT_CONTROLLER);
        expect(req.request.url).toBe(Constants.LOGOUT_CONTROLLER);
        expect(req.request.method).toBe("POST");

        req.flush(null);
    });

    it('should simulate an error event happened when logout() was called', () => {
        //Arrange
        const sessionService: SessionService = TestBed.get(SessionService);
        //Act
        sessionService.logout().subscribe(() => {
        }, (httpErrorRes: HttpErrorResponse) => {
            expect(httpErrorRes.error.type).toBe('SERVER ERROR');
            expect(mockCustomErrorHandlerService.handleError).toHaveBeenCalledWith(httpErrorRes);
            expect(mockCommonServicesService.deleteCookies).toHaveBeenCalledTimes(2);
        });
        //Assert
        const req = httpMock.expectOne(Constants.LOGOUT_CONTROLLER);
        expect(req.request.url).toBe(Constants.LOGOUT_CONTROLLER);
        expect(req.request.method).toBe("POST");

        req.error(new ErrorEvent('SERVER ERROR'));
    });

    it(`should return 'true' when isAuthenticated() is called`, () => {
        const sessionService: SessionService = TestBed.get(SessionService);
        mockCookieService.check.and.returnValue(true);
        expect(sessionService.isAuthenticated()).toBe(true);
    });


    it(`should ping BackendSide to check health and reply once 'is alive'`, () => {
        //Arrange
        const sessionService: SessionService = TestBed.get(SessionService);
        mockMembersTableService.checkBackendHealth.and.returnValue(of('is alive'));
        (sessionService as any).interval = 1000;
        //Act
        const subscription = sessionService.startPingBackendSide().subscribe(response => {
            //Assert
            expect(response).toEqual('is alive');
            expect(mockMembersTableService.checkBackendHealth).toHaveBeenCalledTimes(1);
            subscription.unsubscribe();
        });
    });

});
