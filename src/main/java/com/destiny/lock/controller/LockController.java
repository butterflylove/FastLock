package com.destiny.lock.controller;

import com.destiny.lock.api.BaseResponse;
import com.destiny.lock.api.LockRequest;
import com.destiny.lock.api.LockTouchRequest;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

/**
 * Created by zhangtianlong on 18/7/28.
 */
@RestController
public class LockController {
    private static final Logger logger = LoggerFactory.getLogger(LockController.class);

    /**
     * 加锁
     */
    @RequestMapping(value = "/lock", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse test(@RequestBody LockRequest lockRequest) {
        return new BaseResponse();
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "/unlock/{applicant}/{lockCode}/{requestId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse unlock(@PathVariable("applicant") String applicant, @PathVariable("requestId") String requestId,
                               @PathVariable("lockCode") @NotEmpty @NotNull String lockCode) {
        return new BaseResponse();
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "/unlock", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse unlock() {
        return new BaseResponse();
    }

    /**
     * touch, 延长锁过期时间
     */
    @RequestMapping(value = "/touch", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse touch(@RequestBody LockTouchRequest touchRequest) {
        return new BaseResponse();
    }
}
