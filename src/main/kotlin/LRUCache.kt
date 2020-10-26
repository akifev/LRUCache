import kotlin.math.min

class LRUCache<K, V>(private val capacity: Int) : Map<K, V> {
    private class DoubleLinkedBuffer<K, V>(private val capacity: Int) {
        data class LinkedNode<K, V>(
            var key: K,
            var value: V,

            var prev: LinkedNode<K, V>? = null,
            var next: LinkedNode<K, V>? = null
        )

        /**
         * Most recently used element
         */
        var first: LinkedNode<K, V>? = null
            private set

        /**
         * Least recently used element
         */
        var last: LinkedNode<K, V>? = null
            private set

        var size: Int = 0
            private set

        init {
            require(capacity > 0) { "Capacity must be positive" }
        }

        fun pushFront(key: K, value: V) {
            val oldSize = size

            when (size) {
                0 -> {
                    val newNode = LinkedNode(key, value)
                    first = newNode
                    last = newNode
                    size++
                }
                capacity -> {
                    removeNode(last!!)
                    pushFront(key, value)
                }
                else -> {
                    val newNode = LinkedNode(key, value, prev = first)
                    first!!.next = newNode
                    first = newNode
                    size++
                }
            }

            assert(size in 1..capacity && size == min(oldSize + 1, capacity))
            assert(first != null && first!!.key == key && first!!.value == value)
            assert(last != null)
        }

        fun removeNode(node: LinkedNode<K, V>) {
            if (size == 1) {
                clear()

                assert(size == 0)
                assert(first == null)
                assert(last == null)

                return
            }

            val oldSize = size

            node.prev?.let { it.next = node.next }
            node.next?.let { it.prev = node.prev }
            size--

            if (node == first) {
                first = first?.prev
            } else if (node == last) {
                last = last?.next
            }

            assert(size in 1..capacity && size == oldSize - 1)
            assert(first != null)
            assert(last != null)
        }

        fun clear() {
            first = null
            last = null
            size = 0

            assert(size == 0)
            assert(first == null)
            assert(last == null)
        }
    }

    private val hashMap: HashMap<K, DoubleLinkedBuffer.LinkedNode<K, V>>
    private val linkedBuffer: DoubleLinkedBuffer<K, V>

    init {
        require(capacity > 0) { "Capacity must be positive" }
        linkedBuffer = DoubleLinkedBuffer(capacity)
        hashMap = hashMapOf()
    }

    /**
     * Returns the number of elements in the cache.
     */
    override val size: Int
        get() = hashMap.size

    /**
     * Returns `true` if the cache is empty (contains no elements), `false` otherwise.
     */
    override fun isEmpty(): Boolean = hashMap.isEmpty()

    /**
     * Returns `true` if the cache contains the specified [key].
     */
    override fun containsKey(key: K): Boolean = hashMap.containsKey(key)

    /**
     * Returns `true` if the cache maps one or more keys to the specified [value].
     */
    override fun containsValue(value: V): Boolean {
        for ((_, v) in hashMap) {
            if (v.value == value) {
                return true
            }
        }
        return false
    }

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the cache.
     */
    override fun get(key: K): V? {
        val oldSize = size

        val node = hashMap[key]
        if (node != null) {
            linkedBuffer.removeNode(node)
            linkedBuffer.pushFront(key, node.value)
            hashMap[key] = linkedBuffer.first!!

            assert(linkedBuffer.first?.value == hashMap[key]?.value)
        }

        assert(size == oldSize)

        return node?.value
    }

    /**
     * Returns a [MutableSet] of all keys in this cache.
     */
    override val keys: MutableSet<K>
        get() = hashMap.keys

    /**
     * Returns a read-only [Collection] of all values in this cache. Note that this collection may contain duplicate values.
     */
    override val values: Collection<V>
        get() = hashMap.map { it.value.value }

    /**
     * Returns a read-only [Set] of all key/value pairs in this cache.
     */
    override val entries: Set<Map.Entry<K, V>>
        get() = hashMap.mapValues { it.value.value }.entries

    /**
     * Associates the specified [value] with the specified [key] in the cache.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the cache.
     */
    fun put(key: K, value: V): V? {
        val oldSize = size

        val node = hashMap[key]

        if (node == null && size == capacity) {
            hashMap.remove(linkedBuffer.last!!.key)
        } else if (node != null) {
            linkedBuffer.removeNode(node)
        }
        linkedBuffer.pushFront(key, value)
        hashMap[key] = linkedBuffer.first!!

        assert(size in 1..capacity)
        assert(size == min(oldSize + 1, capacity))
        assert(linkedBuffer.first != null && linkedBuffer.first!!.key == key && linkedBuffer.first!!.value == value)
        assert(hashMap[key] != null && hashMap[key]!!.value == value)
        assert(linkedBuffer.last != null)

        return node?.value
    }

    /**
     * Removes all elements from this cache.
     */
    fun clear() {
        hashMap.clear()
        linkedBuffer.clear()

        assert(size == 0)
        assert(linkedBuffer.first == null)
        assert(linkedBuffer.last == null)
    }
}