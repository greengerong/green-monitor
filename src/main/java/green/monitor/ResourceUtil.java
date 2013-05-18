package green.monitor;

import java.io.InputStream;

public class ResourceUtil {

    private static Class type = ResourceUtil.class;

    protected void setClass(Class type) {
        ResourceUtil.type = type;
    }

    public static InputStream getResourceAsStream(String requestFile) {
        return type.getClass().getResourceAsStream(requestFile);
    }
}
