package com.weatherapp.whishlist_service.controller;

import com.weatherapp.whishlist_service.entity.City;
import com.weatherapp.whishlist_service.entity.Wishlist;
import com.weatherapp.whishlist_service.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestParam String city, HttpServletRequest request) {
        String username = request.getHeader("Authenticated-Email");

        System.out.println(city);
        System.out.println(username);

        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: User not found in request headers");
        }
        Wishlist wishlist = wishlistService.addCityToWishlist(username, city);
        return ResponseEntity.ok().body(wishlist);
    }

    @GetMapping
    public ResponseEntity<Set<City>> getWishlist(
            @RequestHeader("Authenticated-Email") String email) {
        Set<City> userWishlist = wishlistService.getUserWishlist(email);
        if(userWishlist == null){
            Set<City> wishList = new HashSet<>();
            return ResponseEntity.ok(wishList);
        }
        return ResponseEntity.ok(userWishlist);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWishlist(
            @RequestHeader("Authenticated-Email") String email,
            @RequestParam String city) {
        wishlistService.removeCityFromWishlist(email, city);
        return ResponseEntity.noContent().build();
    }

}
