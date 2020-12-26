package com.bj.lib_p_compiler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class Utils {

    public static String getPackageName(String className) {
        if (className == null || "".equals(className))
            return "";
        int lastIndex = className.lastIndexOf(".");
        return className.substring(0, lastIndex);
    }

    public static String arrayToString(String[] args) {
        if (args == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < args.length; i++) {
            sb.append("\"").append(args[i]).append("\"").append(i == args.length - 1 ? "" : ",");
        }
        sb.append("}");

        return sb.toString();
    }


}
