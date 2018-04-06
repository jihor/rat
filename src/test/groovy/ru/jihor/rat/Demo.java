package ru.jihor.rat;

import ru.jihor.rat.annotations.Remove;
import ru.jihor.rat.annotations.RemoveAfterDate;
import ru.jihor.rat.annotations.RemoveInVersion;

/**
 * @author jihor (jihor@ya.ru)
 * Created on 2018-04-06
 */
@Remove(reason = "bad code")
public class Demo {

    @RemoveInVersion(version = "3.0", reason = "ehh")
    public Demo() {
    }

    @RemoveAfterDate(date = "2015-12-31", reason = "too late")
    public static final String value = "nice";

    @RemoveInVersion(version = "2.0", reason = "too old")
    public void test1() {
    }

    public void test2(@RemoveInVersion(version = "3.0", reason = "too cold") String wow) {
    }

}
