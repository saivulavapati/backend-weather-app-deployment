package com.weatherapp.whishlist_service.service;

import com.weatherapp.whishlist_service.entity.City;
import com.weatherapp.whishlist_service.entity.Wishlist;
import com.weatherapp.whishlist_service.repository.WishlistRepository;

import java.util.Set;

public interface WishlistService {
    Wishlist addCityToWishlist(String email, String cityName);

    Set<City> getUserWishlist(String email);

    void removeCityFromWishlist(String email, String cityName);
}
