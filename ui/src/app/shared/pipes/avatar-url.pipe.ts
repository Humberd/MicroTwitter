import { Pipe, PipeTransform } from '@angular/core';
import { isString } from "util";
import { CONSTANTS } from "../../config/Constants";

@Pipe({
  name: 'avatarUrl'
})
export class AvatarUrlPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (!isString(value)) {
      return "";
    }

    if (value.trim().length === 0) {
      return CONSTANTS.DEFAULT_AVATAR_URL;
    }
    return value;
  }

}
