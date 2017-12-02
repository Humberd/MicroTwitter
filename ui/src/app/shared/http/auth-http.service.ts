import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { LoginDTO } from "../../dto/LoginDTO";
import { SignupDTO } from "../../dto/SignupDTO";
import { Observable } from "rxjs/Observable";
import { UpdatePasswordDTO } from "../../dto/UpdatePasswordDTO";

@Injectable()
export class AuthHttpService {

  constructor(private http: HttpClient) {
  }

  public login(body: LoginDTO): Observable<HttpResponse<any>> {
    return this.http.post("/api/auth/login", body, {observe: "response", responseType: "text"});
  }

  public updatePassword(body: UpdatePasswordDTO): Observable<void> {
    return this.http.post<void>("/api/auth/password", body);
  }

  public signup(body: SignupDTO): Observable<void> {
    return this.http.post<void>("/api/auth/signup", body);
  }
}
