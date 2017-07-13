package au.com.bglcorp.helper

import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

/**
 * Created by senthurshanmugalingm on 13/07/2017.
 */
class RestHelper {

    static Map REQUESTHEADER = [
            'Accept'         : "application/json",
            'User-Agent'     : "Mozilla/5.0 Firefox/3.0.4",
            'Content-Type'   : "application/json",
            'Authorization'  : 'Basic REVWRUxPUE1FTlQ6MDM2ODUzMmYtNGZkYi00ZjkzLWE0NGYtNTdkNThlMmVlNWVm'
    ]

    static def post(String host, String path, String jsonBody) {
        try {
            return new RESTClient(host).post(
                    path : path,
                    contentType : ContentType.JSON,
                    headers : REQUESTHEADER,
                    body : jsonBody
            ).getData()

        } catch (HttpResponseException e) {
            return [message : e.response.responseData.message, status : e.response.status]
        }
    }

    static def delete(String host, String path) {
        try {
            return new RESTClient(host).delete(
                    path : path,
                    contentType : ContentType.JSON,
                    headers : REQUESTHEADER
            ).getData()

        } catch (HttpResponseException e) {
            return [message : e.response.responseData.message, status : e.response.status]
        }
    }

}
