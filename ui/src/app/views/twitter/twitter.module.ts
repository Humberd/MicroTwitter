import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WallComponent } from './wall/wall.component';
import { ProfileComponent } from './profile/profile.component';
import { MatButtonModule } from "@angular/material";

@NgModule({
  imports: [
    CommonModule,
    MatButtonModule,
  ],
  declarations: [
    WallComponent,
    ProfileComponent
  ]
})
export class TwitterViewsModule {
}
