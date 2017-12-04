import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WallComponent } from './wall/wall.component';
import { ProfileComponent } from './profile/profile.component';
import { MatButtonModule, MatIconModule, MatMenuModule, MatTabsModule } from "@angular/material";
import { TabsComponent } from './profile/tabs/tabs.component';
import { RouterModule } from "@angular/router";
import { TweetsComponent } from './profile/tweets/tweets.component';
import { SharedModule } from "../../shared/_shared.module";
import { FollowingComponent } from './profile/following/following.component';
import { FollowersComponent } from './profile/followers/followers.component';
import { LikesComponent } from './profile/likes/likes.component';
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { UserInfoComponent } from './profile/user-info/user-info.component';
import { WallTweetsComponent } from './wall/wall-tweets/wall-tweets.component';
import { WallUserCardComponent } from './wall/wall-user-card/wall-user-card.component';

@NgModule({
  imports: [
    CommonModule,
    MatButtonModule,
    MatTabsModule,
    MatMenuModule,
    MatIconModule,
    RouterModule,
    SharedModule,
    InfiniteScrollModule,
  ],
  declarations: [
    WallComponent,
    ProfileComponent,
    TabsComponent,
    TweetsComponent,
    FollowingComponent,
    FollowersComponent,
    LikesComponent,
    UserInfoComponent,
    WallTweetsComponent,
    WallUserCardComponent,
  ]
})
export class TwitterViewsModule {
}
