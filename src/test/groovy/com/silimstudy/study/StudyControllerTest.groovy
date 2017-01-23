package com.silimstudy.study

import com.silimstudy.SilimstudyApiApplication
import com.silimstudy.test.SimpleParam
import com.silimstudy.test.SimpleRest
import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import static com.jayway.jsonassert.JsonAssert.with
import static org.hamcrest.Matchers.greaterThan

@SpringBootTest
class StudyControllerTest extends Specification {
    private final String JOIN_URL = "http://localhost:8080/user/join"
    private final String LOGIN_URL = "http://localhost:8080/user/login"
    private final String STUDY_URL = "http://localhost:8080/study"
    private final String STUDY_LIST_URL = "http://localhost:8080/study/all/list"

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

    void setup () {
        new SimpleRest()
                .post(JOIN_URL, new SimpleParam()
                    .add("username", "wym")
                    .add("password", "1234")
                    .add("email", "wym@silimstudy.net"))

        new SimpleRest()
                .post(JOIN_URL, new SimpleParam()
                    .add("username", "hhj")
                    .add("password", "2345")
                    .add("email", "hhj@silimstudy.net"))
    }

    void "로그인 안하고 스터디 등록 요청" () {
        given :
        def param = new LinkedMultiValueMap<String, String>()
        param.add("title", "객체지향 스터디")
        param.add("contents", "객체지향 스터디 입니다.")

        when :
        new SimpleRest()
                .post(STUDY_URL, new SimpleParam()
                    .add("title", "객체지향 스터디")
                    .add("contents", "객체지향 스터디 입니다."))

        then :
        def e = thrown(HttpClientErrorException.class)
        e.message == '401 null'
    }

    void "로그인 하고 스터디 등록 요청 - 본문내용 기입하지 않을때" () {
        given :
        def loginEntity = new SimpleRest()
                .post(LOGIN_URL, new SimpleParam()
                    .add("username", "wym")
                    .add("password", "1234"))

        when :
        def studyRegisterEntity = SimpleRest.sameSession(loginEntity)
                .post(STUDY_URL, new SimpleParam()
                    .add("title", "객체지향 스터디"))

        then :
        studyRegisterEntity.body == 'FAIL'

        when :
        studyRegisterEntity = SimpleRest.sameSession(loginEntity)
                .post(STUDY_URL, new SimpleParam()
                    .add("contents", "객체지향 스터디입니다. 화이팅해요 여러분~~"))

        then :
        studyRegisterEntity.body == 'FAIL'
    }

    void "로그인 하고 스터디 등록 요청 - 성공" () {
        given :
        def loginEntity = new SimpleRest()
                .post(LOGIN_URL, new SimpleParam()
                    .add("username", "wym")
                    .add("password", "1234"))

        when :
        def studyRegisterEntity = SimpleRest.sameSession(loginEntity)
                .post(STUDY_URL, new SimpleParam()
                    .add("title", "객체지향 스터디")
                    .add("contents", "객체지향 스터디입니다. 화이팅해요. 여러분~~"))

        then :
        studyRegisterEntity.body == 'SUCCESS'
    }

    void "로그인 안하고 스터디 리스트 요청" () {
        when :
        new SimpleRest().get(STUDY_LIST_URL)

        then :
        def e = thrown(HttpClientErrorException.class);
        e.message == '401 null'
    }

    void "로그인 하고 스터디 리스트 요청" () {
        given :
        def loginEntity = new SimpleRest()
                .post(LOGIN_URL, new SimpleParam()
                .add("username", "wym")
                .add("password", "1234"))

        SimpleRest.sameSession(loginEntity)
                .post(STUDY_URL, new SimpleParam()
                .add("title", "OOP study")
                .add("contents", "객체지향 스터디입니다. 화이팅해요. 여러분~~"))
        SimpleRest.sameSession(loginEntity)
                .post(STUDY_URL, new SimpleParam()
                .add("title", "AI study")
                .add("contents", "객체지향 스터디입니다. 화이팅해요. 여러분~~"))

        when :
        def entity = SimpleRest.sameSession(loginEntity).get(STUDY_LIST_URL)

        then :
        entity.statusCode == HttpStatus.OK
        with(entity.body)
                .assertThat('$.length()', greaterThan(0))
    }



}