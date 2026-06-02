import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../model/product';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private HttpClient: HttpClient) {}

  getProducts() : Observable<Array<Product>> {
    return this.HttpClient.get<Array<Product>>('http://localhost:9000/api/product');
  }

  createProduct(product: Product) : Observable<Product> {
    return this.HttpClient.post<Product>('http://localhost:9000/api/product', product);
  }
}
