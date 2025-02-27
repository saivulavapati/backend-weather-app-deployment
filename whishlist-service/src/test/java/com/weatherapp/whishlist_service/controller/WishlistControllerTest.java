package com.weatherapp.whishlist_service.controller;

import com.weatherapp.whishlist_service.entity.City;
import com.weatherapp.whishlist_service.entity.Wishlist;
import com.weatherapp.whishlist_service.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private WishlistController wishlistController;

    private Wishlist wishlist;
    private Set<City> cities;
    private String userEmail = "sai@gmail.com";
    private String cityName = "Hyderabad";

    @BeforeEach
    void setUp() {
        wishlist = new Wishlist();
        wishlist.setEmail(userEmail);
        cities = new HashSet<>();
        wishlist.setCities(cities);
    }

    @Test
    void testAddToWishlist_Success() {
        when(request.getHeader("Authenticated-Email")).thenReturn(userEmail);
        when(wishlistService.addCityToWishlist(anyString(), anyString())).thenReturn(wishlist);

        ResponseEntity<?> response = wishlistController.addToWishlist(cityName, request);

        assertEquals(200, response.getStatusCode().value());
        verify(wishlistService, times(1)).addCityToWishlist(userEmail, cityName);
    }

    @Test
    void testAddToWishlist_Unauthorized() {
        when(request.getHeader("Authenticated-Email")).thenReturn(null);

        ResponseEntity<?> response = wishlistController.addToWishlist(cityName, request);

        assertEquals(401, response.getStatusCode().value());
        verify(wishlistService, never()).addCityToWishlist(anyString(), anyString());
    }

    @Test
    void testGetWishlist_Success() {
        City city = new City();
        city.setCityName(cityName);
        cities.add(city);

        when(wishlistService.getUserWishlist(userEmail)).thenReturn(cities);

        ResponseEntity<Set<City>> response = wishlistController.getWishlist(userEmail);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testRemoveFromWishlist_Success() {
        doNothing().when(wishlistService).removeCityFromWishlist(userEmail, cityName);

        ResponseEntity<Void> response = wishlistController.removeFromWishlist(userEmail, cityName);

        assertEquals(204, response.getStatusCode().value());
        verify(wishlistService, times(1)).removeCityFromWishlist(userEmail, cityName);
    }
}
