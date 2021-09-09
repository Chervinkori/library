package com.chweb.library.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chervinko <br>
 * 18.08.2021
 */
@Configuration
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class Config {

}
