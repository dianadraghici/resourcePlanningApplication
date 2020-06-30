import { Injectable } from '@angular/core';
import { HttpParams, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StoreService } from './store-service';
import { User } from './user.model';
import { environment } from 'src/environments/environment';

export class UserServiceFilter {
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService extends StoreService<User> {

    signinUrl: string;

    constructor(http: HttpClient) {
            super(User, http, environment.BACKEND_BASE_URL + 'users', environment.BACKEND_BASE_URL + 'users/search');
            this.signinUrl = environment.signinUrl;
    }

    prepareData(item: User): Object {
        return item;
    }

    applyFilter(url: string, filter: UserServiceFilter, params: HttpParams): { url: string; params: HttpParams; } {
        if (filter && filter.email) {
            url = this.searchUrl;
            params = params.set('email', filter.email);
        }
        return { url, params };
      }

    findByEmail(email: string): Observable<User> {
        return this.http.get<User>(this.collectionUrl + '/findByEmail', { params: new HttpParams().set('email', email) });
    }

    signin(email: string, password: string): Observable<User> {
        console.log(`signin ${email} ${password}`);
        return this.http.post<User>(this.signinUrl, new HttpParams().set('email', email).set('password', password));
    }

    validateEmail(email: string): Observable<boolean> {
        console.log(`validateEmail ${email}`);
        return this.http.post<boolean>(this.signinUrl + '/validateEmail', new HttpParams().set('email', email));
    }

}
