import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit() {
  }

  isPresent = false;

  registerUser(e) {



  /*	e.preventDefault();
  	console.log(e);
  	var email = e.target.elements[0].value;
    var password = e.target.elements[1].value;

    let usersFromDB = environment.users;
    for(let i=0; i<environment.users.length ; i++){

      if(email == usersFromDB[i].email){
        this.isPresent=true;
        alert("Userul este deja inregistrat");
        break;
      }
    }

    if(this.isPresent==false){
        usersFromDB.push({email: email, password:password});
        alert("Userul a fost inregistrat cu succes!");
    }

    environment.users=usersFromDB;
    
    this.router.navigate(['login']);*/

  }

}
