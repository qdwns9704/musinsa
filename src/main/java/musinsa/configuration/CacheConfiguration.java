package musinsa.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import musinsa.struct.common.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {
    @Bean
    public CacheManager cacheManager() {
        /*
         과제상에서는 별도로 서버 구성 환경에 대해서 언급이 없었으므로, 단일 노드 환경을 가정하고 로컬 캐시를 활용하도록 구성하였습니다.
         다만 서버 구성이 멀티노드라고 가정했을 때 글로벌 캐시(ex: Redis, Memcached)를 사용하도록 변경하면 됩니다.
         Caffeine은 초당 데이터 처리량 측면에서 매우 우수한 지표를 보여주고, 다른 기능이 딱히 필요가 없고,
         단지 Read/Write 시 최고의 성능을 낼 수 있다고 판단하여 사용하였습니다.
         */
        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
            .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder()
                    .recordStats()
                    .expireAfterWrite(cache.getExpireSeconds(), TimeUnit.SECONDS)
                    .maximumSize(10000)
                    .build()
                )
            )
            .toList();

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);

        return cacheManager;
    }
}
