package louis.general.util;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by Louis on 2018/6/20.
 */

/**
 * 需导入lang3库
 * compile 'org.apache.commons:commons-lang3:3.6'
 */

public class ReflectUtil {
    private static final HashMap<String, Field> fieldCache = new HashMap();
    private static final HashMap<String, Method> methodCache = new HashMap();

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        try {
            return ClassUtils.getClass(classLoader, className, false);
        } catch (ClassNotFoundException var3) {
            throw new XposedHelpers.ClassNotFoundError(var3);
        }
    }

    public static Object getStaticObjectField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).get((Object) null);
        } catch (IllegalAccessException var3) {
            throw new IllegalAccessError(var3.getMessage());
        } catch (IllegalArgumentException var4) {
            throw var4;
        }
    }

    public static void setBooleanField(Object obj, String fieldName, boolean value) {
        try {
            findField(obj.getClass(), fieldName).setBoolean(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public static Object callMethod(Object obj, String methodName, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, args).invoke(obj, args);
        } catch (IllegalAccessException var4) {
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new XposedHelpers.InvocationTargetError(var6.getCause());
        }
    }

    public static Object callMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, parameterTypes, args).invoke(obj, args);
        } catch (IllegalAccessException var5) {
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        } catch (InvocationTargetException var7) {
            throw new XposedHelpers.InvocationTargetError(var7.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        try {
            return findMethodBestMatch(clazz, methodName, args).invoke((Object) null, args);
        } catch (IllegalAccessException var4) {
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new XposedHelpers.InvocationTargetError(var6.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return findMethodBestMatch(clazz, methodName, parameterTypes, args).invoke((Object) null, args);
        } catch (IllegalAccessException var5) {
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        } catch (InvocationTargetException var7) {
            throw new XposedHelpers.InvocationTargetError(var7.getCause());
        }
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Object... args) {
        return findMethodBestMatch(clazz, methodName, getParameterTypes(args));
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(methodName);
        sb.append(getParametersString(parameterTypes));
        sb.append("#bestmatch");
        String fullMethodName = sb.toString();
        Method bestMatch;
        if (methodCache.containsKey(fullMethodName)) {
            bestMatch = (Method) methodCache.get(fullMethodName);
            if (bestMatch == null) {
                throw new NoSuchMethodError(fullMethodName);
            } else {
                return bestMatch;
            }
        } else {
            try {
                bestMatch = findMethodExact(clazz, methodName, parameterTypes);
                methodCache.put(fullMethodName, bestMatch);
                return bestMatch;
            } catch (NoSuchMethodError var12) {
                bestMatch = null;
                Class<?> clz = clazz;
                boolean considerPrivateMethods = true;

                do {
                    Method[] var11;
                    int var10 = (var11 = clz.getDeclaredMethods()).length;

                    for (int var9 = 0; var9 < var10; ++var9) {
                        Method method = var11[var9];
                        if ((considerPrivateMethods || !Modifier.isPrivate(method.getModifiers()))
                                && method.getName().equals(methodName)
                                && ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true)
                                && (bestMatch == null || MemberUtils.compareParameterTypes(method.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
                            bestMatch = method;
                        }
                    }

                    considerPrivateMethods = false;
                } while ((clz = clz.getSuperclass()) != null);

                if (bestMatch != null) {
                    bestMatch.setAccessible(true);
                    methodCache.put(fullMethodName, bestMatch);
                    return bestMatch;
                } else {
                    NoSuchMethodError e = new NoSuchMethodError(fullMethodName);
                    methodCache.put(fullMethodName, (Method) null);
                    throw e;
                }
            }
        }
    }

    private static String getParametersString(Class... clazzes) {
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        Class[] var6 = clazzes;
        int var5 = clazzes.length;

        for (int var4 = 0; var4 < var5; ++var4) {
            Class<?> clazz = var6[var4];
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            if (clazz != null) {
                sb.append(clazz.getCanonicalName());
            } else {
                sb.append("null");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    public static Class<?>[] getParameterTypes(Object... args) {
        Class[] clazzes = new Class[args.length];

        for (int i = 0; i < args.length; ++i) {
            clazzes[i] = args[i] != null ? args[i].getClass() : null;
        }

        return clazzes;
    }

    public static Method findMethodExact(Class<?> clazz, String methodName, Object... parameterTypes) {
        return findMethodExact(clazz, methodName, getParameterClasses(clazz.getClassLoader(), parameterTypes));
    }

    public static Method findMethodExact(String className, ClassLoader classLoader, String methodName, Object... parameterTypes) {
        return findMethodExact(findClass(className, classLoader), methodName, getParameterClasses(classLoader, parameterTypes));
    }

    public static Method findMethodExact(Class<?> clazz, String methodName, Class... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(methodName);
        sb.append(getParametersString(parameterTypes));
        sb.append("#exact");
        String fullMethodName = sb.toString();
        Method method;
        if (methodCache.containsKey(fullMethodName)) {
            method = (Method) methodCache.get(fullMethodName);
            if (method == null) {
                throw new NoSuchMethodError(fullMethodName);
            } else {
                return method;
            }
        } else {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                methodCache.put(fullMethodName, method);
                return method;
            } catch (NoSuchMethodException var6) {
                methodCache.put(fullMethodName, (Method) null);
                throw new NoSuchMethodError(fullMethodName);
            }
        }
    }

    /**
     * Look up a field in a class and set it to accessible. The result is cached.
     * If the field was not found, a {@link NoSuchFieldError} will be thrown.
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#');
        sb.append(fieldName);
        String fullFieldName = sb.toString();

        if (fieldCache.containsKey(fullFieldName)) {
            Field field = fieldCache.get(fullFieldName);
            if (field == null)
                throw new NoSuchFieldError(fullFieldName);
            return field;
        }

        try {
            Field field = findFieldRecursiveImpl(clazz, fieldName);
            field.setAccessible(true);
            fieldCache.put(fullFieldName, field);
            return field;
        } catch (NoSuchFieldException e) {
            fieldCache.put(fullFieldName, null);
            throw new NoSuchFieldError(fullFieldName);
        }
    }

    //###########################内部方法，请勿调用及修改#################
    private static Field findFieldRecursiveImpl(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class))
                    break;

                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ignored) {
                }
            }
            throw e;
        }
    }

    private static Class<?>[] getParameterClasses(ClassLoader classLoader, Object[] parameterTypesAndCallback) {
        Class[] parameterClasses = null;

        for (int i = parameterTypesAndCallback.length - 1; i >= 0; --i) {
            Object type = parameterTypesAndCallback[i];
            if (type == null) {
                throw new XposedHelpers.ClassNotFoundError("parameter type must not be null", (Throwable) null);
            }

            if (!(type instanceof XC_MethodHook)) {
                if (parameterClasses == null) {
                    parameterClasses = new Class[i + 1];
                }

                if (type instanceof Class) {
                    parameterClasses[i] = (Class) type;
                } else {
                    if (!(type instanceof String)) {
                        throw new XposedHelpers.ClassNotFoundError("parameter type must either be specified as Class or String", (Throwable) null);
                    }

                    parameterClasses[i] = findClass((String) type, classLoader);
                }
            }
        }

        if (parameterClasses == null) {
            parameterClasses = new Class[0];
        }

        return parameterClasses;
    }

    static abstract class MemberUtils {
        private static final int ACCESS_TEST = 7;
        private static final Class<?>[] ORDERED_PRIMITIVE_TYPES;

        MemberUtils() {
        }

        static {
            Class[] clsArr = new Class[ACCESS_TEST];
            clsArr[0] = Byte.TYPE;
            clsArr[1] = Short.TYPE;
            clsArr[2] = Character.TYPE;
            clsArr[3] = Integer.TYPE;
            clsArr[4] = Long.TYPE;
            clsArr[5] = Float.TYPE;
            clsArr[6] = Double.TYPE;
            ORDERED_PRIMITIVE_TYPES = clsArr;
        }

        static void setAccessibleWorkaround(AccessibleObject o) {
            if (o != null && !o.isAccessible()) {
                Member m = (Member) o;
                if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                    try {
                        o.setAccessible(true);
                    } catch (SecurityException e) {
                    }
                }
            }
        }

        static boolean isPackageAccess(int modifiers) {
            return (modifiers & ACCESS_TEST) == 0;
        }

        static boolean isAccessible(Member m) {
            return (m == null || !Modifier.isPublic(m.getModifiers()) || m.isSynthetic()) ? false : true;
        }

        public static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
            float leftCost = getTotalTransformationCost(actual, left);
            float rightCost = getTotalTransformationCost(actual, right);
            if (leftCost < rightCost) {
                return -1;
            }
            return rightCost < leftCost ? 1 : 0;
        }

        private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
            float totalCost = 0.0f;
            for (int i = 0; i < srcArgs.length; i++) {
                totalCost += getObjectTransformationCost(srcArgs[i], destArgs[i]);
            }
            return totalCost;
        }

        private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
            if (destClass.isPrimitive()) {
                return getPrimitivePromotionCost(srcClass, destClass);
            }
            float cost = 0.0f;
            Class destClass2 = null;
            while (destClass2 != null && !destClass2.equals(srcClass)) {
                if (destClass2.isInterface() && ClassUtils.isAssignable((Class) srcClass, destClass2)) {
                    cost += 0.25f;
                    break;
                }
                cost += 1.0f;
                destClass2 = destClass2.getSuperclass();
            }
            if (destClass2 == null) {
                return cost + 1.5f;
            }
            return cost;
        }

        private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
            float cost = 0.0f;
            Class<?> cls = srcClass;
            if (!cls.isPrimitive()) {
                cost = 0.0f + 0.1f;
                cls = ClassUtils.wrapperToPrimitive(cls);
            }
            int i = 0;
            while (cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length) {
                if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                    cost += 0.1f;
                    if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                        cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                    }
                }
                i++;
            }
            return cost;
        }
    }
}
