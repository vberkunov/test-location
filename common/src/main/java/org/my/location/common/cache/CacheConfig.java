package org.my.location.common.cache;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigureMe(name = "cache", allfields = true)
public class CacheConfig {
    /**
     * {@link Logger} instance.
     */
    private static Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Configure
    private boolean isCacheAvailable;


    public static CacheConfig get() {
        return CacheConfigHolder.instance;
    }



    static class CacheConfigHolder {
        private static CacheConfig instance;

        static {
            instance = new CacheConfig();
            try {
                ConfigurationManager.INSTANCE.configure(instance);
            } catch (IllegalArgumentException e) {
                log.warn("No cache.json config file found. Continuing with defaults.");
            }
            log.info("Cache configured: " + instance);
        }
    }

    public boolean isCacheAvailable() {
        return isCacheAvailable;
    }

    public void setIsCacheAvailable(boolean cacheAvailable) {
        isCacheAvailable = cacheAvailable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CacheConfig{");
        sb.append("isAvailable=").append(isCacheAvailable);
        sb.append('}');
        return sb.toString();
    }
}
