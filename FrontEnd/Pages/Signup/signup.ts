import { Component } from '@angular/core';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  user = {
    name: '',
    age: null,
    sex: '',
    address: '',
    phone: null,
    branchName: '',
    deposit: null,
    email: '',
    password: ''
  };

  onSubmit() {
    // Handle the form submission logic here, e.g., send data to the server.
    console.log(this.user);
  }
}
