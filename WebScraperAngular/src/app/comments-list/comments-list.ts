import { Comment } from './../shared/comment';
import {Component, ViewChild, OnInit, AfterViewInit} from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { WebscraperService } from '../services/webscraper.service';
import { NotificationsService } from 'angular2-notifications';
import { timeout } from 'q';
import { empty } from 'rxjs';

@Component({
  selector: 'comments-list',
  styleUrls: ['comments-list.css'],
  templateUrl: 'comments-list.html',
})
export class CommentsList implements OnInit {

  constructor(private service: WebscraperService,private _service: NotificationsService){}
  dataSource = new MatTableDataSource<Comment>();
  
  displayedColumns: string[] = ['id', 'commentRating', 'date', 'nick', 'productRating', 'text', 'url'];

  Id=0;


  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  ngOnInit() {
    this.service.GetData();
    this.dataSource.data = this.service.list;
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.Refresh();
    
  }
  
  //********** */
  loadingE:boolean=false;
  loadingT:boolean=false;
  loadingL:boolean=false;
  loadingETL:boolean=false;    
  disabled:boolean=false;

  Extract(){
    this.loadingE=!this.loadingE;
    this.disabled=!this.disabled;
    this.service.Extract().subscribe(res=>{
      this.disabled=!this.disabled;
      this.loadingE=!this.loadingE;
      this.onSuccess(res["affectedRow"])
    },
    error=>{
      this.onInvalid("Niewłaściwa operacja"); 
      this.disabled=!this.disabled;
      this.loadingE=!this.loadingE;});
    
  }
  Transform(){
    this.loadingT=!this.loadingT;
    this.disabled=!this.disabled;
    this.service.Transform().subscribe(res=>{
      this.disabled=!this.disabled;
      this.loadingT=!this.loadingT;
      this.onSuccess(res["affectedRow"])
    },
    error=>{
      this.onInvalid("Niewłaściwa operacja"); 
      this.disabled=!this.disabled;
      this.loadingT=!this.loadingT;});
  }
  Load(){
    this.loadingL=!this.loadingL;
    this.disabled=!this.disabled;
    this.service.Load().subscribe(res=>{
      this.disabled=!this.disabled;
      this.loadingL=!this.loadingL;
      this.Refresh()
      this.onSuccess(res["affectedRow"])
    },
    error=>{
      this.onInvalid("Niewłaściwa operacja"); 
      this.disabled=!this.disabled;
      this.loadingL=!this.loadingL;});
  }
  ETL(){
    this.loadingETL=!this.loadingETL;
    this.disabled=!this.disabled;
    this.service.ETL().subscribe(res=>{
      this.disabled=!this.disabled;
      this.loadingETL=!this.loadingETL;
      this.onSuccess(res["loadView"]["affectedRow"])
      this.Refresh()
    },
    error=>{
      this.onInvalid("Niewłaściwa operacja"); 
      this.disabled=!this.disabled;
      this.loadingETL=!this.loadingETL;});
  }



//************************* */

  Refresh(){
    this.service.RefreshList().subscribe(res=>{
      this.dataSource.data = res as Comment[]
    })
  }
  Clear(){
    this.service.Clear().subscribe(res=>{
      if(res!=0) this.onSuccess(res)
      if(res==0) this.onInvalid('Brak elementów do usunięcia');
      this.Refresh();
    });
  }

  DownloadCSV(){
    this.service.DownloadCSV().subscribe(res=>{
      this.downloadFile(res)
    });
  }

  DownloadCSVID(){
    if(this.Id!=null){
    this.service.DownloadCSVId(this.Id).subscribe(res=>{
      this.downloadFile(res,this.Id)
      
    },
    error=>this.onInvalid("Brak elementu o podanym ID"));
  }
  }

  downloadFile(data: Blob, n?:number) {
    const blob = new Blob([data], { type: 'text/csv'});
    if(blob.size!=0){
      const url= window.URL.createObjectURL(blob);
      var link = document.createElement('a');
      link.href = url;
      if(n!=undefined)link.download = "comments"+n+".csv";
      else link.download = "comments.csv";
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
      window.URL.revokeObjectURL(url);
      link.remove();
    }
    if(blob.size==0)this.onInvalid("Brak elementów do pobrania");
  }

  onSuccess(n:any){
    this._service.success('Sukces','Operacja powiodła się dla '+n+' elementów',{
      timeOut: 4000,
      showProgressBar: true,
      pauseOnHover: true,
      clickToClose: true
    });
  }

  onInvalid(msg:string){
    this._service.warn('Ostrzeżenie',msg,{
      timeOut: 4000,
      showProgressBar: true,
      pauseOnHover: true,
      clickToClose: true,
      
    });
  }
  onError(){
    this._service.error('Błąd','Wystąpił nieoczekiwany błąd',{
      timeOut: 4000,
      showProgressBar: true,
      pauseOnHover: true,
      clickToClose: true
    });
  }
}