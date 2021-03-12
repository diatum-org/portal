import { Injectable, Type, Component } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent } from '@angular/common/http';
import { HttpUrlEncodingCodec } from '@angular/common/http';

import { AccountEntry } from './accountEntry';
import { Emigo } from './emigo';
import { Pass } from './pass';
import { Result } from './result';
import { EmigoMessage } from './emigoMessage';
import { ServiceAccess } from './serviceAccess';

@Injectable()
export class AccountService {

  private headers: HttpHeaders;

  constructor(private httpClient: HttpClient) {
    this.headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
  }

  public checkToken(token: string): Promise<boolean> {
    return this.httpClient.get<boolean>("device/token?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public createIdentity(token: string, password: string): Promise<AccountEntry> {
    return this.httpClient.post<AccountEntry>("account/identity?token=" + token + "&password=" + password,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setIdentity(token: string, password: string): Promise<AccountEntry> {
    return this.httpClient.put<AccountEntry>("account/identity?token=" + token + "&password=" + password,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getId(url: string, handle: string): Promise<string> {
    return this.httpClient.get<string>(url + "/emigo/id?handle=" + handle + "&wrap=true",
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getIdentity(id: string, password: string): Promise<AccountEntry> {
    return this.httpClient.get<AccountEntry>("account/identity?emigoId=" + id + "&password=" + password,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getEmigo(entry: AccountEntry): Promise<Emigo> {
    return this.httpClient.get<Emigo>(entry.node + "/identity?token=" + entry.token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public generatePass(entry: AccountEntry, expire: number, service: ServiceAccess): Promise<Pass> {
    return this.httpClient.post<Pass>(entry.node + "/access/accounts/tokens?token=" + entry.token + "&expire=" + expire,
        service, { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getDefaultRegistry(): Promise<string> {
    return this.httpClient.get<string>("account/registry", 
        { headers: this.headers, observe: 'body' }).toPromise();
  }
  
  public checkHandle(url: string, emigoId: string, handle: string): Promise<Result> {
    return this.httpClient.get<Result>(url + "/emigo/status?emigoId=" + emigoId + "&handle=" + handle,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setHandle(entry: AccountEntry, handle: string): Promise<EmigoMessage> {
    return this.httpClient.put<EmigoMessage>(entry.node + "/identity/handle?token=" + entry.token,
        handle, { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setRegistry(entry: AccountEntry, registry: string): Promise<EmigoMessage> {
    return this.httpClient.put<EmigoMessage>(entry.node + "/identity/registry?token=" + entry.token,
        registry, { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setMessage(url: string, msg: EmigoMessage): Promise<void> {
    return this.httpClient.post<void>(url + "/emigo/messages",
        msg, { headers: this.headers, observe: 'body' }).toPromise();
  }

}

