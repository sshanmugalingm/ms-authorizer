package au.com.bglcorp.util;

import au.com.bglcorp.domain.jwt.TokenPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by senthurshanmugalingm on 30/06/2017.
 */
public interface AuthorizerUtil {

    GsonBuilder gsonBuilder = new GsonBuilder();

    static String buildJsonString(Object object) {
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    static <T> T buildObjectFromJsonString(String jsonString, Class<T> clazz) {
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonString, clazz);
    }

}
