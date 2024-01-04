package roy.love.roykook.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class AccessLimitService {

    RateLimiter rateLimiter = RateLimiter.create(2);


    /**
     *  尝试获得令牌
     * @return
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}
