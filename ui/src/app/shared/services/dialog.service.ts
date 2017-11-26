import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from "@angular/material";
import { NewTweetDialogComponent } from "../components/new-tweet-dialog/new-tweet-dialog.component";

@Injectable()
export class DialogService {
  private config: MatDialogConfig = {
    width: '590px',
    panelClass: 'app-custom-dialog',
    position: {
      top: '10%'
    }
  };

  constructor(private matDialog: MatDialog) {
  }

  public showNewTweetDialog(): MatDialogRef<NewTweetDialogComponent> {
    return this.matDialog.open(NewTweetDialogComponent, {...this.config});
  }


}
