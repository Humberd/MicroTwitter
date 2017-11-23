import { Route } from '@angular/router';
import { LoginComponent } from "./login/login.component";
import { SignupComponent } from "./signup/signup.component";
import { UnauthorizedGuard } from "../../shared/guards/unauthorized.guard";

export const authRoutes: Route[] = [
  {
    canActivate: [UnauthorizedGuard],
    path: "login",
    component: LoginComponent
  },
  {
    canActivate: [UnauthorizedGuard],
    path: "signup",
    component: SignupComponent
  }
];
