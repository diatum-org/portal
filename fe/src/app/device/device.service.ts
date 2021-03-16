import { Injectable, Type, Component } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent } from '@angular/common/http';
import { HttpUrlEncodingCodec } from '@angular/common/http';

import { Emigo } from './emigo';
import { DeviceEntry } from './deviceEntry';
import { SystemStat } from './systemStat';
import { AccountEntry } from './accountEntry';

@Injectable()
export class DeviceService {

  private headers: HttpHeaders;

  constructor(private httpClient: HttpClient) {
    this.headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
  }

  public getEntry(login: string, password: string): Promise<DeviceEntry> {
    let p: string = encodeURIComponent(password);
    return this.httpClient.get<DeviceEntry>("device?login=" + login + "&password=" + p,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getStats(url: string, token: string): Promise<SystemStat[]> {
    return this.httpClient.get<SystemStat[]>(url + "/admin/server/stats?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getAccounts(url: string, token: string): Promise<AccountEntry[]> {
    return this.httpClient.get<AccountEntry[]>(url + "/admin/accounts?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getIdentity(url: string, token: string, emigoId: string): Promise<Emigo> {
    return this.httpClient.get<Emigo>(url + "/admin/accounts/" + emigoId + "?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public addAccount(login: string, password: string, expire: number): Promise<string> {
    let p: string = encodeURIComponent(password);
    return this.httpClient.post<string>("device/accounts?login=" + login + "&password=" + p + "&expire=" + expire,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public removeAccount(url: string, token: string, id: string): Promise<void> {
    return this.httpClient.delete<void>(url + "/admin/accounts/" + id + "?token=" + token, 
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public resetAccount(login: string, password: string, expire: number, emigoId: string): Promise<string> {
    let p: string = encodeURIComponent(password);
    return this.httpClient.put<string>("device/accounts?login=" + login + "&password=" + p + "&expire=" + expire + "&emigoId=" + emigoId,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

}

