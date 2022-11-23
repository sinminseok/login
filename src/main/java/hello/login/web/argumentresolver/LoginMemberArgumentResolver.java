package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {


    //@Login애노테이션이 있으면서 Member 타입이면 해당 ArgumentResolver가 실행됨
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasMethodAnnotation(Login.class);

        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberType && hasLoginAnnotation;
    }

    //컨트롤러 호출 직전에 호출되어 필요한 파라미터 정보를 ㄹ생성해준다. 여기서는 세션에 있는 로그인 회원정보인 member객체를 찾아 반환해주고 이후 스프링 MVC는 컨트롤러의 메서드를 호출하고 여기서 반환된 member객체를ㄹ 파라미터에 전달해준다.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session == null){
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
