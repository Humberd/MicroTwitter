import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DynamicColorDirective } from './dynamic-color.directive';
import { DynamicBackgroundColorDirective } from './dynamic-background-color.directive';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective
  ],
  exports: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective
  ]
})
export class DirectivesModule { }
