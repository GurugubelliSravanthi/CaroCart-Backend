package com.carocart.address.service;

import java.util.List;

import com.carocart.address.DTO.AddressRequestDTO;
import com.carocart.address.DTO.AddressResponseDTO;

public interface AddressService {
    
    // Now requires JWT token to get the userId from Auth service
    AddressResponseDTO createAddress(AddressRequestDTO dto, String token);

    AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto);

    void deleteAddress(Long id);

    // Still uses userId directly (useful for admin or dashboard views)
    List<AddressResponseDTO> getAllAddressesByUserId(Long userId);

    AddressResponseDTO getAddressById(Long id);

	List<AddressResponseDTO> getAddressesForCurrentUser(String token);
}
