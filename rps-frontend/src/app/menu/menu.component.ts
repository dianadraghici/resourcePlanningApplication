import {Component, OnInit} from '@angular/core';
import {TranslateService} from "../utils/translate.service";
import {Router} from "@angular/router";
import { AuthenticationService } from '../login/authentication.service';

@Component({
    selector: 'app-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

    lang: string;
    constructor(private translate: TranslateService,
                private loginService: AuthenticationService,
                private router: Router) {
    }

    ngOnInit() {
        this.lang = localStorage.getItem('lang') || this.setLang('fr');
    }

    setLang(lang: string) : string {
        this.translate.use(lang);
        this.lang = localStorage.getItem('lang');
        return this.lang;
    }

    logoutUser() {
        this.loginService.logout('Logout');
    }
}
