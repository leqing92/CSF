import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArchiveService } from '../../archive.service';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-multifile-upload',
  templateUrl: './multifile-upload.component.html',
  styleUrl: './multifile-upload.component.css'
})
export class MultifileUploadComponent {
  private readonly formbuilder = inject(FormBuilder);
  private readonly archiveSvc = inject(ArchiveService);
  private readonly router = inject(Router);

  form !: FormGroup;
  selectedFiles: File[] = [];

  ngOnInit(): void {
    this.form = this.formbuilder.group({
      name : this.formbuilder.control("", [Validators.required]),
      title : this.formbuilder.control("", [Validators.required]),
      comments : this.formbuilder.control(""),
      images : this.formbuilder.control("", [Validators.required]),      
    })
  }

  submit(){
    const formData = new FormData();
    formData.set('name', this.form.get('name')?.value);
    formData.set('title', this.form.get('title')?.value);
    formData.set('comments', this.form.get('comments')?.value || "");    

    for (const file of this.selectedFiles) {
      formData.append('files', file);
    }

    firstValueFrom(this.archiveSvc.uploadMultiple(formData))
      .then(resp => {
        const bundleId = resp.bundleId;
        this.router.navigate(['/bundle', bundleId]);
      })
      .catch(err=> alert(JSON.stringify(err)));
  }

  onFileChange(event: any) {
    if (event.target.files && event.target.files.length) {
      this.selectedFiles = Array.from(event.target.files);

      // Update the form control to reflect the selected files
      this.form.patchValue({ images: this.selectedFiles });
    }
  }
}
