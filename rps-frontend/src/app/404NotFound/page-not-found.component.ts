import {Component} from '@angular/core';
import {Location} from "@angular/common";

@Component({
    templateUrl: './page-not-found.component.html',
    styleUrls: ['./page-not-found.component.css']
})
export class PageNotFoundComponent {

    constructor(public location: Location) {}

}
