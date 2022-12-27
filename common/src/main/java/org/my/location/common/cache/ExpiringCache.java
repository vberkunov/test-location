package org.my.location.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public final class ExpiringCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(ExpiringCache.class);

    private final ConcurrentMap<K, ExpiringValueWrapper<V>> cache;

    public ExpiringCache(long expirationTime) {
        this(expirationTime, 1, TimeUnit.MINUTES);
    }

    public ExpiringCache(long expirationTime, long checkPeriod, TimeUnit timeUnit) {
        this.cache = new ConcurrentHashMap<>();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new RemoveExpiredValuesJob<>(this.cache, expirationTime), 0, checkPeriod, timeUnit);
    }

    /**
     * Puts value into a cache.
     *
     * @param key   key
     * @param value value
     */
    public void put(K key, V value) {
        ExpiringValueWrapper<V> valueWrapper = new ExpiringValueWrapper<>(value);
        cache.put(key, valueWrapper);
    }

    /**
     * Returns value from cache.
     *
     * @param key key
     * @return value or {@code null} if value expired or not exist
     */
    public V get(K key) {
        ExpiringValueWrapper<V> valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return valueWrapper.getValue();
    }

    /**
     * Removes value from cache.
     *
     * @param key key
     * @return stored value or {@code null} if value expired or not exist
     */
    public V remove(K key) {
        ExpiringValueWrapper<V> valueWrapper = cache.remove(key);
        if (valueWrapper == null) {
            return null;
        }
        return valueWrapper.getValue();
    }

    /**
     * Checks if value expired and removes it if expired.
     *
     * @param <K> key type
     * @param <V> value type
     */
    private static class RemoveExpiredValuesJob<K, V> implements Runnable {

        private final ConcurrentMap<K, ExpiringValueWrapper<V>> cache;
        /**
         * How long added value stays not expired.
         */
        private final long expirationTime;

        public RemoveExpiredValuesJob(ConcurrentMap<K, ExpiringValueWrapper<V>> cache, long expirationTime) {
            this.cache = cache;
            this.expirationTime = expirationTime;
        }

        @Override
        public void run() {
            try {
                Iterator<Map.Entry<K, ExpiringValueWrapper<V>>> iterator = this.cache.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<K, ExpiringValueWrapper<V>> cacheEntry = iterator.next();

                    long now = System.currentTimeMillis();
                    ExpiringValueWrapper<V> valueWrapper = cacheEntry.getValue();
                    if (valueWrapper == null) {
                        // remove null value automatically
                        iterator.remove();
                        continue;
                    }

                    // remove if value expired
                    if (now - valueWrapper.getTimestamp() > expirationTime) {
                        iterator.remove();
                    }

                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Wrapper for value which contains info about time when value was added.
     *
     * @param <T> value type
     */
    private static class ExpiringValueWrapper<T> {

        private final T value;
        private final long timestamp;

        public ExpiringValueWrapper(T value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }

        public T getValue() {
            return value;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}