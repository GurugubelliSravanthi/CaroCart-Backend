package com.carocart.cartservice.service;

import com.carocart.cartservice.dto.*;
import com.carocart.cartservice.entity.CartItem;
import com.carocart.cartservice.feign.ProductClient;
import com.carocart.cartservice.feign.UserClient;
import com.carocart.cartservice.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Override
    public CartItemDTO addToCart(String token, AddToCartRequestDTO request) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        Long userId = user.getId();
        ProductDTO product = productClient.getProductDTOById(request.getProductId());

        if (product == null || !product.isAvailable()) {
            throw new RuntimeException("Product unavailable.");
        }
        if (request.getQuantity() > product.getStock()) {
            throw new RuntimeException("Requested quantity exceeds stock.");
        }

        Optional<CartItem> existingOpt = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        CartItem item;
        if (existingOpt.isPresent()) {
            item = existingOpt.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            item = new CartItem(userId, request.getProductId(), request.getQuantity());
        }
        CartItem saved = cartItemRepository.save(item);
        return convertToDTO(saved, product);
    }

    @Override
    public CartItemDTO updateCartItem(String token, Long productId, int quantity) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        Long userId = user.getId();
        ProductDTO product = productClient.getProductDTOById(productId);

        if (product == null || !product.isAvailable()) {
            throw new RuntimeException("Product unavailable.");
        }
        if (quantity > product.getStock()) {
            throw new RuntimeException("Quantity exceeds available stock.");
        }

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
            .orElseThrow(() -> new RuntimeException("Item not in cart"));
        item.setQuantity(quantity);
        CartItem saved = cartItemRepository.save(item);
        return convertToDTO(saved, product);
    }

    @Override
    @Transactional
    public void removeCartItem(String token, Long productId) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Override
    public void clearCart(String token) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        List<CartItem> items = cartItemRepository.findByUserId(user.getId());
        cartItemRepository.deleteAll(items);
    }

    @Override
    public List<CartItemResponse> getCartItemsByToken(String token) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        List<CartItem> items = cartItemRepository.findByUserId(user.getId());

        return items.stream().map(item -> {
            ProductDTO product = productClient.getProductDTOById(item.getProductId());
            return new CartItemResponse(
                item.getId(),
                item.getUserId(),
                item.getProductId(),
                product.getName(),
                item.getQuantity(),
                product.getPrice(),
                product.getImageUrl()
            );
        }).collect(Collectors.toList());
    }

    private CartItemDTO convertToDTO(CartItem item, ProductDTO product) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setUserId(item.getUserId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());

        if (product != null) {
            dto.setProductName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setImageUrl(product.getImageUrl());
        }
        return dto;
    }

//	@Override
//	public List<CartItemDTO> getCartItemsByUserId(Long userId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
