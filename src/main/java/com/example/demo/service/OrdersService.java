package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.user.LowProductStockException;
import com.example.demo.exception.user.ResourceNotFoundException;
import com.example.demo.exception.user.UserNotLoggedInException;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;


@Service
public class OrdersService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final MailService mailService;

    public OrdersService(CartRepository cartRepository, CartItemsRepository cartItemsRepository, OrdersRepository ordersRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, MailService mailService) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
    }

    @Transactional
    public ResponseEntity<String> placeOrder(Long cartId) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotLoggedInException("Must be logged in."));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        Set<CartItem> cartItems = cartItemsRepository.findByCartId(cartId);

        if (cartItems.isEmpty()) {
            return new ResponseEntity<>("Cart is empty. Cannot place order.", HttpStatus.BAD_REQUEST);
        }

        Orders order = new Orders();
        order.setOrderDate(new Date(System.currentTimeMillis()));
        order.setTotalAmount(0);
        order.setUser(user);
        ordersRepository.save(order);

        double totalAmount = 0;
        StringBuilder orderDetails = new StringBuilder("<h3>Your Order Details:</h3><ul>");

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new LowProductStockException("Insufficient stock for " + product.getName());
            }
        }

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).get();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrders(order);
            orderItem.setProduct(product);
            orderItemRepository.save(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            orderDetails.append("<li>").append(product.getName())
                    .append(" - Quantity: ").append(cartItem.getQuantity())
                    .append(", Price: $").append(product.getPrice())
                    .append("</li>");

            cartItemsRepository.delete(cartItem);
        }

        order.setTotalAmount(totalAmount);
        ordersRepository.save(order);

        cartRepository.delete(cart);

        String emailBody = orderDetails.append("</ul><br><strong>Total Amount: $").append(totalAmount).append("</strong>").toString();
        mailService.sendOrderConfirmation(user.getEmail(), "Order Confirmation - #" + order.getId(), emailBody);

        return new ResponseEntity<>("Order placed successfully. A confirmation email has been sent.", HttpStatus.OK);
    }

}
