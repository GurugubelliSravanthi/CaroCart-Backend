package com.carocart.address.service;

import com.carocart.address.DTO.AddressRequestDTO;
import com.carocart.address.DTO.AddressResponseDTO;
import com.carocart.address.DTO.UserResponseDTO;
import com.carocart.address.entity.Address;
import com.carocart.address.feign.UserClient;
import com.carocart.address.repository.AddressRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;
    
    private final UserClient userClient;

    public AddressServiceImpl(AddressRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    private AddressResponseDTO mapToDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setFullName(address.getFullName());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setAddressType(address.getAddressType());
        dto.setIsDefault(address.getIsDefault());
        dto.setFullAddress(
            address.getHouseNumber() + ", " +
            address.getStreet() + ", " +
            address.getCity() + ", " +
            address.getState() + " - " +
            address.getPincode()
        );
        return dto;
    }

    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO dto, String token) {
        // Call auth service and get the current user
        UserResponseDTO user = userClient.getCurrentUser(token);

        Address address = new Address();
        address.setUserId(user.getId());
        address.setFullName(dto.getFullName());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setAlternatePhone(dto.getAlternatePhone());
        address.setPincode(dto.getPincode());
        address.setHouseNumber(dto.getHouseNumber());
        address.setStreet(dto.getStreet());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setAddressType(dto.getAddressType());
        address.setIsDefault(dto.getIsDefault());

        return mapToDTO(repository.save(address));
    }

    @Override
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto) {
        Address address = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setFullName(dto.getFullName());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setAlternatePhone(dto.getAlternatePhone());
        address.setPincode(dto.getPincode());
        address.setHouseNumber(dto.getHouseNumber());
        address.setStreet(dto.getStreet());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setAddressType(dto.getAddressType());
        address.setIsDefault(dto.getIsDefault());

        return mapToDTO(repository.save(address));
    }

    @Override
    public void deleteAddress(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<AddressResponseDTO> getAllAddressesByUserId(Long userId) {
        List<Address> addresses = repository.findByUserId(userId);
        List<AddressResponseDTO> response = new ArrayList<>();
        for (Address address : addresses) {
            response.add(mapToDTO(address));
        }
        return response;
    }

    @Override
    public AddressResponseDTO getAddressById(Long id) {
        Address address = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return mapToDTO(address);
    }
    
    @Override
    public List<AddressResponseDTO> getAddressesForCurrentUser(String token) {
        UserResponseDTO user = userClient.getCurrentUser(token);
        List<Address> addresses = repository.findByUserId(user.getId());

        List<AddressResponseDTO> response = new ArrayList<>();
        for (Address address : addresses) {
            response.add(mapToDTO(address));
        }
        return response;
    }

}
