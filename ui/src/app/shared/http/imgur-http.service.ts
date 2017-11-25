import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { CONSTANTS } from "../../config/Constants";
import { ImgurImageUploadResponseDTO } from "../../dto/ImgurImageUploadResponseDTO";

@Injectable()
export class ImgurHttpService {

  constructor(private http: HttpClient) {
  }

  public uploadImage(body: FormData): Observable<ImgurImageUploadResponseDTO> {
    return this.http.post<ImgurImageUploadResponseDTO>(CONSTANTS.IMGUR_API_URL, body, {
      headers: {
        Authorization: "Client-ID " + CONSTANTS.IMGUR_CLIENT_ID
      }
    });
  }
}
