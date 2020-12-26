package com.bj.lib_p_compiler;

import com.bj.lib_permission_annotation.Defined;
import com.bj.lib_permission_annotation.Granted;
import com.bj.lib_permission_annotation.Permission;
import com.bj.lib_permission_annotation.Rationale;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
//import com.google.auto.service.AutoService;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
//import javax.annotation.processing.Processor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class PermissionProcessor extends AbstractProcessor {
    /*key（类名）：com.bj.HelloWorld*/
    public static final Map<String, PermissionData> permissionData = new LinkedHashMap<>(16);

    private Filer filer;
    private ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        this.processingEnvironment = processingEnvironment;
    }

    private Messager mMsg;

    private void printMessage(String message) {
        if (mMsg == null) {
            mMsg = processingEnv.getMessager();
        }
        mMsg.printMessage(Diagnostic.Kind.NOTE, message);
    }

    public boolean isType(TypeMirror mirror, Class<?> cls) {
        Elements elementUtils = processingEnvironment.getElementUtils();
        TypeMirror type = elementUtils.getTypeElement(cls.getCanonicalName()).asType();
        return type.equals(mirror);
    }

    public boolean isType(TypeMirror mirror, String string) {

//        return mirror.getKind().name().equals(string);
        return mirror.toString().equals(string);
    }


    private Set<? extends Element> pElements;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        printMessage("set: " + set);
