import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig, MatSnackBarRef, SimpleSnackBar } from "@angular/material";

@Injectable()
export class SnackBarService {
  private readonly config: MatSnackBarConfig = {
    verticalPosition: 'top',
    duration: 3000,
  };

  constructor(private matSnackBar: MatSnackBar) {
  }

  public showInfoSnackBar(message: string): MatSnackBarRef<SimpleSnackBar> {
    return this.matSnackBar.open(message, null, {
      ...this.config,
      panelClass: "snack-bar-info",
    });
  }

  public showLongInfoSnackBar(message: string): MatSnackBarRef<SimpleSnackBar> {
    return this.matSnackBar.open(message, "Close", {
      ...this.config,
      panelClass: "snack-bar-info",
      duration: 10000,
    });
  }

  public showWarnSnackBar(message: string): MatSnackBarRef<SimpleSnackBar> {
    return this.matSnackBar.open(message, null, {
      ...this.config,
      panelClass: "snack-bar-warn",
    });
  }

  public showErrorSnackBar(message: string): MatSnackBarRef<SimpleSnackBar> {
    return this.matSnackBar.open(message, null, {
      ...this.config,
      panelClass: "snack-bar-error",
    });
  }
}
