import { Injectable } from '@angular/core';
import { AppUser } from "../models/AppUser";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { Observable } from "rxjs/Observable";
import { AuthHttpService } from "./http/auth-http.service";
import { SignupDTO } from "../dto/SignupDTO";
import { LoginDTO } from "../dto/LoginDTO";
import { UserHttpService } from "./http/user-http.service";
import { CONSTANTS } from "../config/Constants";
import { isString } from "util";

@Injectable()
export class AuthService {
  private appUser: AppUser;
  private storage: Storage;

  /**
   * It has 3 states:
   *  * undefined - when the app has already started and getting auth user requests hasn't completed yet
   *  * null - user not logged in
   *  * object - user is logged in
   * @type {BehaviorSubject<any>}
   */
  private appUserObs: BehaviorSubject<AppUser> = new BehaviorSubject(undefined);

  constructor(private authHttpService: AuthHttpService,
              private userHttpService: UserHttpService) {
    this.storage = localStorage;
  }

  /**
   * Gets an appUser
   * @returns {AppUser}
   */
  public getUser(): AppUser {
    return this.appUser;
  }

  /**
   * Gets an observable stream of appUsers
   * @returns {Observable<AppUser>}
   */
  public getUserObs(): Observable<AppUser> {
    return this.appUserObs.asObservable();
  }

  /**
   * Checks if a user is logged in
   * @returns {boolean}
   */
  public isUserLoggedIn(): boolean {
    return !!this.appUser;
  }

  /**
   * Gets an observable stream of checks if a user is logged in
   * @returns {Observable<boolean>}
   */
  public isUserLoggedInObs(): Observable<boolean> {
    return this.appUserObs.asObservable()
      .map(appUser => !!appUser);
  }

  /**
   * Logs user in.
   * Makes a login request and after that a get user data request
   * @param {LoginDTO} loginData
   * @returns {Observable<AppUser>}
   */
  public login(loginData: LoginDTO): Observable<AppUser> {
    return this.authHttpService.login(loginData)
      .map(response => {
        if (!response.ok) {
          throw Error(response.body);
        }

        const jwtHeader = response.headers.get(CONSTANTS.AUTH_HEADER_NAME);
        if (!isString(jwtHeader)) {
          throw Error(`Expected a '${CONSTANTS.AUTH_HEADER_NAME}' header in a login response`);
        }
        return jwtHeader.replace(CONSTANTS.AUTH_TOKEN_PREFIX, "");
      })
      .flatMap(jwt => this.updateUserData(jwt));
  }

  /**
   * Updates an appUser based on a provided jwt
   * @param {string} jwt
   * @returns {Observable<AppUser>}
   */
  public updateUserData(jwt: string): Observable<AppUser> {
    return this.userHttpService.getMe(jwt)
      .map(user => ({
        data: user,
        jwtToken: jwt
      } as AppUser))
      .do(appUser => {
        this.saveUser(appUser);
      });
  }

  /**
   * Saves an appUser in a service and in a permanent storage
   * @param {AppUser} appUser
   */
  private saveUser(appUser: AppUser): void {
    this.appUser = appUser;
    this.appUserObs.next(appUser);
    this.storage.setItem(CONSTANTS.STORAGE_JWT_KEY, appUser.jwtToken);
  }

  /**
   * Removes an appUser from a service and from a permanent storage
   */
  public logout() {
    this.appUser = null;
    this.appUserObs.next(null);
    this.storage.removeItem(CONSTANTS.STORAGE_JWT_KEY);
  }

  /**
   * Sends a signup request and after that logs user in
   * @param {SignupDTO} signupData
   * @returns {Observable<AppUser>}
   */
  public signup(signupData: SignupDTO): Observable<AppUser> {
    return this.authHttpService.signup(signupData)
      .flatMap(() => this.login({
        username: signupData.username,
        password: signupData.password
      }));
  }
}
