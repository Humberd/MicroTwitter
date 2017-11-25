import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';
import { authRoutes } from "./views/auth/auth-routing.module";
import { LayoutComponent } from "./layout/layout/layout.component";
import { twitterRoutes } from "./views/twitter/twitter-routing,module";

const routes: Route[] = [
  {
    path: "",
    pathMatch: "full",
    redirectTo: "wall"
  },
  {
    path: "",
    component: LayoutComponent,
    children: [
      ...authRoutes,
      ...twitterRoutes
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
