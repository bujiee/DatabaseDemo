package com.bj.lib_p_compiler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;


public class PermissionData {
    private String className;
    private String packageName;
    private String parent;

    private String[] permissions;
    private int requestCode=-1;

    private Set<Element> defined = new HashSet<>();
    private Set<Element> granted = new HashSet<>();
    private Set<Element> permission = new HashSet<>();
    private Set<Element> rationale = new HashSet<>();

    public void addDefined(Element element) {
        defined.add(element);
    }

    public void addGranted(Element element) {
        granted.add(element);
    }

    public void addRationale(Element element) {
        rationale.add(element);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Set<Element> getDefined() {
        return defined;
    }

    public Set<Element> getGranted() {
        return granted;
    }

    public Set<Element> getPermission() {
        return permission;
    }

    public Set<Element> getRationale() {
        return rationale;
    }

    public String getFullName() {
        return packageName + className;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setDefined(Set<Element> defined) {
        this.defined = defined;
    }

    public void setGranted(Set<Element> granted) {
        this.granted = granted;
    }

    public void setPermission(Set<Element> permission) {
        this.permission = permission;
    }

    public void setRationale(Set<Element> rationale) {
        this.rationale = rationale;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public String toString() {
        return "PermissionData{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", parent='" + parent + '\'' +
                ", permissions=" + permissions +
                ", requestCode=" + requestCode +
                ", defined=" + defined +
                ", granted=" + granted +
                ", permission=" + permission +
                ", rationale=" + rationale +
                '}';
    }
}
