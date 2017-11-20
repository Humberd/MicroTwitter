import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';
import { authRoutes } from "./views/auth/auth-routing.module";
import { LayoutComponent } from "./shared/layout/layout/layout.component";

const routes: Route[] = [
  {
    path: "",
    pathMatch: "full",
    redirectTo: "login"
  },
  {
    path: "",
    component: LayoutComponent,
    children: [
      ...authRoutes
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
