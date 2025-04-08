package com.quizhub.quizhub.config;

import com.quizhub.quizhub.strategy.DefaultScoringStrategy;
import com.quizhub.quizhub.strategy.ScoringStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for ScoringStrategy beans.
 * This class defines which ScoringStrategy implementation should be used as the default.
 */
@Configuration
public class ScoringStrategyConfig {

    /**
     * Defines the default ScoringStrategy bean to be used when autowiring ScoringStrategy.
     * The @Primary annotation ensures this bean is chosen when multiple ScoringStrategy
     * implementations exist.
     *
     * @return The default ScoringStrategy implementation
     */
    @Bean
    @Primary
    public ScoringStrategy defaultScoringStrategy() {
        return new DefaultScoringStrategy();
    }
}