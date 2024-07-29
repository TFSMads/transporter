package ml.volder.unikapi.utils;


import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationsScanner {
    /**
     * Finds all classes within a package which are annotated with certain
     * annotation.
     *
     * @param annotation  Annotation which we are looking for
     * @param <T>         Annotation class
     * @param packageName Package in which to search
     * @return The list of annotated classes
     */
    public static <T extends Annotation> List<Class<?>> getAnnotatedClasses(Class<T> annotation, String packageName, ClassLoader classLoader) {
        //TODO implement
        return null;
    }


}
