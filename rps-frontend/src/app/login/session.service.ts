import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Constants} from "../utils/utils.constants";
import {CookieService} from "ngx-cookie-service";
import {CustomErrorHandlerService} from "../global-error-handler.service";
import {CommonServicesService} from "../utils/common-services.service";
import {MembersTableService} from "../members-table/members-table.service";
import {interval, Observable, Subscription} from "rxjs";
import {catchError, switchMap, takeWhile, tap} from "rxjs/operators";


@Injectable()
export class SessionService {
    private userUrlLogin = Constants.LOGIN_CONTROLLER;
    private userUrlLogout = Constants.LOGOUT_CONTROLLER;

    constructor(private _http: HttpClient, private cookieService: CookieService,
                private customErrorHandlerService: CustomErrorHandlerService,
                public commonService: CommonServicesService) {}

    public clientId = 'newClient';
    public redirectUri = 'http://localhost:8089/';

    retrieveToken(code) {
        let params = new URLSearchParams();
        params.append('grant_type','authorization_code');
        params.append('client_id', this.clientId);
        params.append('client_secret', 'newClientSecret');
        params.append('redirect_uri', this.redirectUri);
        params.append('code',code);

        let headers =
            new HttpHeaders({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'});

        this._http.post('http://localhost:8080',
            params.toString(), { headers: headers })
            .subscribe(
                data => this.saveToken(data),
                err => alert('Invalid Credentials'));
    }

    saveToken(token) {
        var expireDate = new Date().getTime() + (1000 * token.expires_in);
        this.cookieService.set("access_token", token.access_token, expireDate);
    }

    getResource(resourceUrl) : Observable<any> {
        var headers = new HttpHeaders({
            'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
            'Authorization': 'Bearer '+this.cookieService.get('access_token')});
        return this._http.get(resourceUrl, { headers: headers })
            .pipe(
                catchError(
                    (error:any) => Observable.throw(error.json().error || 'Server error')
                )
            );
    }

    checkCredentials() {
        return this.cookieService.check('access_token');
    }

    logout() {
        this.cookieService.delete('access_token');
        window.location.reload();
    }
}
