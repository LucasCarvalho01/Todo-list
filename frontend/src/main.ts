import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { provideHttpClient } from '@angular/common/http'; 
import { provideNativeDateAdapter } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { LOCALE_ID } from '@angular/core';

registerLocaleData(localePt);

bootstrapApplication(App, {
  ...appConfig,
  providers: [
    provideHttpClient(),
    provideNativeDateAdapter(),
    { provide: LOCALE_ID, useValue: 'pt-BR' }
  ]
})
  .catch((err) => console.error(err));
