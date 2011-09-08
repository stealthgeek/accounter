package com.vimukti.accounter.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.SpecialReference;

public class ClientConvertUtil extends ObjectConvertUtil {

	private <T, R> R getClientAfterCheckingInCache(T obj, Class<R> destType)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, AccounterException {
		Map<Object, Object> localCache = getCache();

		if (obj == null) {
			return null;
		}
		R ret = (R) localCache.get(obj);
		if (ret == null) {
			ret = toClientObjectInternal(obj, destType);
			localCache.put(obj, ret);
		}
		return ret;
	}

	private <T, R> R getClientwithidAfterCheckingInCache(T obj, R destType) {
		Map<Object, Object> localCache = getCache();

		if (obj == null) {
			return null;
		}
		R ret = (R) localCache.get(obj);
		if (ret == null) {
			ret = toClientWithidInternal(obj, destType);
			localCache.put(obj, ret);
		}
		return ret;
	}

	/**
	 * Convert server Object to client Object. get all values from server Object
	 * ,set them in client object.
	 * 
	 * @param <S>
	 *            IAccounterServerCore
	 * @param <D>
	 *            IAccounterCore
	 * @param src
	 *            Server Object
	 * @param dstType
	 *            Client Object
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws AccounterException
	 * @throws IllegalArgumentException
	 */

	private <S, D> D toClientObjectInternal(S src, Class<D> dstType)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, AccounterException {
		if (src == null)
			return null;

		Class<? extends Object> srcType = src.getClass();

		if (dstType == null)
			dstType = (Class<D>) getClientEqualentClass(srcType);

		D dst = dstType.newInstance();
		getCache().put(src, dst);

		Map<String, Field> srcMap = getAllFields(srcType);
		Map<String, Field> dstMap = getAllFields(dstType);
		for (String dstFieldName : dstMap.keySet()) {
			Field dstField = dstMap.get(dstFieldName);
			Field srcField = srcMap.get(dstFieldName);

			if (srcField == null)
				continue;
			if ((srcField.getModifiers() & Modifier.STATIC) > 0)
				continue;
			if (srcField.get(src) == null)
				continue;

			srcField.setAccessible(true);
			dstField.setAccessible(true);

			Class<?> dstFieldType = dstField.getType();
			if (isPrimitive(dstFieldType)) {
				if (isPrimitive(srcField.getType())) {
					if (isFinanceDate(srcField.getType())) {
						FinanceDate date = (FinanceDate) srcField.get(src);
						dstField.setLong(dst, date != null ? date.getDate() : 0);
					}else if (isDate(srcField.getType())) {
						Date date = (Date) srcField.get(src);
						dstField.setLong(dst, date != null ? date.getTime() : 0);
					}else {
						// Both are primitive, so assign directly
						dstField.set(dst, srcField.get(src));
					}
				} else {
					if (dstFieldName.equals(srcField.getName())
							|| isString(dstField.getType())) {
						dstField.set(dst, getFieldInstanceID(dstFieldName
								.equals("id") ? src : srcField.get(src)));

					}
				}
			} else if (isNotMappingEntity(srcType)) {
				dstField.set(dst,
						toClientObjectInternal(srcField.get(src), dstFieldType));
			} else {
				if (isSet(dstFieldType)) {
					if (isList(srcField.getType())) {
						HashSet set = new HashSet();
						List list = (List) toClientList((List<?>) srcField
								.get(src));
						if (list != null)
							set.addAll(list);
						dstField.set(dst, set);
					} else {
						dstField.set(dst,
								toClientSet((Set<?>) srcField.get(src)));
					}
				} else if (isList(dstFieldType)) {
					if (isSet(srcField.getType())) {
						ArrayList list = new ArrayList();
						Set set = toClientSet((Set<?>) srcField.get(src));
						if (set != null)
							list.addAll(set);

						dstField.set(dst, list);
					} else {
						dstField.set(dst,
								toClientList((List<?>) srcField.get(src)));
					}
				} else if (isMap(dstFieldType)
						|| srcField.get(src) instanceof Map) {
					Map<?, ?> map = toClientMap((Map) srcField.get(src));
					dstField.set(dst, map);
				} else {
					// Both are not primitive, So we have to call toClient
					// on
					// that src value and assign it to the destination

					if ((dstField.getType().getModifiers() & Modifier.ABSTRACT) > 0) {
						dstField.set(
								dst,
								getClientAfterCheckingInCache(
										srcField.get(src),
										getClientEqualentClass(srcField
												.get(src).getClass())));

					} else
						dstField.set(
								dst,
								getClientAfterCheckingInCache(
										srcField.get(src), dstField.getType()));
				}
			}
		}
		return dst;
	}

