import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { Product } from '../../model/product';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-product',
  imports: [
    CommonModule,
    ReactiveFormsModule],
  standalone: true,
  templateUrl: './add-product.html',
  styleUrl: './add-product.css',
})
export class AddProduct {
  addProductForm: FormGroup;
  private readonly productService = inject(ProductService);
  productCreated: boolean = false;

  constructor(private fb:FormBuilder) {
    this.addProductForm = this.fb.group({
      skuCode: ['', [Validators.required]],
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: [0, [Validators.required, Validators.min(1), Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
    });
  }

  onSubmit() {
    if (this.addProductForm.valid) {
      const product: Product = {
        skuCode: this.addProductForm.value.skuCode,
        name: this.addProductForm.value.name,
        description: this.addProductForm.value.description,
        price: this.addProductForm.value.price,
      };

      this.productService.createProduct(product).subscribe({
        next: (response) => {
          console.log('Product created successfully', response);
          this.productCreated = true;
          this.addProductForm.reset();
        },
        error: (error) => {
          console.error('Failed to create product', error);
          this.productCreated = false;
        }
      });
    } else {
      console.error('Form is invalid');
    }
  }
}
