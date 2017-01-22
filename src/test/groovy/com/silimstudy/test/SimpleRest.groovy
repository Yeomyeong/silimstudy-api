package com.silimstudy.test

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

import static com.silimstudy.test.TestUtils.log

class SimpleRest {
    private RestTemplate restTemplate
    private HttpHeaders headers


    SimpleRest() {
        this.restTemplate = new RestTemplate()
    }

    static SimpleRest sameSession(ResponseEntity entity) {
        def simpleRest = new SimpleRest()
        simpleRest.setEntity(entity)
        return simpleRest

    }

    def get(String url) {
        if (headers == null)
            return restTemplate.getForEntity(url, String.class)

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(headers)
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class)

    }

    def get(String url, SimpleParam param) {
        return get(url +"?" + param.toStringValue())
    }

    def post(String url, SimpleParam param) {
        if (headers == null)
            return restTemplate.postForEntity(url, param.toMultiValueMap(), String.class)

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(param.toMultiValueMap(), headers)
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class)

    }


    private void setEntity(ResponseEntity entity) {
        def newCookies = entity.getHeaders().get("Set-Cookie")
        if (newCookies == null)
            return
        log('Set-Cookie)' + newCookies)
        def cookies = newCookies.join(';')
        HttpHeaders headers = new HttpHeaders()
        headers.set("Cookie", cookies)
        this.headers = headers
    }
}
