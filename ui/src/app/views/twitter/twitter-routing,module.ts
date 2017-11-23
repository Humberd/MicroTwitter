import { Route } from "@angular/router";
import { WallComponent } from "./wall/wall.component";
import { AuthorizedGuard } from "../../shared/guards/authorized.guard";

export const twitterRoutes: Route[] = [
  {
    canActivate: [AuthorizedGuard],
    path: "wall",
    component: WallComponent
  }
];
