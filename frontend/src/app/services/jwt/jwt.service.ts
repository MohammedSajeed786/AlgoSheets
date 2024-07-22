import { Injectable } from '@angular/core';
import { jwtDecode, JwtPayload } from 'jwt-decode';
interface TokenPayload extends JwtPayload {
  name: string;
  email: string;
  profilePicture: string;
}
@Injectable({
  providedIn: 'root',
})
export class JwtService {
  constructor() {}

  decodeToken(token: string) {
    return jwtDecode<TokenPayload>(token);
  }

  isTokenValid(token: string) {
    if (token == null) return false;
    else {
      let tokenExpiry = this.decodeToken(token).exp;
      if (tokenExpiry == null || tokenExpiry < Math.floor(Date.now() / 1000))
        return false;
      return true;
    }
  }

  getToken(){
    return localStorage.getItem('token');
  }

  setToken(token:string){
    localStorage.setItem('token',token);
  }
}
