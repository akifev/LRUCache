import org.junit.Before
import org.junit.Test

internal class LRUCacheTest {
    lateinit var cache: LRUCache<Int, Int>

    @Before
    fun init() {
        cache = LRUCache(2)
    }

    @Test
    fun getSize() {
        check(cache.size == 0)
        cache.put(1, 11)
        check(cache.size == 1)
        cache.put(2, 22)
        check(cache.size == 2)
        cache.put(3, 33)
        check(cache.size == 2)
        cache.put(1, 11)
        check(cache.size == 2)
        cache.clear()
        check(cache.size == 0)
    }

    @Test
    fun isEmpty() {
        check(cache.isEmpty())
        cache.put(1, 11)
        check(cache.isNotEmpty())
        cache.put(2, 22)
        check(cache.isNotEmpty())
        cache.put(3, 33)
        check(cache.isNotEmpty())
        cache.clear()
        check(cache.isEmpty())
    }

    @Test
    fun containsKey() {
        check(!cache.containsKey(1))
        cache.put(1, 11)
        check(cache.containsKey(1))
        cache.put(2, 22)
        check(cache.containsKey(1))
        check(cache.containsKey(2))
        cache.put(3, 33)
        check(!cache.containsKey(1))
        check(cache.containsKey(2))
        check(cache.containsKey(3))
        cache.clear()
        check(!cache.containsKey(1))
        check(!cache.containsKey(2))
        check(!cache.containsKey(3))
    }

    @Test
    fun containsValue() {
        check(!cache.containsValue(11))
        cache.put(1, 11)
        check(cache.containsValue(11))
        cache.put(2, 22)
        check(cache.containsValue(11))
        check(cache.containsValue(22))
        cache.put(3, 33)
        check(!cache.containsValue(11))
        check(cache.containsValue(22))
        check(cache.containsValue(33))
        cache.clear()
        check(!cache.containsValue(11))
        check(!cache.containsValue(22))
        check(!cache.containsValue(33))
    }

    @Test
    fun get() {
        check(cache[1] == null)
        cache.put(1, 11)
        check(cache[1] == 11)
        cache.put(2, 22)
        check(cache[1] == 11)
        check(cache[2] == 22)
        cache.put(3, 33)
        check(cache[1] == null)
        check(cache[3] == 33)
        check(cache[2] == 22)
        cache.put(1, 11)
        check(cache[1] == 11)
        check(cache[2] == 22)
        check(cache[3] == null)
        cache.clear()
        check(cache[1] == null)
        check(cache[2] == null)
        check(cache[3] == null)
    }

    @Test
    fun getKeys() {
        val hashSet = hashSetOf<Int>()
        check(cache.keys.containsAll(hashSet) && hashSet.containsAll(cache.keys))
        cache.put(1, 11)
        hashSet.add(1)
        check(cache.keys.containsAll(hashSet) && hashSet.containsAll(cache.keys))
        cache.put(2, 22)
        hashSet.add(2)
        check(cache.keys.containsAll(hashSet) && hashSet.containsAll(cache.keys))
        cache.put(3, 33)
        hashSet.remove(1)
        hashSet.add(3)
        check(cache.keys.containsAll(hashSet) && hashSet.containsAll(cache.keys))
    }

    @Test
    fun getValues() {
        val hashMap = hashMapOf<Int, Int>()
        check(cache.values.containsAll(hashMap.values) && hashMap.values.containsAll(cache.values))
        cache.put(1, 11)
        hashMap.put(1, 11)
        check(cache.values.containsAll(hashMap.values) && hashMap.values.containsAll(cache.values))
        cache.put(2, 22)
        hashMap.put(2, 22)
        check(cache.values.containsAll(hashMap.values) && hashMap.values.containsAll(cache.values))
        cache.put(3, 33)
        hashMap.remove(1)
        hashMap.put(3, 33)
        check(cache.values.containsAll(hashMap.values) && hashMap.values.containsAll(cache.values))
    }

    @Test
    fun getEntries() {
        val hashMap = hashMapOf<Int, Int>()
        check(cache.entries.containsAll(hashMap.entries) && hashMap.entries.containsAll(cache.entries))
        cache.put(1, 11)
        hashMap.put(1, 11)
        check(cache.entries.containsAll(hashMap.entries) && hashMap.entries.containsAll(cache.entries))
        cache.put(2, 22)
        hashMap.put(2, 22)
        check(cache.entries.containsAll(hashMap.entries) && hashMap.entries.containsAll(cache.entries))
        cache.put(3, 33)
        hashMap.remove(1)
        hashMap.put(3, 33)
        check(cache.entries.containsAll(hashMap.entries) && hashMap.entries.containsAll(cache.entries))
    }

    @Test
    fun put() {
        cache.put(1, 11)
        check(cache.contains(1))
        cache.put(2, 22)
        check(cache.contains(1))
        check(cache.contains(2))
        cache.put(3, 33)
        check(!cache.contains(1))
        check(cache.contains(2))
        check(cache.contains(3))
    }

    @Test
    fun clear() {
        cache.put(1, 11)
        cache.put(2, 22)
        cache.put(3, 33)
        cache.clear()
        check(!cache.contains(1))
        check(!cache.contains(2))
        check(!cache.contains(3))
        check(cache.isEmpty() && cache.size == 0)
    }
}