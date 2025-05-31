package com.carocart.cartservice.controller;

import com.carocart.cartservice.dto.AddToCartRequestDTO;
import com.carocart.cartservice.dto.CartItemDTO;
import com.carocart.cartservice.dto.CartItemResponse;
import com.carocart.cartservice.dto.UpdateCartRequestDTO;
import com.carocart.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Get cart items for the authenticated user via token
    @GetMapping("/get")
    public ResponseEntity<List<CartItemResponse>> getCartItemsByToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(cartService.getCartItemsByToken(token));
    }

    // Add an item to the cart, JSON body contains productId and quantity
    @PostMapping("/add")
    public ResponseEntity<CartItemDTO> addToCart(@RequestHeader("Authorization") String token,
                                                 @RequestBody AddToCartRequestDTO request) {
        return ResponseEntity.ok(cartService.addToCart(token, request));
    }

    // Update cart item quantity using JSON body (productId and quantity)
    @PutMapping("/update")
    public ResponseEntity<CartItemDTO> updateCart(@RequestHeader("Authorization") String token,
                                                  @RequestBody UpdateCartRequestDTO request) {
        return ResponseEntity.ok(cartService.updateCartItem(token, request.getProductId(), request.getQuantity()));
    }

    // Remove an item from cart using productId as path variable (RESTful)
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeItem(@RequestHeader("Authorization") String token,
                                           @PathVariable Long productId) {
        cartService.removeCartItem(token, productId);
        return ResponseEntity.noContent().build();
    }

    // Clear entire cart for user
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestHeader("Authorization") String token) {
        cartService.clearCart(token);
        return ResponseEntity.noContent().build();
    }
}
