import { Component, inject, OnInit } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {

  private readonly oidcSecurityService = inject(OidcSecurityService);
  isAuthenticated:boolean = false;
  username:string = '';

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(({ isAuthenticated }) => {
      this.isAuthenticated = isAuthenticated;
  
    });
    this.oidcSecurityService.userData$.subscribe(({ userData }) => {
      this.username = userData?.preferred_username || '';
    });
  }

  login() {
    this.oidcSecurityService.authorize();
  }

  logout() {
    this.oidcSecurityService.logoff()
    .subscribe((result) => {
      console.log('Logged out successfully', result);
    }, (error) => {
      console.error('Logout failed', error);
    });
  }
}
