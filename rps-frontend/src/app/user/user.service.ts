import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {User} from '../models/user.model';
import {Constants} from "../utils/utils.constants";

@Injectable()
export class UserService {

    constructor(private http: HttpClient) {}

    private userUrl = Constants.USER_CONTROLLER;

    public getUsers() {
        return this.http.get<User[]>(this.userUrl, {withCredentials: true});
    }
}
