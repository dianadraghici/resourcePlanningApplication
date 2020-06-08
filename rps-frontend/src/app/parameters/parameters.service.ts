import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Parameters} from "../models/parameters.model";
import {Constants} from "../utils/utils.constants";

@Injectable({
  providedIn: 'root'
})
export class ParametersService {

  constructor(private http: HttpClient) {
  }

  private userUrl = Constants.PARAMETER_CONTROLLER;

  public getStatusParameters() {
    return this.http.get<Parameters[]>(this.userUrl + Constants.GET_STATUS_PARAMETERS, {withCredentials: true});
  }

  public getPercentParameters() {
    return this.http.get<Parameters[]>(this.userUrl + Constants.GET_PERCENT_PARAMETERS, {withCredentials: true});
  }

  public getTechnologyParameters() {
    return this.http.get<Parameters[]>(this.userUrl + Constants.GET_TECHNOLOGY_PARAMETERS, {withCredentials: true});
  }

  public getPositionParameters() {
    return this.http.get<Parameters[]>(this.userUrl + Constants.GET_POSITION_PARAMETERS, {withCredentials: true});
  }
}
