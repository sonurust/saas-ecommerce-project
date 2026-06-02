import { AsyncPipe, CommonModule, JsonPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Router } from '@angular/router';
import { Product } from '../../model/product';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';
import { Order } from '../../model/order';

@Component({
  selector: 'app-home-page',
  imports: [
    CommonModule,
    FormsModule,
  ],
  standalone: true,
  templateUrl: './home-page.html',
  styleUrl: './home-page.css',
})
export class HomePage implements OnInit {
  private readonly oidcSecurityService = inject(OidcSecurityService);
  private productService = inject(ProductService);
  private orderService = inject(OrderService);
  private readonly router = inject(Router);
  isAuthenticated: boolean = false;
  products: Array<Product> = [];
  quantityIsNull: boolean = false;
  orderSuccess: boolean = false;
  orderFailed: boolean = false;


  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(({ isAuthenticated }) => {
      this.isAuthenticated = isAuthenticated;
    });

    this.productService.getProducts()
      .pipe()
      .subscribe((products) => {
        this.products = products;
      });



  }


  goToCreateProductPage() {
    this.router.navigateByUrl('/add-product');
  }

  placeOrder(product: Product, quantity: number) {

    this.oidcSecurityService.userData$.subscribe(({ userData }) => {
      const userDetails = {
        username: userData?.preferred_username || '',
        email: userData?.email || '',
        name: userData?.name || ''
      };
      if(!quantity) {
        this.orderFailed = false;
        this.orderSuccess = false;
        this.quantityIsNull = true;
        return;
      }

      const order: Order = {
        skuCode: product.skuCode || 'iphone_15', // missing in the original code, added here
        price: product.price,
        quantity: Number(quantity),
        // userDetails: userDetails
      };

      this.orderService.placeOrder(order)
        .subscribe({
          next: (response) => {
            console.log('Order placed successfully', response);
            this.orderSuccess = true;
          },
          error: (error) => {
            console.error('Failed to place order', error);
            this.orderFailed = true;
          }
        });
    });


  }
}
