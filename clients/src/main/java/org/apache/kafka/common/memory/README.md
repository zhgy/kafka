# MemoryPool

概述
- 接口：MemoryPool
- 实现：SimpleMemoryPool
- 调试用实现：GarbageCollectedMemoryPool

## SimpleMemoryPool
其实就是封装了`ByteBuffer.allocate(sizeBytes)`来分配buffer，增加了配额与监控相关的逻辑。
- 可用量使用`AtomicLong`类型来实现，即保证了更新操作的原子性，又避免了加锁对性能的伤害。分配buffer毕竟是个高频操作。

## GarbageCollectedMemoryPool

> 被`WeakReference`引用的对象在下一次GC的时候如果没被其他对象引用的话就会被释放，但是WeakReference本身也是一个对象，它并不会被释放。那么
我们怎么知道被持有的对象是否真的被GC了呢？在创建`WeakReference`的时候如果指定一个`ReferenceQueue`的话，ref引用的对象在被GC之后，ref
会被放进这个Queue中，而我们可以通过这个Queue来拿到这个ref，并使用ref#get来判断它引用的对象有没有被释放。