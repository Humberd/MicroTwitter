import { Directive, ElementRef, Input, OnInit } from '@angular/core';

@Directive({
  selector: '[appAutofocus]'
})
export class AutofocusDirective implements OnInit {
  private _autofocus;

  constructor(private el: ElementRef) {
  }

  @Input()
  set appAutofocus(condition: boolean) {
    this._autofocus = condition !== false;
  }

  ngOnInit() {
    if (this._autofocus || typeof this._autofocus === "undefined") {
      // For SSR (server side rendering) this is not safe. Use: https://github.com/angular/angular/issues/15008#issuecomment-285141070)
      this.el.nativeElement.focus();
    }
  }
}
