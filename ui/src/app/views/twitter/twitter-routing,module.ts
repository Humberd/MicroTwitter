import { Route } from "@angular/router";
import { WallComponent } from "./wall/wall.component";
import { AuthorizedGuard } from "../../shared/guards/authorized.guard";
import { ProfileComponent } from "./profile/profile.component";
import { TweetsComponent } from "./profile/tweets/tweets.component";
import { FollowingComponent } from "./profile/following/following.component";
import { FollowersComponent } from "./profile/followers/followers.component";
import { LikesComponent } from "./profile/likes/likes.component";

export const twitterRoutes: Route[] = [
  {
    canActivate: [AuthorizedGuard],
    path: "wall",
    component: WallComponent
  },
  {
    path: "u/:username",
    component: ProfileComponent,
    children: [
      {
        path: "",
        pathMatch: "full",
        component: TweetsComponent,
      },
      {
        path: "following",
        component: FollowingComponent,
      },
      {
        path: "followers",
        component: FollowersComponent,
      },
      {
        path: "likes",
        component: LikesComponent,
      },
    ]
  }
];
