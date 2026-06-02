import { PassedInitialConfig } from "angular-auth-oidc-client";

export const authConfig: PassedInitialConfig = {
    config: {
        authority: "http://localhost:8181/realms/saas-ecommerce-java-project",
        redirectUrl: 'http://localhost:4200',
        postLogoutRedirectUri: 'http://localhost:4200',
        clientId: 'frontend-angular',
        scope: 'openid profile offline_access',
        responseType: 'code',
        silentRenew: true,
        useRefreshToken: true,
        renewTimeBeforeTokenExpiresInSeconds: 30,
    }
};