package com.gangling.scm.base.middleware.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RedissonConfig {
    private static final String REDIS_PROXY = "redis://";
    @Value("${spring.redis.sentinel.nodes}")
    private String sentinelAddress;
    @Value("${spring.redis.sentinel.master}")
    private String masterName;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        List<String> sentinelSslAddressWithList = Arrays.stream(sentinelAddress.split(","))
                .map(s -> REDIS_PROXY + s)
                .collect(Collectors.toList());

        Config config = Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream());
        config.useSentinelServers()
                .setPassword(password)
                .setMasterName(masterName)
                .setReadMode(ReadMode.MASTER)
                .setCheckSentinelsList(false)
                // use "rediss://" for SSL connection
                .addSentinelAddress(sentinelSslAddressWithList.toArray(new String[sentinelAddress.split(",").length]));
        return Redisson.create(config);
    }
}
