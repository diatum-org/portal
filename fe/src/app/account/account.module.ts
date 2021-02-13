import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms'
import { CommonModule } from '@angular/common'
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { ChartsModule } from 'ng2-charts';
import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account.component';
import { ProfileComponent } from './profile/profile.component';
import { ExpireComponent } from './expire/expire.component';
import { UsernameComponent } from './username/username.component';
import { AccountService } from './account.service';

@NgModule({
  declarations: [
    AccountComponent,
    ProfileComponent,
    ExpireComponent,
    UsernameComponent,
  ],
  imports: [
    FormsModule,
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatDialogModule,
    HttpClientModule,
    MatProgressSpinnerModule,
    AccountRoutingModule,
    ChartsModule
  ],
  providers: [
    AccountService,
  ],
  bootstrap: [],
  entryComponents: [
    ExpireComponent,
    UsernameComponent,
  ]
})
export class AccountModule { }
