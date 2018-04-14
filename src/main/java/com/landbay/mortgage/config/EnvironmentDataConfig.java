package com.landbay.mortgage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lekanomotayo on 12/04/2018.
 */

@Configuration
public class EnvironmentDataConfig {

    @Value("${mortgage.liborRate}")
    private double liborRate;
    @Value("${mortgage.lenderFixedRate}")
    private double lenderFixedRate;
    @Value("${mortgage.lenderTrackerRate}")
    private double lenderTrackerRate;
    @Value("${mortgage.platformFeeRate}")
    private double platformFeeRate;

    public double getLiborRate() {
        return liborRate;
    }

    public void setLiborRate(double liborRate) {
        this.liborRate = liborRate;
    }

    public double getLenderFixedRate() {
        return lenderFixedRate;
    }

    public void setLenderFixedRate(double lenderFixedRate) {
        this.lenderFixedRate = lenderFixedRate;
    }

    public double getLenderTrackerRate() {
        return lenderTrackerRate;
    }

    public void setLenderTrackerRate(double lenderTrackerRate) {
        this.lenderTrackerRate = lenderTrackerRate;
    }

    public double getPlatformFeeRate() {
        return platformFeeRate;
    }

    public void setPlatformFeeRate(double platformFeeRate) {
        this.platformFeeRate = platformFeeRate;
    }
}
