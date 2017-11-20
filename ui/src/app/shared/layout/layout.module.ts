import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout/layout.component';
import { NavbarComponent } from './navbar/navbar.component';
import { RouterModule } from "@angular/router";
import { MatButtonModule, MatIconModule, MatTabsModule } from "@angular/material";

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    MatTabsModule,
    MatIconModule,
    MatButtonModule,
  ],
  declarations: [
    LayoutComponent,
    NavbarComponent
  ],
  exports: [
    LayoutComponent
  ]
})
export class LayoutModule { }
