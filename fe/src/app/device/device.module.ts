import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms'
import { CommonModule } from '@angular/common'
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';

import { ChartsModule } from 'ng2-charts';
import { DeviceRoutingModule } from './device-routing.module';
import { DeviceComponent } from './device.component';
import { DeviceService } from './device.service';
import { ExpireComponent } from './expire/expire.component';
import { StatsComponent } from './stats/stats.component';
import { ConfirmComponent } from './confirm/confirm.component';
import { AccountsComponent } from './accounts/accounts.component';

@NgModule({
  declarations: [
    DeviceComponent,
    StatsComponent,
    ConfirmComponent,
    AccountsComponent,
    ExpireComponent,
  ],
  imports: [
    FormsModule,
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatDialogModule,
    HttpClientModule,
    DeviceRoutingModule,
    ChartsModule
  ],
  providers: [
    DeviceService,
  ],
  bootstrap: [],
  entryComponents: [
    StatsComponent,
    ConfirmComponent,
    AccountsComponent,
    ExpireComponent,
  ]
})
export class DeviceModule { }
