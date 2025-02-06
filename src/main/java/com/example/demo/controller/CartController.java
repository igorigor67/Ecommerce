package com.example.demo.controller;

import com.example.demo.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("addToCart/{productId}/{cartId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId, @PathVariable Long cartId ) {
        return cartService.addToCart(productId,cartId);
    }

    @DeleteMapping("deleteFromCart/{cartId}/{productId}")
    public ResponseEntity<String> deleteFromCart(@PathVariable Long cartId,@PathVariable Long productId){
        return cartService.deleteFromCart(cartId,productId);
    }

    @PostMapping("createNewCart")
    public ResponseEntity<String> createNewCart(){
        return cartService.createNewCart();
    }


}
