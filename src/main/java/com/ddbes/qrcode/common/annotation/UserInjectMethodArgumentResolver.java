package com.ddbes.qrcode.common.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by daitian on 2018/7/10.
 */
public class UserInjectMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(CurrentUser.class)&&methodParameter.hasParameterAnnotation(UserInject.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        CurrentUser currentUser = (CurrentUser) nativeWebRequest.getAttribute(CurrentUser.CURRENT_USER, RequestAttributes.SCOPE_REQUEST);
        if(currentUser!=null){
            return currentUser;
        }
        //TODO 处理异常
        return null;
    }
}
