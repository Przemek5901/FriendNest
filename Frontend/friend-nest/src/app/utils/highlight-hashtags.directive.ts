import {
  Directive,
  ElementRef,
  AfterViewInit,
  EventEmitter,
  Output,
} from '@angular/core';

@Directive({
  selector: '[highlightHashtags]',
  standalone: true,
})
export class HighlightHashtagsDirective implements AfterViewInit {
  @Output() hashtagClicked = new EventEmitter<{
    hashtag: string;
    event: Event;
  }>();

  constructor(private el: ElementRef) {}

  ngAfterViewInit(): void {
    this.highlightHashtags();
    this.addClickListeners();
  }

  private highlightHashtags(): void {
    const element = this.el.nativeElement;
    const originalText = element.innerHTML;

    const regex = /\B#[a-zA-Z0-9_]+/g;
    const newText = originalText.replace(regex, (match: string) => {
      return `<span class="highlighted-hashtag">${match}</span>`;
    });

    element.innerHTML = newText;
  }

  private addClickListeners(): void {
    const element = this.el.nativeElement;
    const hashtags = element.querySelectorAll('.highlighted-hashtag');

    hashtags.forEach((hashtagEl: HTMLElement) => {
      hashtagEl.addEventListener('click', (e: Event) => {
        this.hashtagClicked.emit({
          hashtag: hashtagEl.textContent || '',
          event: e,
        });
      });
    });
  }
}
