import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from "../utils/translate.service";
import {Login} from "../models/login.model";
import {SessionService} from "./session.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    login: Login = new Login();
    returnUrl: string;
    lang: string;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private loginService: SessionService,
                private translate: TranslateService) {
    }

    ngOnInit() {
        if (!!localStorage.getItem('lang')) {
            this.lang = localStorage.getItem('lang');
        }else {
            this.setLang('fr');
        }
        // get return url from route parameters or default to '/home'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    }


    setLang(lang: string) {
        this.translate.use(lang);
        this.lang = localStorage.getItem('lang');
    }

    loginUser(e) {
        e.preventDefault();

        this.login.user = e.target.elements[0].value;
        this.login.credential = e.target.elements[1].value;

        this.loginService.login(this.login).subscribe(
            isSuccess => {
                this.router.navigateByUrl(this.returnUrl);
            },
            isError => {
                document.getElementById("alerta").style.display = "block" ;
            }
        );
    }

}
