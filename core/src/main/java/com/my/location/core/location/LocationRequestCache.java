package com.my.location.core.location;

import com.my.location.core.location.bean.LocationData;
import org.my.location.common.cache.ExpiringCache;

import java.util.concurrent.TimeUnit;

public class LocationRequestCache {

    private final ExpiringCache<String, LocationData> cache;

    public LocationRequestCache(int minutes) {

        this.cache = new ExpiringCache<>(TimeUnit.MINUTES.toMillis(minutes));
    }

    /**
     * Puts location request by street name
     *
     * @param streetName unique identifier of location
     * @param locationRequest {@link LocationData}
     */
    public void put(String streetName, LocationData locationRequest) {
        cache.put(streetName, locationRequest);
    }

    /**
     * Returns and removes request by street name.
     *
     * @param streetName recurring transactionId.
     * @return {@link LocationData}
     */
    public LocationData remove(String streetName) {
        return cache.remove(streetName);
    }

    /**
     * Retrieves request by street name.
     *
     * @param streetName recurring transactionId.
     * @return {@link LocationData}
     */
    public LocationData get(String streetName) {
        return cache.get(streetName);
    }

}
