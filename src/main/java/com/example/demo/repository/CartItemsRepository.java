package com.example.demo.repository;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CartItemsRepository extends JpaRepository<CartItem,Long> {

    boolean existsByProduct(Product product);

    CartItem findByProduct(Product product);

    CartItem findByProductAndCart(Product product, Cart cart);

    CartItem findByCartAndProduct(Cart cart, Product product);

    Set<CartItem> findByCartId(Long cartId);
}
