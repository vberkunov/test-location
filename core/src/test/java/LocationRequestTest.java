import com.my.location.core.coordinates.bean.CoordinatesData;
import com.my.location.core.location.LocationRequestCache;
import com.my.location.core.location.bean.LocationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import java.time.Instant;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class LocationRequestTest {

    private static final String STREET_NAME = "Street";
    private static final Date LAST_MODIFIED = Date.from(Instant.now());
    private LocationRequestCache cache;



    @Test
    public void testSavingLocationCache(){
        cache = new LocationRequestCache(1);

        LocationData locationDataExpected = LocationData.builder()
                .id(1L)
                .streetName(STREET_NAME)
                .coordinates(new CoordinatesData(3.57,5.64))
                .lastModified(LAST_MODIFIED)
                .build();
        cache.put(STREET_NAME,locationDataExpected);

        assertEquals(locationDataExpected, cache.get(STREET_NAME));
    }

    @Test
    public void testRemovingLocationCache(){
        cache = new LocationRequestCache(1);

        LocationData locationDataExpected = LocationData.builder()
                .id(1L)
                .streetName(STREET_NAME)
                .coordinates(new CoordinatesData(3.57,5.64))
                .lastModified(LAST_MODIFIED)
                .build();
        cache.put(STREET_NAME,locationDataExpected);
        cache.remove(STREET_NAME);
        assertEquals(null, cache.get(STREET_NAME));

    }

}
