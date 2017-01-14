package com.silimstudy.auth

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * Created by yeomyeongwoo on 2017. 1. 14..
 */
class UserControllerTests extends Specification {
    /*
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
    */

    void "HTTP GET 으로 회원 가입 요청 불가"() {
        when:
        def ex = null
        try {
            new RestTemplate()
                    .getForEntity("http://localhost:8080/user/join", String.class)
        } catch (Exception e){
            ex = e
        }

        then:
        ex.class == HttpClientErrorException.class
        ex.message.contains('405')
        System.out.println(ex.message)
    }

    void "ID, password 기입하지 않고 HTTP POST 로  회원 가입 요청 불가"() {
        when:
        def entity = new RestTemplate()
                    .postForEntity("http://localhost:8080/user/join", "", String.class)


        then:
        entity.statusCode == HttpStatus.OK
        entity.body == 'fail'
    }

    void "should reverse request!"() {
        when:
        ResponseEntity<String> entity = new RestTemplate().getForEntity(url, String.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body == reversedString

        where:
        url                                 || reversedString
        'http://localhost:8080/reverse/uno' || 'onu'
        'http://localhost:8080/reverse/ufc' || 'cfu'
    }
}
