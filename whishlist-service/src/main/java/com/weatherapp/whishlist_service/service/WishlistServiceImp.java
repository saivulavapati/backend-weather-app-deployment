package com.weatherapp.whishlist_service.service;

import com.weatherapp.whishlist_service.entity.City;
import com.weatherapp.whishlist_service.entity.Wishlist;
import com.weatherapp.whishlist_service.repository.CityRepository;
import com.weatherapp.whishlist_service.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class WishlistServiceImp implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Wishlist addCityToWishlist(String email, String cityName) {
        Optional<Wishlist> wishlistOptional = wishlistRepository.findByEmail(email);
        Wishlist wishlist;
        if (wishlistOptional.isPresent()) {
            wishlist = wishlistOptional.get();
        } else {
            wishlist = new Wishlist();
            wishlist.setEmail(email);
            wishlist.setCities(new HashSet<>());
        }

        boolean isCityPresent = wishlist.getCities().stream()
                .anyMatch(city -> city.getCityName().equalsIgnoreCase(cityName));
        if (!isCityPresent) {
            City city = new City();
            city.setCityName(cityName);
            city.setWishlist(wishlist);
            wishlist.getCities().add(city);
        }
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Set<City> getUserWishlist(String email) {
        return wishlistRepository.findByEmail(email)
                .map(Wishlist::getCities)
                .orElse(Collections.emptySet());
    }

    @Override
    public void removeCityFromWishlist(String email, String cityName) {
        Optional<Wishlist> optionalWishlist = wishlistRepository.findByEmail(email);
        if(optionalWishlist.isPresent()){
            Wishlist wishlist = optionalWishlist.get();
            City cityToRemove = wishlist.getCities().stream().filter(city -> city.getCityName().equalsIgnoreCase(cityName))
                    .findFirst().orElse(null);
            if(cityToRemove != null){
                wishlist.getCities().remove(cityToRemove);
                wishlistRepository.save(wishlist);
            }
        }

    }
}

