import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RootComponent } from './root/root.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: RootComponent
  },
  {
    path: 'console',
    loadChildren: () => import('./console/console.module').then(mod => mod.ConsoleModule)
  },
  {
    path: 'device',
    loadChildren: () => import('./device/device.module').then(mod => mod.DeviceModule)
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.module').then(mod => mod.AccountModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

