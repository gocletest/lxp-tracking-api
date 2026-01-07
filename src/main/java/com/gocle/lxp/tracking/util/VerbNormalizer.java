package com.gocle.lxp.tracking.util;

public class VerbNormalizer {

    public static String normalize(String verb) {
        if (verb == null) return null;

        String v = verb.toLowerCase();

        // xAPI full URI 대응
        if (v.contains("launched")) return "launched";
        if (v.contains("initialized")) return "launched";

        if (v.contains("started")) return "started";
        if (v.contains("experienced")) return "started";

        if (v.contains("completed")) return "completed";
        if (v.contains("passed")) return "completed";

        // fallback (확장 대비)
        return v;
    }

    private VerbNormalizer() {}
}
