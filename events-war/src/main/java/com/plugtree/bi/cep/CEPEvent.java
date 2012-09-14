package com.plugtree.bi.cep;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CEPEvent implements Map<String, Object> {

	private final Map<String, Object> map = new HashMap<String, Object>();
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	public void clear() {
		map.clear();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public int size() {
		return map.size();
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CEPEvent other = (CEPEvent) obj;
		if (map == null) {
			if (other.map != null) return false;
		} else if (!map.equals(other.map)) return false;
		return true;
	}
}
