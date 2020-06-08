import {Injectable} from '@angular/core';
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class CommonServicesService {
    objetsExported: boolean[]; //array of selected objects (true = selected, false = unselected)

    constructor(private cookieService: CookieService,
                private router: Router) {}

    /**
     * Enable the elements of the given array to be exported.
     * @param data - the initial data array to be exported.
     * */
    enableExportFor(data: any[]): void{
        this.objetsExported = data.map(() => true);
    }

    /**
     * Filter all fields from an given array.
     * @param searchKey - the search key.
     * @param arr - the initial array to filter.
     * @return an array containing only the elements having the search key.
     * */
    filterIt(arr: any, searchKey: any) {
        return arr.filter((obj: any) => {
            return Object.keys(obj).some((key) => {
                if (obj[key] !== null && key !== 'id') { //the id must not be considered when filtering.
                    const tempKey = obj[key];
                    const tempKeyStr = `${obj[key]}`.toLowerCase();
                    const tempSearch = `${searchKey}`.toLowerCase();
                    return tempKeyStr.includes(tempSearch) || Object.keys(tempKey).some((k) => {
                        if (k == "bop" || k == "eop") {
                            return tempKey[k].includes(tempSearch) || tempKey[k].includes(tempSearch);
                        }
                    });
                }
            });
        });
    }

    /**
     * Split the given array in multiple arrays as chunks - Good fit for a pagination functionality.
     * @param page - the page number represents the target chunk array to retrieve.
     * @param maxPerPage - the max of elements on each chunks.
     * @param dataSource - the initial source array to split.
     * @return an array containing the targeted chunk/page
     * */
    applyPageDataSource(page: number, maxPerPage: number, dataSource: any[]): any[] {
        const [datasource, chunkSize] = [[...dataSource], maxPerPage];
        return new Array(Math.ceil(datasource.length / chunkSize)).fill({}).map(el => datasource.splice(0, chunkSize))[page - 1]; // -1 because Arrays are 0 based and this pagination isn't.
    }

    /**
     * Convert a NgbDate to a normal Date.
     * @param ngDate - the NgbDate to convert.
     * @return a converted normal Date.
     * */
    converToDate(ngDate: NgbDate): Date {
        return new Date(ngDate.year, ngDate.month, ngDate.day);
    }

    /**
     * Convert a NgbDate to a string Date formatted like yyyy-MM-dd.
     * @param date - the NgbDate to convert.
     * @return the formatted string.
     * */
    mapNgDateToString(date: NgbDate): string {
        let finalNgbDateToString = '';
        if (date.month < 10 && date.day < 10) {
            finalNgbDateToString = date.year + "-0" + date.month + "-0" + date.day;
        } else if (date.month < 10) {
            finalNgbDateToString = date.year + "-0" + date.month + "-" + date.day;
        } else if (date.day < 10) {
            finalNgbDateToString = date.year + "-" + date.month + "-0" + date.day;
        } else {
            finalNgbDateToString = date.year + "-" + date.month + "-" + date.day;
        }
        return finalNgbDateToString;
    }


    /*** Delete the JSESSIONID cookie.* */
    deleteCookies(): void {
        if (this.cookieService.check('JSESSIONID')) {
            this.cookieService.delete('JSESSIONID');
        }
        this.router.navigate(['login']);
    }
}
