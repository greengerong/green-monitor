package green.monitor;

import java.io.InputStream;

public class ResourceUtil {

    public static InputStream getThreadResourceAsStream(String requestFile) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return getResourceAsStream(requestFile, classLoader);
    }

    private static InputStream getResourceAsStream(String requestFile, ClassLoader classLoader) {
        return classLoader.getResourceAsStream(requestFile);
    }

    public static InputStream getCurrentResourceAsStream(String requestFile) {
        return getResourceAsStream(requestFile, ResourceUtil.class.getClassLoader());
    }
}
