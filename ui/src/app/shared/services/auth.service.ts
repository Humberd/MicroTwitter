import { Injectable } from '@angular/core';
import { AppUser } from "../../models/AppUser";
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { Observable } from "rxjs/Observable";
import { AuthHttpService } from "../http/auth-http.service";
import { SignupDTO } from "../../dto/SignupDTO";
import { LoginDTO } from "../../dto/LoginDTO";
import { UserHttpService } from "../http/user-http.service";
import { CONSTANTS } from "../../config/Constants";
import { isObject, isString } from "util";
import { UserResponseDTO } from "../../dto/UserResponseDTO";

@Injectable()
export class AuthService {
  /**
   * When user data has not yet been acquired from the server, but we already have a jwt from storage.
   */
  initialJWT: string;
  private appUser: AppUser;
  private storage: Storage;

  /**
   * This observable should be triggered only once, when the initial user retrieve try is finished.
   */
  private initialAuthCheckFinished = new BehaviorSubject<boolean>(false);
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
   * The promise resolves, when the initial check for user is finished
   * @returns {Promise<any>}
   */
  public isInitialAuthCheckFinished(): Observable<boolean> {
    return this.initialAuthCheckFinished;
  }

  private finishInitialAuthCheck(): void {
    this.initialAuthCheckFinished.next(true);
  }

  /**
   * Checks if given userOrUsername is the same as logged user
   * @param {UserResponseDTO} userOrUsername
   * @returns {boolean}
   */
  public isMe(userOrUsername: UserResponseDTO | string): boolean {
    if (isString(userOrUsername)) {
      return this.isUserLoggedIn() && this.appUser.data.username.toLowerCase() === userOrUsername;
    } else if (isObject(userOrUsername)) {
      return this.isUserLoggedIn() && this.appUser.data.id === (userOrUsername as UserResponseDTO).id;
    }
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

        console.info("Acquired jwt token from login request");
        return jwtHeader.replace(CONSTANTS.AUTH_TOKEN_PREFIX, "");
      })
      .catch(error => {
        console.error("Cannot acquire jwt token from login request", error);
        return Observable.throw(error);
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
        console.info("Retrieved user data from getMe request");
        this.saveUser(appUser);
      })
      .catch(error => {
        console.error("Cannot retrieve user data from getMe request", error);
        this.logout();
        return Observable.throw(error);
      });
  }

  /**
   * Method that can be called from within the code to ask to send a request to update user data
   */
  public requestUserDataUpdate(): void {
    this.updateUserData(this.appUser.jwtToken)
      .subscribe();
  }

  /**
   * Removes an appUser from a service and from a permanent storage
   */
  public logout() {
    this.appUser = null;
    this.appUserObs.next(null);
    this.storage.removeItem(CONSTANTS.STORAGE_JWT_KEY);
    console.info("Logged out a user");
  }

  /**
   * Sends a signup request and after that logs user in
   * @param {SignupDTO} signupData
   * @returns {Observable<AppUser>}
   */
  public signup(signupData: SignupDTO): Observable<AppUser> {
    return this.authHttpService.signup(signupData)
      .do(() => console.info("Signed up a user"))
      .flatMap(() => this.login({
        username: signupData.username,
        password: signupData.password
      }));
  }

  public readFromStorage() {
    const jwt = this.storage.getItem(CONSTANTS.STORAGE_JWT_KEY);
    if (isString(jwt) && jwt.length > 0) {
      console.info("Acquired jwt token from storage");
      this.initialJWT = jwt;
      this.updateUserData(jwt)
        .finally(() => {
          this.finishInitialAuthCheck();
          this.initialJWT = null;
        })
        .subscribe();
    } else {
      this.logout();
      this.finishInitialAuthCheck() ;
    }
  }

  /**
   * Saves an appUser in a service and in a permanent storage
   * @param {AppUser} appUser
   */
  private saveUser(appUser: AppUser): void {
    this.appUser = appUser;
    this.appUserObs.next(appUser);
    this.storage.setItem(CONSTANTS.STORAGE_JWT_KEY, appUser.jwtToken);
    console.info("Saved a user");
  }
}
