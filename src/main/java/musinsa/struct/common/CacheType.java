package musinsa.struct.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CacheType {
    PRODUCT_3S(CachePrefix.PRODUCT_1M, 3),

    PRODUCT_1M(CachePrefix.PRODUCT_1M, 60),

    BRAND_3S(CachePrefix.BRAND_3S, 3),

    BRAND_1M(CachePrefix.BRAND_1M, 60),

    CATEGORY_3S(CachePrefix.CATEGORY_3S, 3),

    CATEGORY_1M(CachePrefix.CATEGORY_1M, 60),

    LOWEST_PRICE_CATEGORIES_3S(CachePrefix.LOWEST_PRICE_CATEGORIES_3S, 3),

    LOWEST_PRICE_BRANDS_3S(CachePrefix.LOWEST_PRICE_BRANDS_3S, 3),

    LOWEST_HIGHEST_CATEGORY_3S(CachePrefix.LOWEST_HIGHEST_CATEGORY_3S, 3),
    ;

    private final String cacheName;

    private final int expireSeconds;

    CacheType(String cacheName, int expireSeconds) {
        this.cacheName = cacheName;
        this.expireSeconds = expireSeconds;
    }

    @JsonValue
    public String getCacheName() {
        return cacheName;
    }

    @JsonCreator
    public static CacheType of(String cacheType) {
        return Arrays.stream(values()).filter(each -> each.getCacheName().equals(cacheType)).findFirst().orElse(null);
    }
    }
