package com.silimstudy.auth

import com.jayway.jsonpath.JsonPath
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
import static com.jayway.jsonassert.JsonAssert.*
import static org.hamcrest.Matchers.*

/**
 * Created by yeojung on 17. 1. 15.
 */
class UserControllerLoginTest extends Specification {
    private final String JOIN_URI = "http://localhost:8080/user/join"
    private final String LOGIN_URI = "http://localhost:8080/user/login"
    private String TEST_ID = "aaaa"
    private String TEST_PASSWORD = "1234"
    private String TEST_EMAIL = "aaaa@silimstudy.net"

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

    void setup() {
        //회원 가입하기
        def joinParam = new LinkedMultiValueMap<String, String>()
        joinParam.add("username", TEST_ID)
        joinParam.add("password", TEST_PASSWORD)
        joinParam.add("email", TEST_EMAIL)
        new RestTemplate()
                .postForEntity(JOIN_URI, joinParam, String.class)

    }

    void "로그인 하기"() {
        given:
        def loginParam = new LinkedMultiValueMap<String, String>()
        loginParam.add("username", TEST_ID)
        loginParam.add("password", TEST_PASSWORD)

        when:
        def entity = new RestTemplate()
                    .postForEntity(LOGIN_URI, loginParam, String.class)

        then:
        log(entity.body)
        entity.statusCode == HttpStatus.OK
        with(entity.body)
                .assertThat('$.response', is('SUCCESS'))
                .assertThat('$.username', is(TEST_ID))
                .assertThat('$.authorities[0].authority', is('USER'))
                .assertThat('$.token', not(isEmptyString()))
    }

    void "존재하지 않는 username 으로 로그인 요청"() {
        given:
        def loginParam = new LinkedMultiValueMap<String, String>()
        loginParam.add("username", "aaa123123")
        loginParam.add("password", TEST_PASSWORD)

        when:
        def entity = new RestTemplate()
                .postForEntity(LOGIN_URI, loginParam, String.class)

        then:
        log(entity.body)
        entity.statusCode == HttpStatus.OK
        with(entity.body)
                .assertThat('$.response', is('NOT_MATCHED'))
                .assertThat('$.username', isEmptyOrNullString())
                .assertThat('$.authorities', isEmptyOrNullString())
                .assertThat('$.token', isEmptyOrNullString())
    }

    void "틀린 패스워드로 로그인 요청"() {
        given:
        def loginParam = new LinkedMultiValueMap<String, String>()
        loginParam.add("username", TEST_ID)
        loginParam.add("password", '123123123')

        when:
        def entity = new RestTemplate()
                .postForEntity(LOGIN_URI, loginParam, String.class)

        then:
        log(entity.body)
        entity.statusCode == HttpStatus.OK
        with(entity.body)
                .assertThat('$.response', is('NOT_MATCHED'))
                .assertThat('$.username', isEmptyOrNullString())
                .assertThat('$.authorities', isEmptyOrNullString())
                .assertThat('$.token', isEmptyOrNullString())
    }
}