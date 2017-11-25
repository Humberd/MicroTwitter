import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { LoginFormComponent } from './login/login-form/login-form.component';
import { MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSnackBarModule } from "@angular/material";
import { RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/_shared.module";
import { DirectivesModule } from "../../shared/directives/_directives.module";
import { SignupFormComponent } from './signup/signup-form/signup-form.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    HttpClientModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatSnackBarModule,
    MatCardModule,
    DirectivesModule,
  ],
  declarations: [
    LoginComponent,
    SignupComponent,
    LoginFormComponent,
    SignupFormComponent,
  ],
  exports: [
    LoginFormComponent
  ],
  providers: []
})
export class AuthViewsModule {
}
