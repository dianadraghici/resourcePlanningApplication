import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Project} from "../models/project.model";
import {Constants} from "../utils/utils.constants";
import {ProjectPosition} from "../models/project-position";
import {Observable} from "rxjs";
import {PageResponse} from "../models/page-response.model";
import {PageParams} from "../models/page-params.model";
import {Sort} from "../models/sort.model";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

    constructor(private http: HttpClient) {}

    private userUrl = Constants.PROJECT_CONTROLLER;

    public  getProjects() {
        return this.http.get<Project[]>(this.userUrl, {withCredentials: true});
    }

    public getProjectsPage(page: number, size?: number): Observable<PageResponse<Project>> {
        const url = `${this.userUrl}${Constants.GET_PAGINATED_PROJECTS}`;
        return this.http.get<PageResponse<Project>>(url, {withCredentials: true, params: PageParams.build(page, size, ProjectService.sortCriteria())});
    }

    public addProject(project) {
        return this.http.post(this.userUrl, project, {withCredentials: true});
    }

    public editProject(project) {
        return this.http.put(this.userUrl + Constants.UPDATE_PROJECT_BY_ID + project.id, project, {responseType: 'text', withCredentials: true});
    }

    public getProjectPositions(project) {
        return this.http.get<{ [key: string]: ProjectPosition[]; }>(Constants.PROJECT_POSITION_CONTROLLER + Constants.FIND_BY_ID_PROJECT_POSITION + project.id, {withCredentials: true});
    }

    public editProjectPosition(projectPosition) {
        return this.http.put(Constants.PROJECT_POSITION_CONTROLLER + Constants.UPDATE_PROJECT_POSITION, projectPosition, {
            responseType: 'text',
            withCredentials: true
        });
    }

    private static sortCriteria(): Sort {
        return PageParams.processSortCriteria(localStorage.getItem(Constants.SORT_ORDER_PROJECT));
    }
}
