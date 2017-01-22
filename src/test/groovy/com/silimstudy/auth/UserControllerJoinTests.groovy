package com.silimstudy.auth

import com.silimstudy.SilimstudyApiApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootTest
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

import static com.silimstudy.test.TestUtils.log

@SpringBootTest
class UserControllerJoinTests extends Specification {
    private final String URI = "http://localhost:8080/user/join"

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

    void "HTTP GET 으로 회원 가입 요청 불가"() {
        when :
        def param = "username=aaa&password=aaaa&email=aaa@naver.net"
        new RestTemplate().getForEntity(URI + "?" + param, String.class)

        then:
        def e = thrown(HttpClientErrorException.class)
        e.message == '401 null'
    }

    void "아무것도 기입하지 않고 HTTP POST 로  회원 가입 요청 불가"() {
        when:
        def entity = new RestTemplate()
                    .postForEntity(URI, "", String.class)


        then:
        entity.statusCode == HttpStatus.OK
        entity.body == '"NOT_VALID"'
        log(entity.body)
    }

    void "username 만 기입하고 HTTP POST 로 회원 가입 요청 불가"() {
        given :
        def param = new LinkedMultiValueMap<String, String>()
        param.add("username", "aaaa")

        when:
        def entity = new RestTemplate()
                .postForEntity(URI, param, String.class)


        then:
        entity.statusCode == HttpStatus.OK
        entity.body == '"NOT_VALID"'
        log(entity.body)
    }

    void "전체 기입하고 HTTP POST 로 회원 가입 요청"() {
        given:
        def param = new LinkedMultiValueMap<String, String>()
        param.add("username", "aaaa")
        param.add("password", "aaaa")
        param.add("email", "aaaa@silimstudy.net")

        when:
        def entity = new RestTemplate()
                .postForEntity(URI, param, String.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body == '"SUCCESS"'
        log(entity.body)
    }

    void "동일한 username 으로 회원 가압 요청 불가"() {
        given:
        def param1 = new LinkedMultiValueMap<String, String>()
        param1.add("username", "bbbb")
        param1.add("password", "bbbb")
        param1.add("email", "bbbb@silimstudy.net")

        def param2 = new LinkedMultiValueMap<String, String>()
        param2.add("username", "bbbb")
        param2.add("password", "bbb")
        param2.add("email", "bbb@silimstudy.net")

        when:
        def entity1 = new RestTemplate()
                .postForEntity(URI, param1, String.class)
        def entity2 = new RestTemplate()
                .postForEntity(URI, param2, String.class)



        then:
        entity1.statusCode == HttpStatus.OK
        entity1.body == '"SUCCESS"'

        entity2.statusCode == HttpStatus.OK
        entity2.body == '"ALREADY_EXIST"'
        log(entity2.body)
    }
}
