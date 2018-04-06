package ru.jihor.rat.tests.unit

import ru.jihor.rat.utils.Version
import spock.lang.Specification
import spock.lang.Unroll

public class VersionTest extends Specification {
    @Unroll
    def "Version should build correctly for any remotely valid string"(String src, String rawVersion) {
        expect:
        Version.from(src).toString() == rawVersion
        where:
        src                       | rawVersion
        "1.0"                     | "1.0"
        "1.0.0"                   | "1.0.0"
        "1.0.10-SNAPSHOT"         | "1.0.10"
        "1.0.00"                  | "1.0.00"
        "1.whatever.0"            | "1.0"
        "1...0"                   | "1.0"
        "1.0.awt0n6.0sf0ghbq4gj5" | "1.0.06.0045"
    }

    @Unroll
    def "Version should compare correctly to other Versions"(Version v1, Version v2, Integer result) {
        expect:
        v1.compareTo(v2) == result
        where:
        v1                              | v2                              | result
        Version.from("1.0")             | Version.from("1.0")             | 0
        Version.from("1.0")             | Version.from("1.0.0")           | -1
        Version.from("1.0.0")           | Version.from("1.0")             | 1
        Version.from("1.0")             | Version.from("2.0.0")           | -1
        Version.from("2.0.0")           | Version.from("1.0")             | 1
        Version.from("1.0.0")           | Version.from("2.0")             | -1
        Version.from("2.0")             | Version.from("1.0.0")             | 1
        Version.from("1.0.10-SNAPSHOT") | Version.from("1.0.10")          | 0
        Version.from("1.0.10-SNAPSHOT") | Version.from("1.0.11-SNAPSHOT") | -1
        Version.from("1.0.11-SNAPSHOT") | Version.from("1.0.10-SNAPSHOT") | 1
    }


}
