import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Bundle, BundleInfo } from './Models/models';

@Injectable({
  providedIn: 'root'
})
export class ArchiveService {

  private readonly http = inject(HttpClient);

  upload(data : any) : Observable<any>{
    return this.http.post("/api/upload", data);
  }
  
  getBundleById(id : string) : Observable<any> {
    return this.http.get(`/api/bundle/${id}`);
  }

  getBundles() : Observable<BundleInfo[]> {
    return this.http.get<BundleInfo[]>('/api/bundles');
  }
}
