import { Comment } from './../shared/comment';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})

export class WebscraperService { 
  readonly url = "http://localhost:8080/";
  list:Comment[]=[new Comment()];

  constructor(private http: HttpClient) {}

  Extract(){
    return this.http.post(this.url+"export","https://www.rebel.pl/product.php/1,1203/99136/Tajniacy.html");
  }

  Transform(){
    return this.http.post(this.url+"transform",null);
  }

  Load(){
    return this.http.post(this.url+"load",null);
  }

  ETL(){
    return this.http.post(this.url,"https://www.rebel.pl/product.php/1,1203/99136/Tajniacy.html");
  }

  DownloadCSV(){
    return this.http.get(this.url+"csv",{ responseType: 'blob' });
  }

  DownloadCSVId(id){
    return this.http.get(this.url+id+"/",{ responseType: 'blob' });
  }

  Clear(){
    return this.http.post(this.url+"clear",null);
  }

  RefreshList(){
    return this.http.get(this.url);
  }
  GetData(){
    this.http.get(this.url)
    .toPromise()
    .then(res => this.list = res as Comment[]);
  }
}
