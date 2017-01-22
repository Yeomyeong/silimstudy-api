package com.silimstudy.test

import org.springframework.util.LinkedMultiValueMap

class SimpleParam {
    def map = new LinkedMultiValueMap<String, String> ()

    def add(String key, String value) {
        map.add(key, value)
        return this
    }

    def toMultiValueMap() {
        return map
    }

    def toStringValue() {
        return map.toSingleValueMap().toMapString()
    }
}
