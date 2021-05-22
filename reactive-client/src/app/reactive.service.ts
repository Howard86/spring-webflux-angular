import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Quote } from './quote';

@Injectable({
  providedIn: 'root',
})
export class ReactiveService {
  private url = 'http://localhost:8080/quotes-reactive';
  private urlPaged = 'http://localhost:8080/quotes-reactive-paged';

  getQuoteStream(page?: number, size?: number): Observable<Quote> {
    return new Observable<Quote>((observer) => {
      const requestUrl =
        typeof size !== 'undefined' && typeof page !== 'undefined'
          ? `${this.urlPaged}?page=${page}&size=${size}`
          : this.url;

      const eventSource = new EventSource(requestUrl);

      eventSource.onmessage = (event) => {
        console.debug(`Received event: ${event}`);
        const { id, book, content } = JSON.parse(event.data);

        observer.next(new Quote(id, book, content));
      };

      eventSource.onerror = (error) => {
        if (eventSource.readyState !== 0) {
          observer.error(`EventSource error: ${error}`);
          return;
        }

        console.log('The stream has been closed by the server');
        eventSource.close();
        observer.complete();
      };
    });
  }
}
