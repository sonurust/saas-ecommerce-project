import { APP_INITIALIZER, ApplicationConfig, PLATFORM_ID, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './interceptor/auth.interceptor';
import { OidcSecurityService, provideAuth } from 'angular-auth-oidc-client';
import { authConfig } from './config/auth.config';

function initAuth(oidcSecurityService: OidcSecurityService, platformId: Object) {
  return () => {
    if (isPlatformBrowser(platformId)) {
      return oidcSecurityService.checkAuth();
    }
    return Promise.resolve();
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes), provideClientHydration(withEventReplay()),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
    provideAuth(authConfig),
    {
      provide: APP_INITIALIZER,
      useFactory: initAuth,
      deps: [OidcSecurityService, PLATFORM_ID],
      multi: true,
    }
  ]
};
