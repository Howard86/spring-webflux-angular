import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ReactiveService } from '../reactive.service';
import { BlockService } from '../block.service';
import { Quote } from '../quote';

@Component({
  selector: 'app-quote',
  templateUrl: './quote.component.html',
  styleUrls: ['./quote.component.css'],
})
export class QuoteComponent implements OnInit {
  quoteArray: Quote[] = [];
  selectedQuote!: Quote;
  mode!: string;
  pagination!: boolean;
  page!: number;
  size!: number;

  constructor(
    private readonly reactiveService: ReactiveService,
    private readonly blockService: BlockService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.mode = 'reactive';
    this.pagination = true;
    this.page = 0;
    this.size = 50;
  }

  resetData() {
    this.quoteArray = [];
  }

  requestQuoteStream(): void {
    this.resetData();
    const quote$ = this.pagination
      ? this.reactiveService.getQuoteStream(this.page, this.size)
      : this.reactiveService.getQuoteStream();

    quote$.subscribe((quote) => {
      this.quoteArray.push(quote);
      this.cdr.detectChanges();
    });
  }

  requestQuoteBlocking(): void {
    this.resetData();
    const quotes$ = this.pagination
      ? this.blockService.getQuotes(this.page, this.size)
      : this.blockService.getQuotes();

    quotes$.subscribe((quotes) => {
      this.quoteArray = quotes;
    });
  }

  onSelect(quote: Quote): void {
    this.selectedQuote = quote;
    this.cdr.detectChanges();
  }
}
