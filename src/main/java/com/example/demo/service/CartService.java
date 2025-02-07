package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.user.LowProductStockException;
import com.example.demo.exception.user.ResourceNotFoundException;
import com.example.demo.exception.user.UserNotLoggedInException;
import com.example.demo.repository.CartItemsRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemsRepository cartItemsRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addToCart(Long productId, Long cartId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotLoggedInException("Must be logged in."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (!cart.getUser().equals(user)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }

        CartItem existingCartItem = cartItemsRepository.findByCartAndProduct(cart,product);

        if(product.getStockQuantity() <= 0){
            throw new LowProductStockException("Low product stock");
        }

        if(existingCartItem != null){
            existingCartItem.setQuantity(existingCartItem.getQuantity()+1);
            cartItemsRepository.save(existingCartItem);
        }else{
            CartItem item = new CartItem();
            item.setQuantity(1);
            item.setCart(cart);
            item.setProduct(product);
            cartItemsRepository.save(item);
        }

        cart.setUpdatedAt(new Date(System.currentTimeMillis()));
        cartRepository.save(cart);

        return ResponseEntity.ok("Item added to cart.");
    }


    public ResponseEntity<String> deleteFromCart(Long cartId,Long productId) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotLoggedInException("Must be logged in."));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        if (!cart.getUser().equals(user)) {
            throw new AccessDeniedException("Access denied.");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found;"));

        CartItem existingCartItem = cartItemsRepository.findByCartAndProduct(cart,product);

        if(existingCartItem.getQuantity() > 0) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() - 1);
            cartItemsRepository.save(existingCartItem);
            cart.setUpdatedAt(new Date(System.currentTimeMillis()));
            cartRepository.save(cart);
            return ResponseEntity.ok("Item removed.");
        }else{
            existingCartItem.setQuantity(0);
            cartItemsRepository.save(existingCartItem);
            cart.setUpdatedAt(new Date(System.currentTimeMillis()));
            cartRepository.save(cart);
            return ResponseEntity.ok("");
        }

    }


    public ResponseEntity<String> createNewCart() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotLoggedInException("Must be logged in."));


        int cartCount = cartRepository.countByUser(user);
        if(cartCount >= 5){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot create more than 5 carts.");
        }
        Cart cart = new Cart();
        cart.setCreatedAt(new Date(System.currentTimeMillis()));
        cart.setUpdatedAt(new Date(System.currentTimeMillis()));
        cart.setUser(user);
        cartRepository.save(cart);

        return ResponseEntity.ok("Cart created.");
    }
}
