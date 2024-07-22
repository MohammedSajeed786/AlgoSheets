import { Component } from '@angular/core';
import { UserDetails } from '../../../interfaces/UserDetails';
import { JwtService } from '../../../services/jwt/jwt.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
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
