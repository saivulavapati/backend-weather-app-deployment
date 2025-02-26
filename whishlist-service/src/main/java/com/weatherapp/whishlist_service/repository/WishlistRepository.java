package com.weatherapp.whishlist_service.repository;

import com.weatherapp.whishlist_service.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {

    Optional<Wishlist> findByEmail(String email);
}
