package ybl.lib.backend.util;

/**
 * ThreadLocal 工具类
 * 用于在同一个线程内共享数据（比如当前登录用户信息）
 */
public class ThreadLocalUtil {

    // 提供 ThreadLocal 对象
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    /**
     * 根据键获取值（泛型方法，自动强转）
     */
    @SuppressWarnings("unchecked")
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    /**
     * 存储键值对
     */
    public static void set(Object value) {
        THREAD_LOCAL.set(value);
    }

    /**
     * 清除 ThreadLocal，防止内存泄漏
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}