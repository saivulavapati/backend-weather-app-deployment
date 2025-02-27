package com.weatherapp.whishlist_service.service;

import com.weatherapp.whishlist_service.entity.City;
import com.weatherapp.whishlist_service.entity.Wishlist;
import com.weatherapp.whishlist_service.repository.CityRepository;
import com.weatherapp.whishlist_service.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImpTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private WishlistServiceImp wishlistService;

    private Wishlist wishlist;
    private String userEmail = "sai@gmail.com";
    private String cityName = "Hyderabad";

    @BeforeEach
    void setUp() {
        wishlist = new Wishlist();
        wishlist.setEmail(userEmail);
        wishlist.setCities(new HashSet<>());
    }

    @Test
    void testAddCityToWishlist_NewWishlist() {
        when(wishlistRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        Wishlist result = wishlistService.addCityToWishlist(userEmail, cityName);

        assertNotNull(result);
        assertEquals(userEmail, result.getEmail());
        assertFalse(result.getCities().stream().anyMatch(city -> city.getCityName().equals(cityName)));
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }

    @Test
    void testAddCityToWishlist_ExistingWishlist() {
        when(wishlistRepository.findByEmail(userEmail)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        Wishlist result = wishlistService.addCityToWishlist(userEmail, cityName);

        assertNotNull(result);
        assertTrue(result.getCities().stream().anyMatch(city -> city.getCityName().equals(cityName)));
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }

    @Test
    void testGetUserWishlist() {
        Set<City> cities = new HashSet<>();
        City city = new City();
        city.setCityName(cityName);
        cities.add(city);
        wishlist.setCities(cities);

        when(wishlistRepository.findByEmail(userEmail)).thenReturn(Optional.of(wishlist));

        Set<City> result = wishlistService.getUserWishlist(userEmail);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.contains(city));
    }

    @Test
    void testRemoveCityFromWishlist() {
        City city = new City();
        city.setCityName(cityName);
        wishlist.getCities().add(city);

        when(wishlistRepository.findByEmail(userEmail)).thenReturn(Optional.of(wishlist));

        wishlistService.removeCityFromWishlist(userEmail, cityName);

        assertFalse(wishlist.getCities().contains(city));
        verify(wishlistRepository, times(1)).save(wishlist);
    }
}
