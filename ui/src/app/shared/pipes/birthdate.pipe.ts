import { Pipe, PipeTransform } from '@angular/core';
import { BirthdateDTO } from "../../dto/ProfileUpdateDTO";

@Pipe({
  name: 'birthdate',
})
export class BirthdatePipe implements PipeTransform {

  transform(value: BirthdateDTO, args?: any): any {
    const date = new Date(`${value.year}/${value.month}/${value.day}`);
    if (isNaN(date.getTime())) {
      return "";
    }
    return date.getTime();
  }
}
