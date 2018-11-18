package edu.comp6591.problog.datastructure;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class MultisetHashMap<K, V> {
	private Map<K, Multiset<V>> map;

	public MultisetHashMap() {
		map = new HashMap<>();
	}

	public boolean put(K key, V value) {
		Multiset<V> s = map.get(key);
		if (s == null) {
			s = HashMultiset.create();
			map.put(key, s);
		}
		return s.add(value);
	}

	public Multiset<V> get(K key) {
		return map.get(key);
	}

	public boolean remove(K key, V value) {
		Multiset<V> s = map.get(key);
		if (s == null) {
			return false;
		}
		boolean changed = s.remove(value);
		if (s.size() == 0) {
			map.remove(key);
		}
		return changed;

	}
}
