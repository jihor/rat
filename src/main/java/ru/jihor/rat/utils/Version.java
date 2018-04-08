package ru.jihor.rat.utils;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;

public class Version implements Comparable<Version> {

    private final TreeMap<Integer, Long> versionNumbers = new TreeMap<>();
    private final String rawVersion;

    @Override
    public String toString() {
        return rawVersion;
    }

    /**
     * Special case for when a version is unknown
     */
    public static final Version UNDEFINED = new Version() {

        /**
         * Unknown version is always less than any known version
         */
        @Override
        public int compareTo(Version other) {
            Objects.requireNonNull(other);
            return other == this ? 0 : -1;
        }

    };

    private Version() {
        this.rawVersion = "UNDEFINED";
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionNumbers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return Objects.equals(versionNumbers, version.versionNumbers);
    }

    private Version(String versionAsString) {
        int index = 0;
        StringJoiner stringJoiner = new StringJoiner(".");
        for (String s : versionAsString.split("\\.")) {
            Long value = Long.valueOf(s);
            versionNumbers.put(index++, value);
            stringJoiner.add(value.toString());
        }
        this.rawVersion = stringJoiner.toString();
    }

    public static Version from(String versionAsString) {

        if (versionAsString == null) {
            return Version.UNDEFINED;
        }

        String versionCleaned = versionAsString.replaceAll("[^\\d|.]", "").replaceAll("\\.+", ".");

        if (versionCleaned.isEmpty() || versionCleaned.replaceAll("\\.", "").isEmpty()) {
            return Version.UNDEFINED;
        }

        return new Version(versionCleaned);

    }

    @Override
    public int compareTo(Version other) {
        if (other == UNDEFINED) {
            return 1;
        }

        if (Objects.equals(this, other)) {
            return 0;
        }

        TreeMap<Integer, Long> vn1 = this.versionNumbers;
        TreeMap<Integer, Long> vn2 = other.versionNumbers;

        Set<Integer> iterationBase = vn1.size() >= vn2.size() ? vn1.keySet() : vn2.keySet();

        for (Integer i : iterationBase) {

            // if 'this' version is shorter and previous numbers were equal
            if (!vn1.containsKey(i)) {
                // if longer version contains simply more zeroes, it's not considered newer
                if (Long.compare(0L, vn2.get(i)) == 0){
                    continue;
                } else {
                // but if there are non-zeroes, longer version is considered newer
                    return -1;
                }
            }

            // same check if 'other' version is longer
            if (!vn2.containsKey(i)) {
                if (Long.compare(0L, vn1.get(i)) == 0){
                    continue;
                } else {
                    return 1;
                }
            }

            int compare = Long.compare(vn1.get(i), vn2.get(i));
            if (compare != 0) {
                return compare;
            }
        }

        return 0;
    }

    public boolean isGreaterOrEqual(Version other) {
        return this.compareTo(other) >= 0;
    }
}
