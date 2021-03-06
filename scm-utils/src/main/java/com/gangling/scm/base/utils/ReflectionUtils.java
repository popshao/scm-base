package com.gangling.scm.base.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射的Util函数集合.
 * <p/>
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性,转换字符串到对象等Util函数.
 *
 * @author calvin
 */
public class ReflectionUtils {

	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	private static final Map<Class, List<Field>> classFiledMap = new ConcurrentHashMap<Class, List<Field>>();

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * 可以取得父类的属性
	 *
	 * @param object    对象
	 * @param fieldName 属性名
	 * @return 属性值
	 */
	public static Object
	getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e);
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 * 可以设置父类的属性值
	 *
	 * @param object    对象
	 * @param fieldName 属性名
	 * @param value     属性值
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e);
		}
	}





	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 可以调用父类的方法
	 *
	 * @param object         对象
	 * @param methodName     方法名
	 * @param parameterTypes 方法类型
	 * @param parameters     方法参数
	 * @return 方法执行结果
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}
		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param object    对象
	 * @param fieldName 属性名
	 * @return 对象的Field
	 */
	public static Field getDeclaredField(final Object object, final String fieldName) {
		return getDeclaredField(object.getClass(), fieldName);
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param object 对象
	 * @return 对象的Field
	 */
	public static List<Field> getDeclaredFields(final Object object) {
		return getDeclaredFields(object.getClass());
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param clazz 对象类型
	 * @return 对象的Field
	 */
	public static List<Field> getDeclaredFields(final Class clazz) {
		List<Field> classFieldList = classFiledMap.get(clazz);
		if (classFieldList != null) {
			return classFieldList;
		}
		List<Field> fieldList = new ArrayList<Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					boolean contains = false;
					for (Field field1 : fieldList) {
						if (field1.getName().equals(field.getName())) {
							contains = true;
							break;
						}
					}
					if (!contains) {
						fieldList.add(field);
					}
				}
			}
		}
		classFiledMap.put(clazz, fieldList);
		return fieldList;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param clazz     对象类型
	 * @param fieldName 属性名
	 * @return 对象的Field
	 */
	public static Field getDeclaredField(final Class clazz, final String fieldName) {
		List<Field> fields = getDeclaredFields(clazz);
		if (CollectionUtils.isNotEmpty(fields)) {
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					return field;
				}
			}
		}
		return null;
	}

	/**
	 * 强行设置Field可访问
	 *
	 * @param field 属性
	 */
	public static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param object         对象
	 * @param methodName     方法名
	 * @param parameterTypes 方法参数类型
	 * @return 方法
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
		return getDeclaredMethod(object.getClass(), methodName, parameterTypes);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param clazz          对象
	 * @param methodName     方法名
	 * @param parameterTypes 方法参数类型
	 * @return 方法
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				if (superClass == null) {
					return null;
				}
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 * 如向上转型到Object仍无法找到, 返回null.
	 *
	 * @param clazz      对象
	 * @param methodName 方法名
	 * @return 方法
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Method[] methods = superClass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}

	public static List<Method> getDeclaredMethods(Class<?> clazz) {
		List<Method> methodList = new ArrayList<Method>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			if (superClass == null) {
				break;
			}
			Method[] methods = superClass.getDeclaredMethods();
			Collections.addAll(methodList, methods);
		}
		return methodList;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<Role>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 * determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * <p/>
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 * determined
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}




	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException
				|| e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException)
			return new IllegalArgumentException("Reflection Exception.", e);
		else if (e instanceof InvocationTargetException)
			return new RuntimeException("Reflection Exception.",
					((InvocationTargetException) e).getTargetException());
		else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

//	*
//	 * 判断一个属性是否在一个对象中
//	 * 包括以下情况
//	 * 1 此属性在该对象的父对象中
//	 * 2 如果此属性字符串中包含<p>.</p> 那么要按照ognl的规则判断
//	 *
//	 * @param clazz
//	 * @param propertyName
//	 * @return
//
//	public static boolean isProperty(Class<?> clazz, String propertyName) {
//		Assert.hasText(propertyName);
//		String[] ognls = propertyName.split(".");
//		if (ognls.length > 0) {
//			Object object = getFieldValue(clazz, ognls[0]);
//		}
//		Field field = getDeclaredField(clazz, propertyName);
//		if (field != null) {
//			return true;
//		}
//		return false;
//	}

	/**
	 * 取得集合内的泛型类
	 *
	 * @return
	 */
	public static Class getGenericCollectionType(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		if (pt.getActualTypeArguments().length > 0) {
			return (Class) pt.getActualTypeArguments()[0];
		}
		return null;
	}

	/**
	 * 取得map类的泛型类
	 *
	 * @return
	 */
	public static Class[] getGenericMapType(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		if (pt.getActualTypeArguments().length > 1) {
			Class[] classes = new Class[2];
			classes[0] = (Class) pt.getActualTypeArguments()[0];
			classes[1] = (Class) pt.getActualTypeArguments()[1];
			return classes;
		}
		return null;
	}

	/**
	 * 判断一个类是否是另一个类的子类
	 *
	 * @param clazz1 子类
	 * @param clazz2 另一个类
	 * @return 是否为子类
	 */
	public static boolean isSubClass(Class clazz1, Class clazz2) {
		Class parent = clazz1.getSuperclass();
		while (parent != null) {
			if (parent.getName().equals(clazz2.getName())) {
				return true;
			}
			parent = parent.getSuperclass();
		}
		return false;
	}

	/**
	 * 检查一个类是否实现了某个接口
	 *
	 * @param clazz1 实现类
	 * @param clazz2 接口
	 * @return true 实现了接口 false 未实现该接口
	 */
	public static boolean isInterfaceOf(Class clazz1, Class clazz2) {
		Class[] interfaces = clazz1.getInterfaces();
		if (interfaces == null || interfaces.length == 0) {
			return false;
		}
		for (Class anInterface : interfaces) {
			if (anInterface.getName().equals(clazz2.getName())) {
				return true;
			}
		}
		return false;
	}


	public static String buildMethodSignature(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
		StringBuilder builder = new StringBuilder();
		builder.append(clazz.getName());
		builder.append(".");
		builder.append(methodName);
		builder.append("(");
		for (int i = 0; i < parameterTypes.length; i++) {
			builder.append(i > 0 ? "," : "");
			builder.append(parameterTypes[i].getName());
		}
		builder.append(")");
		return builder.toString();
	}

	public static String buildMethodSignature(Method method) {
		return buildMethodSignature(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
	}
}
