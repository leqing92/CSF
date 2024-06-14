import { Component, OnInit, inject } from '@angular/core';
import { ArchiveService } from '../../archive.service';
import { Bundle, BundleInfo } from '../../Models/models';
import { Observable, firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-view0',
  templateUrl: './view0.component.html',
  styleUrl: './view0.component.css'
})
export class View0Component implements OnInit {  

  private readonly archiveSvc = inject(ArchiveService);
  bundles$ !: Observable<BundleInfo[]>;
  bundlesO !: BundleInfo[];
  bundles : BundleInfo[] =[];

  ngOnInit(): void {
    //by Observable | async
    this.bundles$ = this.archiveSvc.getBundles();

    this.archiveSvc.getBundles()
      .subscribe(resp =>
        {          
          this.bundlesO = resp;
        }
      )

    firstValueFrom(this.archiveSvc.getBundles())
      .then(resp=>{
        this.bundles = resp;
      }
      )
    
  }
}
