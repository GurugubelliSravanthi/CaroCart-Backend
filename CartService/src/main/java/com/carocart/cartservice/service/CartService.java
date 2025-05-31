package com.carocart.cartservice.service;

import com.carocart.cartservice.dto.AddToCartRequestDTO;
import com.carocart.cartservice.dto.CartItemDTO;
import com.carocart.cartservice.dto.CartItemResponse;

import java.util.List;

public interface CartService {
//	List<CartItemDTO> getCartItemsByUserId(Long userId);

	CartItemDTO addToCart(String token, AddToCartRequestDTO request);

	CartItemDTO updateCartItem(String token, Long productId, int quantity);

	void removeCartItem(String token, Long productId);

	void clearCart(String token);

	List<CartItemResponse> getCartItemsByToken(String token);

}
