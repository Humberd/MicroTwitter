import { Component } from '@angular/core';
import { MatDialogRef } from "@angular/material";

@Component({
  selector: 'app-new-tweet-dialog',
  templateUrl: './new-tweet-dialog.component.html',
  styleUrls: ['./new-tweet-dialog.component.scss']
})
export class NewTweetDialogComponent {

  constructor(private dialogRef: MatDialogRef<NewTweetDialogComponent>) {
  }

  closeDialog() {
    this.dialogRef.close();
  }

}
