package com.yc.httpserver;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class ExceptionUtils {

    /*
     * 在使用Retrofit+RxJava时，我们访问接口，获取数据的流程一般是这样的：订阅->访问接口->解析数据->展示。
     * 如上所说，异常和错误本质是一样的，因此我们要尽量避免在View层对错误进行判断，处理。
     *
     * 在获取数据的流程中，访问接口和解析数据时都有可能会出错，我们可以通过拦截器在这两层拦截错误。
     * 1.在访问接口时，我们不用设置拦截器，因为一旦出现错误，Retrofit会自动抛出异常。
     * 2.在解析数据时，我们设置一个拦截器，判断Result里面的code是否为成功，如果不成功，则要根据与服务器约定好的错误码来抛出对应的异常。
     * 3.除此以外，为了我们要尽量避免在View层对错误进行判断，处理，我们必须还要设置一个拦截器，拦截onError事件，然后使用ExceptionHandler，让其根据错误类型来分别处理。
     */



    /**
     * 对应HTTP的状态码
     */
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int METHOD_NOT_ALLOWED = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int CONFLICT = 409;
    private static final int PRECONDITION_FAILED = 412;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    /**
     * 服务器定义的状态吗
     * 比如：登录过期，提醒用户重新登录；
     *      添加商品，但是服务端发现库存不足，这个时候接口请求成功，服务端定义业务层失败，服务端给出提示语，客户端进行吐司
     *      请求接口，参数异常或者类型错误，请求code为200成功状态，不过给出提示，这个时候客户端用log打印服务端给出的提示语，方便快递查找问题
     *      其他情况，接口请求成功，但是服务端定义业务层需要吐司服务端返回的对应提示语
     */
    /**
     * 完全成功
     */
    private static final int CODE_SUCCESS = 0;
    /**
     * Token 失效
     */
    public static final int CODE_TOKEN_INVALID = 401;
    /**
     * 缺少参数
     */
    public static final int CODE_NO_MISSING_PARAMETER = 400400;
    /**
     * 其他情况
     */
    public static final int CODE_NO_OTHER = 403;
    /**
     * 统一提示
     */
    public static final int CODE_SHOW_TOAST = 400000;



    /**
     * 这个可以处理服务器请求成功，但是业务逻辑失败，比如token失效需要重新登陆
     * @param code                  自定义的code码
     */
    public static void serviceException(int code , String content){
        if (code != CODE_SUCCESS){
            ServerException serverException = new ServerException();
            serverException.setCode(code);
            serverException.setMessage(content);
            handleException(serverException);
        }
    }

    /**
     * 这个是处理网络异常，也可以处理业务中的异常
     * @param e                     e异常
     */
    public static void handleException(Throwable e){
        HttpException ex;
        //HTTP错误   网络请求异常 比如常见404 500之类的等
        if (e instanceof retrofit2.HttpException){
            retrofit2.HttpException httpException = (retrofit2.HttpException) e;
            ex = new HttpException(e, ErrorCode.HTTP_ERROR);
            switch(httpException.code()){
                case BAD_REQUEST:
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case METHOD_NOT_ALLOWED:
                case REQUEST_TIMEOUT:
                case CONFLICT:
                case PRECONDITION_FAILED:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    //均视为网络错误
                default:
                    ex.setDisplayMessage("网络错误"+httpException.code());
                    break;
            }
        } else if (e instanceof ServerException){
            //服务器返回的错误
            ServerException resultException = (ServerException) e;
            int code = resultException.getCode();
            String message = resultException.getMessage();
            ex = new HttpException(resultException, ErrorCode.SERVER_ERROR);
            switch (code){
                case CODE_TOKEN_INVALID:
                    ex.setDisplayMessage("重新登陆");
                    break;
                case CODE_NO_OTHER:
                    ex.setDisplayMessage("其他情况");
                    break;
                case CODE_SHOW_TOAST:
                    ex.setDisplayMessage("吐司");
                    break;
                case CODE_NO_MISSING_PARAMETER:
                    ex.setDisplayMessage("缺少参数");
                    break;
                default:
                    ex.setDisplayMessage(message);
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new HttpException(e, ErrorCode.PARSE_ERROR);
            //均视为解析错误
            ex.setDisplayMessage("解析错误");
        }else if(e instanceof ConnectException){
            ex = new HttpException(e, ErrorCode.NETWORK_ERROR);
            //均视为网络错误
            ex.setDisplayMessage("连接失败");
        } else if(e instanceof java.net.UnknownHostException){
            ex = new HttpException(e, ErrorCode.NETWORK_ERROR);
            //网络未连接
            ex.setDisplayMessage("未知网络未连接");
        } else if (e instanceof SocketTimeoutException) {
            ex = new HttpException(e, ErrorCode.NETWORK_ERROR);
            //网络未连接
            ex.setDisplayMessage("服务器响应超时");
        }  else {
            ex = new HttpException(e, ErrorCode.UNKNOWN);
            //未知错误
            ex.setDisplayMessage("未知错误");
        }
        String displayMessage = ex.getDisplayMessage();
        //这里直接吐司日志异常内容，注意正式项目中一定要注意吐司合适的内容
    }


}
