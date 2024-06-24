package ml.volder.unikapi.api;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import ml.volder.unikapi.NotSupportedAPIException;
import ml.volder.unikapi.SupportedClient;
import ml.volder.unikapi.UnikAPI;
import ml.volder.unikapi.utils.AnnotationsScanner;

public class ApiManager {

    public static <T> T getAPI(ApiProvider<T> apiProvider, String packageName, Function<ApiReferenceStorage, T> referenceStorageFunction, Class<T> expectedReturnType) {
        if(apiProvider.isApiSpecified())
            return apiProvider.getApi();
        if(UnikAPI.isReferenceStorageSpecified()) {
            T api = referenceStorageFunction.apply(UnikAPI.getApiReferenceStorage());
            if(api != null && isSupported(api.getClass())) {
                apiProvider.setApi(api);
                return api;
            }
        }

        T api = getAPIUsingReflection(apiProvider, packageName, expectedReturnType);
        if(api != null) {
            apiProvider.setApi(api);
            return api;
        }

        throw new NotSupportedAPIException(apiProvider.getApiName(), packageName, expectedReturnType.getName());
    }

    private static <T> T getAPIUsingReflection(ApiProvider<T> apiProvider, String packageName, Class<T> expectedReturnType) {
        List<Class<?>> classes = AnnotationsScanner.getAnnotatedClasses(SupportedClient.class, packageName, apiProvider.getClass().getClassLoader());
        for (Class<?> klass: classes) {
            if (isSupported(klass)) {
                try {
                    Method method = klass.getDeclaredMethod("getAPI", null);
                    if(method == null)
                        continue;
                    T playerAPI = (T) method.invoke(null, null);
                    if(playerAPI == null)
                        continue;
                    apiProvider.setApi(playerAPI);
                    return playerAPI;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        throw new NotSupportedAPIException(apiProvider.getApiName(), packageName, expectedReturnType.getName());
    }

    public static Class getClassAPI(ApiProvider<Class> apiProvider, String packageName, Function<ApiReferenceStorage, Class> referenceStorageFunction, Class expectedReturnType) {
        if(apiProvider.isApiSpecified())
            return apiProvider.getApi();
        if(UnikAPI.isReferenceStorageSpecified()) {
            Class api = referenceStorageFunction.apply(UnikAPI.getApiReferenceStorage());
            if(api != null && isSupported(api)) {
                apiProvider.setApi(api);
                return api;
            }
        }
        Class api = getClassAPIUsingReflection(apiProvider, packageName, expectedReturnType);
        if(api != null) {
            apiProvider.setApi(api);
            return api;
        }
        throw new NotSupportedAPIException(apiProvider.getApiName(), packageName, expectedReturnType.getName());
    }

    private static Class getClassAPIUsingReflection(ApiProvider<Class> apiProvider, String packageName, Class expectedReturnType) {
        List<Class<?>> classes = AnnotationsScanner.getAnnotatedClasses(SupportedClient.class, packageName, apiProvider.getClass().getClassLoader());
        for (Class klass: classes) {
            if(isSupported(klass)) {
                try {
                    apiProvider.setApi(klass);
                    return klass;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        throw new NotSupportedAPIException(apiProvider.getApiName(), packageName, expectedReturnType.getName());
    }

    private static boolean isSupported(Class<?> klass) {
        if (!klass.isAnnotationPresent(SupportedClient.class))
            if(klass.getSuperclass() != null)
                return isSupported(klass.getSuperclass());
            else
                return false;
        if(!UnikAPI.isSupported(klass.getAnnotation(SupportedClient.class)))
            return false;
        return true;
    }
}
