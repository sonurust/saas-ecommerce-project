import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order } from '../model/order';
import { response } from 'express';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {

  constructor(private httpClient: HttpClient) { 

  }

  placeOrder(order: Order) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      responseType: 'text' as 'json'
    };
    return this.httpClient.post('http://localhost:9000/api/order', order, httpOptions);
  }
}
