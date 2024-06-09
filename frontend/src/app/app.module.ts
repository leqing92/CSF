import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { View0Component } from './View/view0/view0.component';
import { View1Component } from './View/view1/view1.component';
import { View2Component } from './View/view2/view2.component';

import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

const appRoute : Routes = [
  
  {path: '', component : View0Component},
  {path: 'upload', component : View1Component},
  {path : 'bundle/:id', component: View2Component},
  // wildcard
  {path:'**', redirectTo: '/', pathMatch:'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    View1Component,
    View2Component,
    View0Component,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoute, {useHash: true})
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
