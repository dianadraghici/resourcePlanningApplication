import {Component, OnInit} from '@angular/core';
import {TranslateService} from "../utils/translate.service";
import {SessionService} from "../login/session.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

    lang: string;
    constructor(private translate: TranslateService,
                private loginService: SessionService,
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
        this.loginService.logout().subscribe(
            isSuccess => {
                this.router.navigate(['login']);
            }
        );
    }
}
