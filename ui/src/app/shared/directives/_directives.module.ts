import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DynamicColorDirective } from './dynamic-color.directive';
import { DynamicBackgroundColorDirective } from './dynamic-background-color.directive';
import { AutofocusDirective } from "./autofocus.directive";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,
  ],
  exports: [
    DynamicColorDirective,
    DynamicBackgroundColorDirective,
    AutofocusDirective,
  ]
})
export class DirectivesModule { }
