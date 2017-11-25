import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { LoginFormComponent } from './login/login-form/login-form.component';
import { MatButtonModule, MatFormFieldModule, MatInputModule, MatSnackBarModule } from "@angular/material";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatSnackBarModule,
  ],
  declarations: [
    LoginComponent,
    SignupComponent,
    LoginFormComponent,
  ],
  exports: [
    LoginFormComponent
  ],
  providers: []
})
export class AuthViewsModule {
}