//        /*获取所有被Permission标注的类，并判断是否是Activity*/
        pElements = roundEnvironment.getElementsAnnotatedWith(Permission.class);
        getAllAnnotationMethods(roundEnvironment, Granted.class, 1);
        getAllAnnotationMethods(roundEnvironment, Defined.class, 2);
        getAllAnnotationMethods(roundEnvironment, Rationale.class, 3);

        List<String> defined = new ArrayList<>();
        List<String> granted = new ArrayList<>();
        List<String> notShow = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        printMessage("pElements" + pElements);
        for (Element element : pElements) {
            String packageName = Utils.getPackageName(element.toString());
            PermissionData permissionData = PermissionProcessor.permissionData.get(element.toString());
            printMessage("permissionData " + permissionData);
            if (permissionData == null) {
                continue;
            }
            Permission annotation = element.getAnnotation(Permission.class);
            if (annotation != null) {
                permissionData.setPermissions(annotation.permission());
                permissionData.setRequestCode(annotation.reqCode());
            }

            //Defined
            for (Element definedE : permissionData.getDefined()) {
                ExecutableElement parameterizable = (ExecutableElement) definedE;
                String methodName = parameterizable.getSimpleName().toString();
                List<? extends VariableElement> parameters = parameterizable.getParameters();
                if (parameters == null || parameters.size() != 2) {
                    continue;
                }
                if (isType(parameters.get(0).asType(), int.class.getCanonicalName())
                        && isType(parameters.get(1).asType(), String[].class.getCanonicalName())) {
                    defined.add(methodName);
                }
            }
            //Granted
            for (Element grantedE : permissionData.getGranted()) {
                ExecutableElement parameterizable = (ExecutableElement) grantedE;
                String methodName = parameterizable.getSimpleName().toString();
                List<? extends VariableElement> parameters = parameterizable.getParameters();
                if (parameters == null || parameters.size() != 1) {
                    continue;
                }
                if (isType(parameters.get(0).asType(), int.class.getCanonicalName())) {
                    granted.add(methodName);
                }
            }
            //Rationale
            for (Element rationaleE : permissionData.getRationale()) {
                ExecutableElement parameterizable = (ExecutableElement) rationaleE;
                String methodName = parameterizable.getSimpleName().toString();
                List<? extends VariableElement> parameters = parameterizable.getParameters();
                if (parameters == null || parameters.size() != 2) {
                    continue;
                }
                if (isType(parameters.get(0).asType(), int.class.getCanonicalName())
                        && isType(parameters.get(1).asType(), String[].class.getCanonicalName())) {
                    notShow.add(methodName);
                }
            }
            try {
                writeFile(element.getSimpleName().toString(),
                        packageName,
                        granted,
                        defined,
                        notShow
                );
                granted.clear();
                defined.clear();
                notShow.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private void getAllAnnotationMethods(RoundEnvironment roundEnvironment, Class<? extends Annotation> annotation, int flag) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
        for (Element element : elements) {
            String pk;
            String cl;
            pk = Utils.getPackageName(element.getEnclosingElement().toString());
            cl = element.getEnclosingElement().getSimpleName().toString();
            String parent = element.getEnclosingElement().getEnclosingElement().toString();

            PermissionData data = permissionData.get(element.getEnclosingElement().toString());

            if (data == null) {
                data = new PermissionData();
                data.setClassName(cl);
                data.setPackageName(pk);
                data.setParent(parent);
            }

            switch (flag) {
                case 1: {//Granted
                    data.addGranted(element);
                    break;
                }
                case 2: {//Defined
                    data.addDefined(element);
                    break;
                }
                case 3: {//Rationale
                    data.addRationale(element);
                    break;
                }
                default:
                    break;
            }
            permissionData.put(element.getEnclosingElement().toString(), data);
        }
    }

    private void writeFile(String className, String packageName,
                           List<String> granted,
                           List<String> defined,
                           List<String> notShow) throws IOException {

        printMessage("packageName + className: " + packageName + className);

        PermissionData permissionData = PermissionProcessor.permissionData.get(packageName + "." + className);

        String createClassName = className + "_PermissionBinding";
        printMessage("createClassName " + createClassName);

        ClassName activity = C.createClass(packageName, className);
        List<MethodSpec> methodSpecs = new ArrayList<>();
        //添加Activity成员变量
        FieldSpec activityName = FieldSpec.builder(activity, "activity")
                .addModifiers(Modifier.PUBLIC)
                .build();

        FieldSpec.Builder permissionsStatementBuilder = FieldSpec.builder(String[].class, "permissions")
                .addModifiers(Modifier.PUBLIC);
        if (permissionData != null && permissionData.getPermissions() != null) {
            permissionsStatementBuilder.initializer(Utils.arrayToString(permissionData.getPermissions()));
        }
        FieldSpec permissionsStatement = permissionsStatementBuilder.build();

        FieldSpec.Builder requestCodeStatementBuilder = FieldSpec.builder(int.class, "requestCode")
                .addModifiers(Modifier.PUBLIC);
        if (permissionData != null) {
            requestCodeStatementBuilder.initializer(String.valueOf(permissionData.getRequestCode()));
        }
        FieldSpec requestCodeStatement = requestCodeStatementBuilder.build();

        //添加构造方法
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addParameter(activity, "activity")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.activity=activity")
                .build();
        //申请权限的方法
        MethodSpec requestPermission = MethodSpec.methodBuilder("requestPermission")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(C.Context, "mContext")
                .addParameter(String[].class, "mPermission")
                .beginControlFlow("for (String permission : mPermission)")
                .beginControlFlow("if($T.checkSelfPermission(mContext,permission) != $T.PERMISSION_GRANTED)", C.ContextCompat, C.PackageManager)
                .endControlFlow()
                .endControlFlow()
                .build();
        //权限通过
        MethodSpec.Builder onPermissionGrantedBuilder = MethodSpec.methodBuilder("onPermissionGranted")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(int.class, "requestCode");
        for (String s : granted) {
            onPermissionGrantedBuilder.addStatement("activity." + s + "(requestCode)");
        }
        MethodSpec onPermissionGranted = onPermissionGrantedBuilder.build();
        //权限拒绝
        MethodSpec.Builder onPermissionDeniedBuilder = MethodSpec.methodBuilder("onPermissionDenied")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(int.class, "requestCode")
                .addParameter(String[].class, "permission");
        for (String s : defined) {
            onPermissionDeniedBuilder.addStatement("activity." + s + "(requestCode,permission)");
        }
        MethodSpec onPermissionDenied = onPermissionDeniedBuilder.build();
        //用户点击了不再显示申请弹窗
        MethodSpec.Builder onPermissionDeniedAndNotHintBuilder = MethodSpec.methodBuilder("onPermissionDeniedAndNotHint")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(int.class, "requestCode")
                .addParameter(String[].class, "permission");
        for (String s : notShow) {
            onPermissionDeniedAndNotHintBuilder.addStatement("activity." + s + "(requestCode,permission)");
        }
        MethodSpec onPermissionDeniedAndNotHint = onPermissionDeniedAndNotHintBuilder.build();
        //ADD
        methodSpecs.add(requestPermission);
        methodSpecs.add(onPermissionGranted);
        methodSpecs.add(onPermissionDenied);
        methodSpecs.add(onPermissionDeniedAndNotHint);
        //生成代码文档
        TypeSpec helloWorld = TypeSpec.classBuilder(createClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(C.IPermissionResultListener)
                .addField(activityName)
                .addField(permissionsStatement)
                .addField(requestCodeStatement)
                .addMethod(constructorMethod)
                .addMethods(methodSpecs)
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, helloWorld)
                .addFileComment("Generated code from Tai. Do not modify!")
                .build();
        javaFile.writeTo(filer);
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(Defined.class);
        annotations.add(Granted.class);
        annotations.add(Rationale.class);
        annotations.add(Permission.class);
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
