package run.halo.liquid.utils;

import java.io.File;
import java.net.URL;

/**
 * @author Dioxide.CN
 * @date 2023/5/23 9:07
 * @since 1.0
 */
public class NativeLoader {

    public static void loadNative(Class<?> clazz, String url) {
        URL resource = CPUWatchDog.class.getResource("/native/" + url);
        if (resource == null) {
            throw new Error("can't find native source from " + url);
        }
        File file = new File(resource.getFile());
        System.load(file.getAbsolutePath());
    }

}
