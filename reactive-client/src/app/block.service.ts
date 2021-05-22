import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quote } from './quote';

@Injectable({
  providedIn: 'root',
})
export class BlockService {
  private url = 'http://localhost:8080/quotes-blocking';
  private urlPaged = 'http://localhost:8080/quotes-blocking-paged';

  constructor(private http: HttpClient) {}

  getQuotes(page?: number, size?: number): Observable<Quote[]> {
    const requestUrl =
      typeof size !== 'undefined' && typeof page !== 'undefined'
        ? `${this.urlPaged}?page=${page}&size=${size}`
        : this.url;

    return this.http.get<Quote[]>(requestUrl);
  }
}
