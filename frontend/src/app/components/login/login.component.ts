import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ToastService } from '../../shared/services/toast.service';
import { RegexConstants } from '../../constants/RegexConstants';
import { Router } from '@angular/router';
declare const google: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  client!: any;
  email: FormControl;

  @ViewChild('oauth')
  oAuthButtonRef!: ElementRef;

  constructor(
    private authService: AuthService,
    private toastService: ToastService,
    private router: Router
  ) {
    // Initialize email FormControl
    this.email = new FormControl('', [
      Validators.required,
      Validators.pattern(RegexConstants.EMAIL_PATTERN),
    ]);
  }

  ngOnInit() {
    this.initOAuth();
  }

  verifyAndLogin() {
    // Verify if email exists in backend
    this.authService.validateEmail(this.email.value).subscribe({
      next: (res: HttpResponse<any>) => {
        // On success response -> found
        this.refreshToken();
      },
      error: (err: HttpErrorResponse) => {
        let status = err.status;
        let message = '';
        if (status === 404) {
          // Status is 404 -> show consent screen
          this.oAuthButtonRef.nativeElement.click();
          message = 'Email not found. Redirecting to consent screen...';
        } else if (status >= 400 && status < 500) {
          // Status is 4xx -> invalid email
          message = 'Invalid email address.';
        } else {
          // Status is 5xx -> server error
          message = 'Something went wrong. Please try again later.';
        }
        this.toastService.showToast(message, 'danger');
      },
    });
  }

  refreshToken() {
    this.authService.refreshToken(this.email.value).subscribe({
      next: (res) => this.router.navigate(['home']),
      error: (err) => this.toastService.showToast(err.message, 'danger'),
    });
  }

  initOAuth() {
    this.client = google.accounts.oauth2.initCodeClient({
      client_id: environment.googleClientId,
      scope:
        'https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile',
      ux_mode: 'popup',
      redirect_uri: 'http://localhost:4300/home',
      callback: (response: any) => {
        this.exchangeAuthCode(response.code);
      },
    });
  }

  exchangeAuthCode(authCode: string) {
    this.authService.exchangeAuthCode(authCode).subscribe({
      next: (res) => {
        this.router.navigate(['home']);
      },
      error: (err) => {
        this.toastService.showToast(err.message, 'danger');
      },
    });
  }
}
