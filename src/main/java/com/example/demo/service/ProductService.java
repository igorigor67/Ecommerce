package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.user.ResourceNotFoundException;
import com.example.demo.model.ProductModel;
import com.example.demo.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public ResponseEntity<String> addProduct(ProductModel productModel) {
        Product product = new Product();
        product.setName(productModel.getName());
        product.setPrice(productModel.getPrice());
        product.setStockQuantity(productModel.getStockQuantity());

        if(Objects.equals(productModel.getDescription(), "")){
            product.setDescription("No description.");
        }else {
            product.setDescription(productModel.getDescription());
        }

        productRepository.save(product);
        return ResponseEntity.ok("Product added.");
    }

    public ResponseEntity<String> deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        productRepository.delete(product);
        return ResponseEntity.ok("Product deleted.");
    }

    public ResponseEntity<String> updateProduct(Long productId, ProductModel product) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setDescription(product.getDescription());
        if(Objects.equals(existingProduct.getDescription(),"")){
            existingProduct.setDescription("No description");
        }

        productRepository.save(existingProduct);
        return ResponseEntity.ok("Product updated.");
    }


    public ResponseEntity<ProductModel> getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
        ProductModel productModel = new ProductModel();
        productModel.setName(product.getName());
        productModel.setPrice(product.getPrice());
        productModel.setStockQuantity(product.getStockQuantity());
        productModel.setDescription(product.getDescription());

        return new ResponseEntity<>(productModel, HttpStatus.FOUND);
    }
}
