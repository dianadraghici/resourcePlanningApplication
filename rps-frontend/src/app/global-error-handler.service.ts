import {ErrorHandler, Injectable} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {CommonServicesService} from "./utils/common-services.service";


@Injectable()
export class CustomErrorHandlerService implements ErrorHandler {

    constructor(public commonService: CommonServicesService) {
    }

    handleError(error) {
        if (error instanceof HttpErrorResponse) {
            console.error('An error occurred from backend side: ', error.message);
            this.commonService.deleteCookies();
        } else {
            console.error('An error occurred from client side: ', error.message);
        }
    }
}