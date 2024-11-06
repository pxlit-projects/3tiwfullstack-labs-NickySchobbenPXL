import {Component, inject, OnInit} from '@angular/core';
import {FilterComponent} from "../filter/filter.component";
import {CustomerItemComponent} from "../customer-item/customer-item.component";
import {Customer} from "../../../shared/models/customer.model";
import {Filter} from "../../../shared/models/filter.model";
import {AddCustomerComponent} from "../add-customer/add-customer.component";
import {CustomerService} from "../../../shared/services/customer.service";

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    FilterComponent,
    CustomerItemComponent,
    AddCustomerComponent
  ],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})

export class CustomerListComponent implements OnInit {
  customers!: Customer[];
  filteredData!: Customer[];
  customerService: CustomerService = inject(CustomerService);


  ngOnInit(): void {
    this.customerService.getCustomers().subscribe({
      next: customers => {
        this.fetchData();
      }
    });
  }




  handleFilter(filter: Filter){
    this.customerService.filterCustomers(filter).subscribe({
      next: customers => this.filteredData = customers
    });
  }

  processAdd(customer: Customer){
    this.customerService.addCustomer(customer).subscribe({
      next: () => {
        this.fetchData();
      }
    });
  }

  fetchData(): void {
    this.customerService.getCustomers().subscribe({
      next: customers => {
        this.filteredData = customers;
      }
    });
  }

}
