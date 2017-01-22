package com.silimstudy.auth

import com.silimstudy.SilimstudyApiApplication
import com.silimstudy.test.SimpleParam
import com.silimstudy.test.SimpleRest
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
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

import static com.silimstudy.test.TestUtils.log
import static com.jayway.jsonassert.JsonAssert.with
import static org.hamcrest.Matchers.*

class UserControllerLoginTest extends Specification {
    private final String JOIN_URI = "http://localhost:8080/user/join"
    private final String LOGIN_URI = "http://localhost:8080/user/login"
    private final String LOGOUT_URI = "http://localhost:8080/user/logout"
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
        new SimpleRest()
                .post(JOIN_URI, new SimpleParam()
                    .add("username", TEST_ID)
                    .add("password", TEST_PASSWORD)
                    .add("email", TEST_EMAIL))

    }

    void "로그인 하기"() {
        when:
        def entity = new SimpleRest()
                .post(LOGIN_URI, new SimpleParam()
                    .add("username", TEST_ID)
                    .add("password", TEST_PASSWORD))

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
        when:
        def entity = new SimpleRest()
                .post(LOGIN_URI, new SimpleParam()
                    .add("username", "aaa123123")
                    .add("password", TEST_PASSWORD))

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
        when:
        def entity = new SimpleRest()
                .post(LOGIN_URI, new SimpleParam()
                .add("username", TEST_ID)
                .add("password", 'nnnn'))

        then:
        log(entity.body)
        entity.statusCode == HttpStatus.OK
        with(entity.body)
                .assertThat('$.response', is('NOT_MATCHED'))
                .assertThat('$.username', isEmptyOrNullString())
                .assertThat('$.authorities', isEmptyOrNullString())
                .assertThat('$.token', isEmptyOrNullString())
    }

    void "로그인 하고 나서 로그아웃 하기"() {
        given: "로그인 하기"
        def loginEntity = new SimpleRest()
                .post(LOGIN_URI, new SimpleParam()
                    .add("username", TEST_ID)
                    .add("password", TEST_PASSWORD))
        def loginRest = SimpleRest.sameSession(loginEntity)

        when: "같은 세션의 로그아웃 요청"
        def logoutEntity = loginRest.get(LOGOUT_URI)

        then:
        logoutEntity.statusCode == HttpStatus.OK

        when : "같은 세션의 로그아웃 요청 한번 더"
        loginRest.get(LOGOUT_URI)

        then :
        def e = thrown(HttpClientErrorException.class)
        e.message == '401 null'

    }
}