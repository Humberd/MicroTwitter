import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { TweetUserResponseDTO } from "../../../dto/TweetResponseDTO";

@Component({
  selector: 'app-user-basic-big',
  templateUrl: './user-basic-big.component.html',
  styleUrls: ['./user-basic-big.component.scss']
})
export class UserBasicBigComponent {
  @Input() user: UserResponseDTO;

  constructor() {
  }
}