	private Map<Object, Object> toClientMap(Map<?, ?> map)
			throws AccounterException {

		Map<Object, Object> clientMap = new HashMap<Object, Object>();
		for (Object key : map.keySet()) {
			Object val = map.get(key);
			Object clientKey = null;
			if (key instanceof IAccounterServerCore) {
				clientKey = toClientObject(val,
						getClientEqualentClass(val.getClass()));
			}
			if (val instanceof IAccounterServerCore) {
				val = toClientObject(val,
						getClientEqualentClass(val.getClass()));

			}
			if (clientKey == null)
				clientMap.put(key, val);
			else {
				clientMap.put(clientKey, val);
			}

		}
		return clientMap;

	}

	/**
	 * This method is to convert Server Objects to Client Objects
	 * 
	 * @param <S>
	 *            IAccounterServerCore
	 * @param <D>
	 *            IAccounterCore
	 * @param src
	 *            Server Object
	 * @param dstType
	 *            Client Object
	 * @return
	 * @throws AccounterException
	 */
	public <S, D> D toClientObject(S src, Class<D> dstType)
			throws AccounterException {
		D ret;
		try {
			ret = toClientObjectInternal(src, dstType);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
		cache.set(null);
		return ret;
	}

	public <S, D> D toClientWithid(S src, D dst) {
		D ret = toClientWithidInternal(src, dst);
		cache.set(null);
		return ret;
	}

	private <S, D> D toClientWithidInternal(S src, D dst) {
		try {
			if (src == null)
				return null;

			Class<? extends Object> srcType = src.getClass();
			if (dst == null)
				dst = (D) getClientEqualentClass(srcType).newInstance();

			getCache().put(src, dst);

			Map<String, Field> srcMap = getAllFields(srcType);
			Map<String, Field> dstMap = getAllFields(dst.getClass());
			for (String dstFieldName : dstMap.keySet()) {
				Field dstField = dstMap.get(dstFieldName);
				Field srcField = srcMap.get(dstFieldName);

				if (srcField == null)
					continue;
				if ((srcField.getModifiers() & Modifier.STATIC) > 0)
					continue;
				if (srcField.get(src) == null)
					continue;

				if (dstField.getAnnotation(SpecialReference.class) != null) {
					continue;
				}
				srcField.setAccessible(true);
				dstField.setAccessible(true);

				Class<?> dstFieldType = dstField.getType();
				if (isNotMappingEntity(srcType))
					continue;
				if ((isMap(dstFieldType) || srcField.get(src) instanceof Map))
					continue;

				if (isSet(srcField.getType()) || isList(srcField.getType())) {
					Collection dstCollection = (Collection) dstField.get(dst);

					if (dstCollection == null)
						if (isList(dstFieldType)) {
							dstCollection = new ArrayList();
						} else if (isSet(dstFieldType)) {
							dstCollection = new HashSet();
						}
					dstField.set(
							dst,
							toCollection((Collection) srcField.get(src),
									dstCollection));

				} else if (isPrimitive(dstFieldType)) {
					if (dstFieldName.equals("id")) {
						dstField.set(dst, srcField.get(src));
					}

				} else {
					dstField.set(
							dst,
							getClientwithidAfterCheckingInCache(
									srcField.get(src), dstField.get(dst)));
				}
			}

			return dst;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public <S extends IAccounterServerCore, D extends IAccounterCore> Collection<D> toCollection(
			Collection<S> collection, Collection dstCollection) {
		Collection collection2 = null;
		try {
			collection2 = dstCollection.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		for (S s : collection) {
			if (s instanceof Address) {
				return dstCollection;
			}
			collection2.add(toClientWithidInternal(s,
					getObject(dstCollection, s.getID())));
		}
		return collection2;
	}

	public static <D extends IAccounterCore> D getObject(Collection<D> list,
			long id) {
		if (list == null)
			return null;
		for (D s : list) {
			if (s != null && s.getID() == id) {
				return s;
			}
		}
		return null;
	}

	private Set<?> toClientSet(Set<?> set) throws IllegalArgumentException,
			InstantiationException, IllegalAccessException, AccounterException {
		if (set == null)
			return null;
		HashSet result = new HashSet();
		if (set.size() == 0)
			return result;

		try {
			for (Object obj : set) {
				if (obj != null) {
					Class<? extends Object> resultClass = Class
							.forName("com.vimukti.accounter.web.client.core.Client"
									+ obj.getClass().getSimpleName());

					result.add(getClientAfterCheckingInCache(obj, resultClass));
				}
			}
			return result;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	private ArrayList toClientList(List<?> list) throws IllegalArgumentException,
			InstantiationException, IllegalAccessException, AccounterException {
		if (list == null)
			return null;
		ArrayList result = new ArrayList();
		if (list.size() == 0)
			return result;

		try {
			for (Object obj : list) {
				if (obj != null) {
					Class<? extends Object> resultClass = Class
							.forName("com.vimukti.accounter.web.client.core.Client"
									+ obj.getClass().getSimpleName());
					result.add(getClientAfterCheckingInCache(obj, resultClass));
				}
			}
			return result;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
