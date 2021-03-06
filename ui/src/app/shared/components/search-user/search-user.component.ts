import { Component, OnInit } from '@angular/core';
import { FormControl } from "@angular/forms";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { Observable } from "rxjs/Observable";
import { UserHttpService } from "../../http/user-http.service";
import { Router } from "@angular/router";
import { CONSTANTS } from "../../../config/Constants";

@Component({
  selector: 'app-search-user',
  templateUrl: './search-user.component.html',
  styleUrls: ['./search-user.component.scss']
})
export class SearchUserComponent implements OnInit {
  searchInputControl: FormControl;
  results: Observable<UserResponseDTO[]>;
  CONSTANTS = CONSTANTS;

  constructor(private userHttpService: UserHttpService,
              private router: Router) {
  }

  ngOnInit() {
    this.searchInputControl = new FormControl("");

    this.results = this.searchInputControl.valueChanges
      .filter(value => value !== "")
      .debounceTime(300)
      .do(value => console.info(`Getting users for query: ${value}`))
      .switchMap(inputValue => this.userHttpService.getUsers(inputValue)
        .do(page => console.info(`Received ${page.numberOfElements} users`))
        .map(page => page.content)
        .catch(e => Observable.of([])));
  }

  goToUserPage(event: any) {
    const username: string = event.option.value;
    this.router.navigate(['/u', username]);
  }

}
