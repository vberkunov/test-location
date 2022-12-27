import com.my.location.core.coordinates.bean.CoordinatesData;
import com.my.location.core.coordinates.persistance.Coordinates;
import com.my.location.core.location.bean.LocationData;
import com.my.location.core.location.persistance.LocationEntity;
import com.my.location.core.location.persistance.LocationRepository;
import com.my.location.core.location.service.LocationServiceImpl;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.my.location.common.cache.CacheConfig;


import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

    private static final String STREET_NAME = "Street";
    private static final Date LAST_MODIFIED = Date.from(Instant.now());
    private static final double DELTA = 1e-15;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Mock
    private LocationRepository locationRepository;

    @Test
    public void testRetrievingCoordinatesFromEmptyCache(){
        LocationData locationDataExpected = LocationData.builder()
                                                .id(1L)
                                                .streetName(STREET_NAME)
                                                .coordinates(new CoordinatesData(3.57,5.64))
                                                .lastModified(LAST_MODIFIED)
                                                .build();
        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .streetName(STREET_NAME)
                .coordinates(new Coordinates(3.57,5.64))
                .lastModified(LAST_MODIFIED)
                .build();
        when(locationRepository.findByStreetName(STREET_NAME)).thenReturn(Optional.of(locationEntity));

        CoordinatesData coordinatesData = locationService.getCoordinatesBySteetName(STREET_NAME);

        assertEquals(locationDataExpected.getCoordinates().getX(),coordinatesData.getX(),DELTA);


    }

    @Test
    public void testRetrievingCoordinatesWithoutCache(){
        CacheConfig.get().setIsCacheAvailable(false);
        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .streetName(STREET_NAME)
                .coordinates(new Coordinates(3.57,5.64))
                .lastModified(LAST_MODIFIED)
                .build();

        when(locationRepository.findByStreetName(STREET_NAME)).thenReturn(Optional.of(locationEntity));

        CoordinatesData coordinatesData = locationService.getCoordinatesBySteetName(STREET_NAME);

        assertEquals(locationEntity.getCoordinates().getX(), coordinatesData.getX(),DELTA);
        assertEquals(locationEntity.getCoordinates().getY(), coordinatesData.getY(),DELTA);

        verify(locationRepository,atLeastOnce()).findByStreetName(STREET_NAME);

    }



}
