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
        for (String s : versionAsString.split("\\.")) {
            versionNumbers.put(index++, Long.valueOf(s));
        }
        this.rawVersion = versionAsString;
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

            // 'other' version is larger if 'this' version is shorter and previous numbers were equal
            if (!vn1.containsKey(i)) {
                return -1;
            }

            // 'this' version is larger if 'other' version is shorter and previous numbers were equal
            if (!vn2.containsKey(i)) {
                return 1;
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
