package com.example.demo.controller;

import com.example.demo.model.ProductModel;
import com.example.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductModel productModel){
        return productService.addProduct(productModel);
    }

    @DeleteMapping("deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        return productService.deleteProduct(productId);
    }

    @PutMapping("updateProduct/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody ProductModel product){
        return productService.updateProduct(productId,product);
    }

    @GetMapping("getProductById/{productId}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable Long productId){
        return productService.getProductById(productId);
    }

}
