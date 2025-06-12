package com.carocart.address.controller;

import org.springframework.web.bind.annotation.*;

import com.carocart.address.DTO.AddressRequestDTO;
import com.carocart.address.DTO.AddressResponseDTO;
import com.carocart.address.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    // ✅ Create address with token to fetch user info
    @PostMapping
    public AddressResponseDTO create(@RequestBody AddressRequestDTO dto,
                                     @RequestHeader("Authorization") String token) {
        return service.createAddress(dto, token);
    }

    // ✅ Update address by ID (userId not needed)
    @PutMapping("/{id}")
    public AddressResponseDTO update(@PathVariable Long id,
                                     @RequestBody AddressRequestDTO dto) {
        return service.updateAddress(id, dto);
    }

    // ✅ Delete address by ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAddress(id);
    }

    // ✅ Get all addresses for a user (could be admin use)
    @GetMapping("/user/{userId}")
    public List<AddressResponseDTO> getAllByUser(@PathVariable String userId) {
        return service.getAllAddressesByUserId(userId);
    }

    // ✅ Get address by address ID
    @GetMapping("/{id}")
    public AddressResponseDTO getOne(@PathVariable Long id) {
        return service.getAddressById(id);
    }
}
