package com.maksgir.sneakers.repository;

import com.maksgir.sneakers.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, String> {
    boolean existsByUserId(String userId);
}
