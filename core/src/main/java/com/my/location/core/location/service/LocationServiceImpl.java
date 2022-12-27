package com.my.location.core.location.service;

import com.my.location.core.coordinates.bean.CoordinatesData;
import com.my.location.core.location.bean.LocationData;
import com.my.location.core.location.persistance.LocationEntity;
import com.my.location.core.location.persistance.LocationRepository;
import com.my.location.core.coordinates.persistance.Coordinates;
import com.my.location.core.location.LocationRequestCache;
import com.my.location.core.location.bean.LocationObjectMapper;
import org.my.location.common.cache.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import static org.my.location.common.RandomUtils.MAX;
import static org.my.location.common.RandomUtils.MIN;


@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;
    /**
     * Used for retrieving location from cache by street name.
     */
    private final LocationRequestCache locationRequestsCache;

    public LocationServiceImpl( LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        this.locationRequestsCache = new LocationRequestCache(7);
    }

    @Override
    public CoordinatesData getCoordinatesBySteetName(final String streetName) {
    return CacheConfig.get().isCacheAvailable()
            ?  getCoordinatesFromCache(streetName) : LocationObjectMapper.mapToCoordinates(getLocationFromDb(streetName));

    }

    private CoordinatesData getCoordinatesFromCache(final String streetName){
        return locationRequestsCache.get(streetName) != null
                ? retrieveCoordinatesCache(streetName) : createCoordinatesCache(streetName);

    }

    private CoordinatesData retrieveCoordinatesCache(final String streetName){
        LocationData locationDataFromCache = locationRequestsCache.remove(streetName);

        LocationData updatedLocation = updateLastRequestTime(locationDataFromCache.getId());
        locationRequestsCache.put(streetName,
                LocationData.builder()
                            .id(locationDataFromCache.getId())
                            .streetName(locationDataFromCache.getStreetName())
                            .coordinates(locationDataFromCache.getCoordinates())
                            .lastModified(updatedLocation.getLastModified())
                            .build()
        );

        return LocationObjectMapper.mapToCoordinates(locationDataFromCache);
    }
    private CoordinatesData createCoordinatesCache(String streetName){

        LocationData locationData = getLocationFromDb(streetName);
        locationRequestsCache.put(streetName,
                LocationData.builder()
                        .id(locationData.getId())
                        .streetName(locationData.getStreetName())
                        .coordinates(locationData.getCoordinates())
                        .lastModified(locationData.getLastModified())
                        .build());
        return LocationObjectMapper.mapToCoordinates(locationData);

    }

    private LocationData getLocationFromDb(final String streetName){
        Optional<LocationEntity> locationEntity = locationRepository.findByStreetName(streetName);
        if(locationEntity.isPresent()){
            return LocationObjectMapper.map(locationEntity.get());
        }else {
            Random random = new Random();
            LocationEntity newLocationEntity = locationRepository.save(LocationEntity.builder()
                    .streetName(streetName)
                    .coordinates(new Coordinates(MIN + (MAX - MIN) * random.nextDouble(),
                            MIN + (MAX - MIN) * random.nextDouble()))
                    .lastModified(Date.from(Instant.now()))
                    .build());
            return LocationObjectMapper.map(newLocationEntity);
        }
    }

    private LocationData updateLastRequestTime(final long id) throws RuntimeException{
        Optional<LocationEntity> locationOpt = locationRepository.findById(id);
        if(locationOpt.isPresent()) {
            LocationEntity location = locationOpt.get();
            location.setLastModified(Date.from(Instant.now()));
            return LocationObjectMapper.map(locationRepository.save(location));
        }
        log.error("No such location by id: "+ id);
        throw new RuntimeException();

    }
}
