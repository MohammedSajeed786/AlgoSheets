import { Component } from '@angular/core';
import { JwtService } from '../../services/jwt/jwt.service';
import { UserDetails } from '../../interfaces/UserDetails';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  userDetails!: UserDetails;

  constructor(private jwtService: JwtService) {
    let token = jwtService.decodeToken(jwtService.getToken() as string);
    this.userDetails = {
      name: token.name,
      email: token.email,
      profilePicture: token.profilePicture,
    };
  }
}
