import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { ProfileUpdateDTO } from "../../../dto/ProfileUpdateDTO";
import { UserHttpService } from "../../http/user-http.service";
import { SnackBarService } from "../../services/snack-bar.service";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { Observable } from "rxjs/Observable";

@Component({
  selector: 'app-update-profile-form',
  templateUrl: './update-profile-form.component.html',
  styleUrls: ['./update-profile-form.component.scss']
})
export class UpdateProfileFormComponent implements OnInit {
  @Input() user: UserResponseDTO;
  profileForm: FormGroup;

  constructor(private userHttpService: UserHttpService,
              private snackBarService: SnackBarService) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  public updateProfile(): Observable<UserResponseDTO> {
    this.profileForm.markAsDirty();
    if (this.profileForm.invalid) {
      return Observable.throw("Form is invalid");
    } else {
      const requestData = this.prepareData();
      return this.userHttpService.updateProfile(requestData);
    }
  }

  private initForm(): void {
    this.profileForm = new FormGroup({
      fullName: new FormControl(this.user.profile.fullName || ""),
      description: new FormControl(this.user.profile.description || ""),
      location: new FormControl(this.user.profile.location || ""),
      profileLinkColor: new FormControl(this.user.profile.profileLinkColor || ""),
      url: new FormControl(this.user.profile.url || ""),
      avatarUrl: new FormControl(this.user.profile.avatarUrl || ""),
      backgroundUrl: new FormControl(this.user.profile.backgroundUrl || ""),
    });
  }

  private prepareData(): ProfileUpdateDTO {
    return {
      ...this.profileForm.value
    };
  }
}
