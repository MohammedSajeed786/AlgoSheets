import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpResponse,
} from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Token } from '@angular/compiler';
export interface TokenResponse {
  token: string;
}
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  authUrl = environment.backendUrl + 'auth/v1/';
  codeReceiverUri: string = environment.backendUrl + 'auth/v1/oauth/callback';

  constructor(private http: HttpClient) {}

  //check whether email exists in backend
  validateEmail(email: String): Observable<HttpResponse<any>> {
    let body = {
      email: email,
    };
    return this.http.post(this.authUrl + 'validate-email', body, {
      observe: 'response',
    });
  }

  refreshToken(email: String) {
    let body = {
      email: email,
    };
    return this.http
      .post<TokenResponse>(this.authUrl + 'refresh-token', body)
      .pipe(
        tap((res: TokenResponse) => localStorage.setItem('token', res.token))
      );
  }

  exchangeAuthCode(authCode: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Requested-With': 'XmlHttpRequest',
    });

    const body = new URLSearchParams();
    body.set('code', authCode);

    return this.http
      .post<TokenResponse>(this.codeReceiverUri, body.toString(), { headers })
      .pipe(
        tap((response: TokenResponse) => {
          if (response && response.token) {
            localStorage.setItem('token', response.token);
          }
        })
      );
  }
}
