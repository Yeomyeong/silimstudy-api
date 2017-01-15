package com.silimstudy.auth

import com.silimstudy.SilimstudyApiApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import static com.TestUtils.log


/**
 * Created by yeojung on 17. 1. 15.
 */
class UserControllerLoginTest extends Specification {
    private final String JOIN_URI = "http://localhost:8080/user/join"
    private final String LOGIN_URI = "http://localhost:8080/user/login"

    @Shared
    @AutoCleanup
    ConfigurableApplicationContext context

    void setupSpec() {
        Future future = Executors
                .newSingleThreadExecutor().submit(
                new Callable() {
                    @Override
                    ConfigurableApplicationContext call() throws Exception {
                        return (ConfigurableApplicationContext) SpringApplication
                                .run(SilimstudyApiApplication.class)
                    }
                })
        context = (ConfigurableApplicationContext) future.get(60, TimeUnit.SECONDS)
    }

    void "회원 가입 후 로그인 하기"() {
        given:
        def joinParam = new LinkedMultiValueMap<String, String>()
        joinParam.add("username", "aaaa")
        joinParam.add("password", "aaaa")
        joinParam.add("email", "aaaa@silimstudy.net")
        new RestTemplate()
                .postForEntity(JOIN_URI, joinParam, String.class)

        when:
        def loginParam = new LinkedMultiValueMap<String, String>()
        loginParam.add("username", "aaaa")
        loginParam.add("password", "aaaa")
        def entity = new RestTemplate()
                    .postForEntity(LOGIN_URI, loginParam, String.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body == ''
        log(entity.body)
    }
}