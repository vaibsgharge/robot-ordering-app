package de.tech26.robot.factory.data;

import java.util.Arrays;
import java.util.List;

public enum StockMap {

    A("FACE"),
    B("FACE"),
    C("FACE"),
    D("ARM"),
    E("ARM"),
    F("MOB"),
    G("MOB"),
    H("MOB"),
    I("MAT"),
    J("MAT"),
    FACE("A,B,C"),
    ARM("D,E"),
    MOB("F,G,H"),
    MAT("I,J");

    private final String codes;

    StockMap(String codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return Arrays.asList(this.codes.split(","));
    }
}
