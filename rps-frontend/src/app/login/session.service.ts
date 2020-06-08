import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Constants} from "../utils/utils.constants";
import {CookieService} from "ngx-cookie-service";
import {CustomErrorHandlerService} from "../global-error-handler.service";
import {CommonServicesService} from "../utils/common-services.service";
import {MembersTableService} from "../members-table/members-table.service";
import {interval, Observable, Subscription} from "rxjs";
import {switchMap, takeWhile, tap} from "rxjs/operators";


@Injectable()
export class SessionService {
    private alive: boolean; // used to unsubscribe from the TimerObservable// when OnDestroy is called.
    private interval: number;
    private userUrlLogin = Constants.LOGIN_CONTROLLER;
    private userUrlLogout = Constants.LOGOUT_CONTROLLER;
    private startPingBackendSideSubscription: Subscription;

    constructor(private http: HttpClient, private cookieService: CookieService,
                private customErrorHandlerService: CustomErrorHandlerService,
                public commonService: CommonServicesService,
                private membersTableService: MembersTableService) {
        this.alive = true;
        this.interval = 60000;
        this.pingBackendSide('SUBSCRIBE');
    }

    public login(login) {
        this.alive = true;
        this.pingBackendSide('SUBSCRIBE');
        return this.http.post(this.userUrlLogin, login, {withCredentials: true})
    }

    public logout() {
        this.alive = false;
        this.pingBackendSide('UNSUBSCRIBE');
        return this.http.post(this.userUrlLogout, null).pipe(tap(
            () => this.commonService.deleteCookies(),
            error => {
                this.customErrorHandlerService.handleError(error);
                this.commonService.deleteCookies();
            }));
    }

    public isAuthenticated() {
        return !!this.cookieService.check('JSESSIONID');
    }

    public startPingBackendSide(): Observable<string> {
        return interval(this.interval).pipe(
            takeWhile(() => this.alive),
            switchMap(() => this.membersTableService.checkBackendHealth()),
            tap(console.log, error => {
                this.alive = false;
                this.customErrorHandlerService.handleError(error);
            }));
    }

    private pingBackendSide(action: 'SUBSCRIBE' | 'UNSUBSCRIBE'): void {
        if (action === 'SUBSCRIBE') {
            if (!!this.startPingBackendSideSubscription) this.startPingBackendSideSubscription.unsubscribe();
            this.startPingBackendSideSubscription = this.startPingBackendSide().subscribe();
        } else if (action === 'UNSUBSCRIBE') {
            if (!!this.startPingBackendSideSubscription) this.startPingBackendSideSubscription.unsubscribe();
        }
    }
}
