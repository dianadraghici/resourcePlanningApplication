import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Constants} from "../../../utils/utils.constants";

@Injectable({
    providedIn: 'root'
})
export class ProjectPositionService {

    constructor(private http: HttpClient) {}

    private userUrl = Constants.PROJECT_POSITION_CONTROLLER;

    public addProjectPosition(projectPosition) {
        return this.http.post(this.userUrl, projectPosition, {withCredentials: true});
    }

    public deleteProjectPosition(projectPosition){
        return this.http.delete(this.userUrl + Constants.DELETE_PROJECT_POSITION + projectPosition.id, {withCredentials: true});
    }
}
