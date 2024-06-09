import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArchiveService } from '../../archive.service';
import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view1',
  templateUrl: './view1.component.html',
  styleUrl: './view1.component.css'
})
export class View1Component implements OnInit{
  
  private readonly formbuilder = inject(FormBuilder);
  private readonly archiveSvc = inject(ArchiveService);
  private readonly router = inject(Router);

  form !: FormGroup;
  //method from slide 8 day37 ; require to put #file in html to indicate 
  @ViewChild('zipFile') zipFile !: ElementRef; 

  ngOnInit(): void {
    this.form = this.formbuilder.group({
      name : this.formbuilder.control("", [Validators.required]),
      title : this.formbuilder.control("", [Validators.required]),
      comments : this.formbuilder.control(""),
      'zipFile' : this.formbuilder.control("", [Validators.required]),
    })
  }

  submit(){
    const formData = new FormData();
    formData.set('name', this.form.get('name')?.value);
    formData.set('title', this.form.get('title')?.value);
    formData.set('comments', this.form.get('comments')?.value);
    formData.set('zipFile',this.zipFile.nativeElement.files[0]);

    console.info(formData);
    firstValueFrom(this.archiveSvc.upload(formData))
      .then(response =>{
        console.info(JSON.stringify(response));
        const bundleId = response['bundleId'];
        this.router.navigate(['/bundle', bundleId])
      })
      .catch(error =>{        
        console.error(error);
        alert(JSON.stringify(error));
      })
  }

  // onFileChange(event: any) {
  //   if (event.target.files && event.target.files.length) {
  //     const file = event.target.files[0];

  //     this.form.patchValue({
  //       zipFile: file
  //     });
  //   }
  // }

}
