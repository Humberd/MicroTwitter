import { Component, OnInit } from '@angular/core';
import { FormControl } from "@angular/forms";
import { UserResponseDTO } from "../../../dto/UserResponseDTO";
import { Observable } from "rxjs/Observable";
import { UserHttpService } from "../../http/user-http.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-search-user',
  templateUrl: './search-user.component.html',
  styleUrls: ['./search-user.component.scss']
})
export class SearchUserComponent implements OnInit {
  searchInputControl: FormControl;
  results: Observable<UserResponseDTO[]>;

  constructor(private userHttpService: UserHttpService,
              private router: Router) {
  }

  ngOnInit() {
    this.searchInputControl = new FormControl("");

    this.results = this.searchInputControl.valueChanges
      .filter(value => value !== "")
      .debounceTime(300)
      .do(value => console.info(`Getting users for query: ${value}`))
      .switchMap(inputValue => this.userHttpService.getUsers(inputValue))
      .do(page => console.info(`Received ${page.numberOfElements} users`))
      .map(page => page.content);
  }

  goToUserPage(event: any) {
    const user: UserResponseDTO = event.option.value;
    this.router.navigate(['/u', user.username]);
  }

}
