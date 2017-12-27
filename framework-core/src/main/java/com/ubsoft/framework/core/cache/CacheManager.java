package com.ubsoft.framework.core.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import redis.clients.jedis.JedisCluster;
import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.support.util.JedisUtil;
/**
 * 
* @ClassName: CacheManager
* @Description: 通用缓存类
* @author chenkf
* @date 2017-2-21 上午10:26:26
* @version V1.0
 */
public class CacheManager {
	private JedisCluster jedisCluster;  
	//public ShardedJedisPool jedisPool;
	//static URL url = CacheManager.class.getClassLoader().getResource("./conf/Ehcache.xml");
	public static net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager(System.getProperty("net.sf.ehcache.path"));
	private boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;
	public CacheManager() {		
		//jedisPool = (ShardedJedisPool) AppContext.getBean("jedisPool");
		if(isRedis){
			jedisCluster=(JedisCluster) AppContext.getBean("jedisCluster");
		}
	} 
	

	/**	
	* @Title: get
	* @Description: 
	* @author chenkf
	* @date  2017-2-21 上午10:26:47
	* @param name
	* @param key
	* @param isByte
	* @return
	 */
	public Object get(String name, String key, boolean isByte) {
		boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;
		//ShardedJedis jedis = null;
		if (isRedis) {
			try {
				 
				//jedis = jedisCluster.getResource();
				if (isByte) {
					byte[] value = jedisCluster.hget(name.getBytes(), key.getBytes());

					if (value != null)
						return JedisUtil.unserialize(value);
					else
						return null;
				} else {
					String value = jedisCluster.hget(name, key);
					return value;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//jedisPool.returnResourceObject(jedis);
				//jedisPool.close();
			}
			return null;
		} else {
			Cache cache = cacheManager.getCache(name);
			Element el = cache.get(key);
			if (el != null) {
				return el.getObjectValue();
			} else {
				return null;
			}
		}

	}

	/**
	 * 
	 * @param name
	 * @param key
	 * @param value
	 * @param expDate
	 */
	public void put(String name, String key, Object value) {
		boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;		
		if (isRedis) {
			try {
				
				if (value instanceof String) {
					jedisCluster.hset(name, key, value + "");
				} else {
					jedisCluster.hset(name.getBytes(), key.getBytes(), JedisUtil.serialize(value));

				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
			}

		} else {
			Cache cache = cacheManager.getCache(name);
			cache.put(new Element(key, value));

		}

	}

	public void remove(String name, String key, boolean isByte) {
		boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;
		
		if (isRedis) {
			try {
				
				if (isByte) {
					jedisCluster.hdel(name.getBytes(), key.getBytes());
				} else {
					jedisCluster.hdel(name, key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			
			}

		} else {
			Cache cache = cacheManager.getCache(name);
			cache.remove(key);
		}
	}

	public void clear(String name, boolean isByte) {
		boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;
		
		if (isRedis) {
			try {
				
				if (isByte) {
					jedisCluster.del(name);
				} else {
					jedisCluster.del(name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			
			}

		} else {
			Cache cache = cacheManager.getCache(name);
			cache.removeAll();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAll(String name, boolean isByte) {
		boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;
		
		if (isRedis) {
			try {				
				ArrayList list = new ArrayList();
				if (isByte) {
					Map<byte[], byte[]> value = jedisCluster.hgetAll(name.getBytes());
					java.util.Iterator it = value.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						list.add(JedisUtil.unserialize((byte[]) entry.getValue()));
					}
					return list;
				} else {
					Map value = jedisCluster.hgetAll(name);
					java.util.Iterator it = value.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						list.add(entry.getValue().toString());
					}
					return list;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
			}
			return null;
		} else {
			Cache cache = cacheManager.getCache(name);
			ArrayList list = new ArrayList();
			for (Object key : cache.getKeys()) {
				Element el = cache.get(key);
				list.add(el.getObjectValue());

			}
			return list;
		}

	}

	public boolean containsValue(String name, Object value) {
		boolean isRedis = AppConfig.getDataItem("cacheMode").equals("redis") ? true : false;
	
		if (isRedis) {
			try {
			
				Map map = jedisCluster.hgetAll(name);
				return map.containsValue(value);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				
			}

		} else {
			Cache cache = cacheManager.getCache(name);
			ArrayList list = new ArrayList();
			for (Object key : cache.getKeys()) {
				Element el = cache.get(key);
				Object valueTemp = el.getObjectValue();
				if (valueTemp.toString().equals(value.toString())) {
					return true;
				}

			}

		}
		return false;
	}
}
