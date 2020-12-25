package com.bj.lib_p_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;

public class BindingSet {
    private final TypeName targetTypeName;
    private final ClassName bindingClassName;
    private final TypeElement enclosingElement;
    private final boolean isFinal;
    private final boolean isView;
    private final boolean isActivity;
    private final boolean isDialog;

    public BindingSet(TypeName targetTypeName, ClassName bindingClassName,
                      TypeElement enclosingElement, boolean isFinal,
                      boolean isView, boolean isActivity, boolean isDialog) {
        this.targetTypeName = targetTypeName;
        this.bindingClassName = bindingClassName;
        this.enclosingElement = enclosingElement;
        this.isFinal = isFinal;
        this.isView = isView;
        this.isActivity = isActivity;
        this.isDialog = isDialog;
    }
}
