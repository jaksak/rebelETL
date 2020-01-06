export interface Comment {
    id:number;
    commentRating:number;
    date:Date;
    nick:string;
    productRating?:number;
    text:string;
    url:string;
}
export class Comment {
    id:number;
    commentRating:number;
    date:Date;
    nick:string;
    productRating?:number;
    text:string;
    url:string;
}