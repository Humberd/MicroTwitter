import { Route } from "@angular/router";
import { ProfileComponent } from "./profile/profile.component";
import { TweetsComponent } from "./profile/tweets/tweets.component";
import { FollowingComponent } from "./profile/following/following.component";
import { FollowersComponent } from "./profile/followers/followers.component";
import { LikesComponent } from "./profile/likes/likes.component";
import { RootWrapperComponent } from "./root-wrapper/root-wrapper.component";

export const twitterRoutes: Route[] = [
  {
    path: "",
    pathMatch: "full",
    component: RootWrapperComponent
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
