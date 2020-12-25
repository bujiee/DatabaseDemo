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


}
