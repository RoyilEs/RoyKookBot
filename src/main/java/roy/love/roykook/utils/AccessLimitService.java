package roy.love.roykook.utils;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class AccessLimitService {

    RateLimiter rateLimiter = RateLimiter.create(2, 1, TimeUnit.MILLISECONDS);


    /**
     *  尝试获得令牌
     * @return
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}
