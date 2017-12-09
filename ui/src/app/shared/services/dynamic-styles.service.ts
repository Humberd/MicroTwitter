import { Injectable } from '@angular/core';
import { UserResponseDTO } from "../../dto/UserResponseDTO";
import { CONSTANTS } from "../../config/Constants";
import { isNull } from "util";

@Injectable()
export class DynamicStylesService {
  constructor() {
  }

  public setUser(user?: UserResponseDTO): void {
    this.removeOldStyleSheetFromDOM();
    this.addStyleSheetToDOM(user);
  }

  private removeOldStyleSheetFromDOM(): void {
    const oldStyleSheet = document.getElementById(CONSTANTS.DYNAMIC_STYLESHEET_ID);
    if (oldStyleSheet) {
      oldStyleSheet.remove();
    }
  }

  private addStyleSheetToDOM(user?: UserResponseDTO): void {
    let userColor: string;
    if (isNull(user)) {
      userColor = CONSTANTS.DEFAULT_TWITTER_COLOR;
    } else {
      userColor = user.profile.profileLinkColor;
    }

    const css = this.generateStylesheet(userColor);
    const head = document.head || document.getElementsByTagName('head')[0];
    const style = document.createElement('style');
    style.id = CONSTANTS.DYNAMIC_STYLESHEET_ID;
    style.type = "text/css";

    if ((style as any).styleSheet) {
      (style as any).styleSheet.cssText = css;
    } else {
      style.appendChild(document.createTextNode(css));
    }
    head.appendChild(style);
  }

  private generateStylesheet(userColor: string): string {
    // language=CSS
    return `
      .dynamic-button-primary:not([disabled]) {
        background-color: ${userColor} !important;
      }
      .dynamic-button:not([disabled]) {
        color: ${userColor} !important;
        border-color: ${userColor} !important;
      }
      .dynamic-link {
        color: ${userColor} !important;
      }
      .dynamic-color {
        color: ${userColor} !important;
      }
      .dynamic-color-hover:hover {
        color: ${userColor} !important;
      }
      
      /* styles to a profile tabs component */
      .profile-tabs-component .mat-tab-link.mat-tab-label-active > .tab > .count {
        color: ${userColor} !important;
      }
      .profile-tabs-component .mat-tab-link > .tab:hover > .count {
        color: ${userColor} !important;
      }
      .profile-tabs-component .mat-tab-links > .mat-ink-bar {
        background-color: ${userColor} !important;
      }
      
      .wall-user-card-component .item:hover > .label {
        color: ${userColor} !important;
      }
    `;
  }
}
