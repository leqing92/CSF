import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArchiveService } from '../../archive.service';
import { Bundle } from '../../Models/models';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-view2',
  templateUrl: './view2.component.html',
  styleUrl: './view2.component.css'
})
export class View2Component implements OnInit{
  private readonly activatedroute = inject(ActivatedRoute);
  private readonly archiveSvc = inject(ArchiveService);
  private readonly router = inject(Router);

  id !: string;
  bundle !: Bundle;

  ngOnInit(): void {
    this.id  = this.activatedroute.snapshot.params['id'];

    this.archiveSvc.getBundleById(this.id)
      .pipe(
        catchError(error => {
          alert('Error occurred: ' + JSON.stringify(error.error));
          this.router.navigate(['/']);
          return of(null);
        })
      )
      .subscribe(resp => {
        this.bundle = resp;
      });
  }

}
