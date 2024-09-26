import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Customer } from './shared/models/customer.model';
import {CustomerListComponent} from "./core/customers/customer-list/customer-list.component";
import {NavbarComponent} from "./core/navbar/navbar.component";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, CustomerListComponent, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'customer-app';
  constructor() {
    const c1 = new Customer('Dries Swinnen', 'dries@d-ries.be','Pelt','mystreet','Belgium',21);
    const c2 = new Customer('John Doe', 'john@doe.com','New York','5th Avenue','USA',6, 'assets/john.png');
    c2.isLoyal = true;

    console.log(c1);
    console.log(c2);
  }

}
