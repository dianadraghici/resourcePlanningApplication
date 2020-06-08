import {SessionService} from "../login/session.service";
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable()
export class AuthGuard implements CanActivate {


    constructor(private _authService: SessionService, private _router: Router) {
    }

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        // if (this._authService.isAuthenticated()){
        //     return true;
        // }
        //
        // // if user is not authenticated navigate to login page
        // this._router.navigate(['/login'],  { queryParams: { returnUrl: state.url }});
        return true;
    }
}