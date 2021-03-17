import { Injectable, Type, Component } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent } from '@angular/common/http';
import { HttpUrlEncodingCodec } from '@angular/common/http';

import { AccountEntry } from './accountEntry';
import { Amigo } from './amigo';
import { Pass } from './pass';
import { Result } from './result';
import { AmigoMessage } from './amigoMessage';
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
    return this.httpClient.get<string>(url + "/amigo/id?handle=" + handle + "&wrap=true",
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getIdentity(id: string, password: string): Promise<AccountEntry> {
    return this.httpClient.get<AccountEntry>("account/identity?amigoId=" + id + "&password=" + password,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getAmigo(entry: AccountEntry): Promise<Amigo> {
    return this.httpClient.get<Amigo>(entry.node + "/identity?token=" + entry.token,
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
  
  public checkHandle(url: string, amigoId: string, handle: string): Promise<Result> {
    return this.httpClient.get<Result>(url + "/amigo/status?amigoId=" + amigoId + "&handle=" + handle,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setHandle(entry: AccountEntry, handle: string): Promise<AmigoMessage> {
    return this.httpClient.put<AmigoMessage>(entry.node + "/identity/handle?token=" + entry.token,
        handle, { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setRegistry(entry: AccountEntry, registry: string): Promise<AmigoMessage> {
    return this.httpClient.put<AmigoMessage>(entry.node + "/identity/registry?token=" + entry.token,
        registry, { headers: this.headers, observe: 'body' }).toPromise();
  }

  public setMessage(url: string, msg: AmigoMessage): Promise<void> {
    return this.httpClient.post<void>(url + "/amigo/messages",
        msg, { headers: this.headers, observe: 'body' }).toPromise();
  }

}

