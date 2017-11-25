import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout/layout.component';
import { NavbarComponent } from './navbar/navbar.component';
import { RouterModule } from "@angular/router";
import { MatButtonModule, MatIconModule, MatMenuModule, MatTabsModule } from "@angular/material";
import { SharedModule } from "../shared/_shared.module";

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    MatTabsModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    SharedModule,
  ],
  declarations: [
    LayoutComponent,
    NavbarComponent,
  ],
  exports: [
    LayoutComponent
  ]
})
export class LayoutModule { }
