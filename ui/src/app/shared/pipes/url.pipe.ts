import { Pipe, PipeTransform } from '@angular/core';
import { isString } from "util";

@Pipe({
  name: 'url'
})
export class UrlPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (!isString(value)) {
      return value;
    }

    if (value.startsWith("https://") || value.startsWith("http://")) {
      return value;
    } else {
      return "//" + value;
    }
  }

}
