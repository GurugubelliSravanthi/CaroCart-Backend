package com.carocart.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carocart.address.entity.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(String userId);
}
