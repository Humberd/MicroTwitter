import { Component, Input } from '@angular/core';
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { AuthService } from "../../services/auth.service";
import { UserHttpService } from "../../http/user-http.service";

@Component({
  selector: 'app-follow-button',
  templateUrl: './follow-button.component.html',
  styleUrls: ['./follow-button.component.scss']
})
export class FollowButtonComponent {
  @Input() user: UserResponseDTO;
  isUnfollowButtonHovered = false;

  constructor(public authService: AuthService,
              private userHttpService: UserHttpService) {
  }

  triggerFollowUserAction(): void {
    this.userHttpService.followUser(this.user.id)
      .subscribe(newUserData => {
        Object.assign(this.user, newUserData);
      });
  }

  triggerUnfollowUserAction(): void {
    this.userHttpService.unfollowUser(this.user.id)
      .subscribe(newUserData => {
        Object.assign(this.user, newUserData);
      });
  }
}
