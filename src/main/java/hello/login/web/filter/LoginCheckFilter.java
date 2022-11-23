package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login",
            "/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", request);
            //whitelist가 아니면 인증 로직 실행
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    //미인증 사용자는 로그인 화면으로 리다이렉트한다. 그런데 로그인 이후에 다시 홈으로 이동해버리면 원하는 경로를 다시 찾아가야하는 불편함이있다.
                    //이런 부분이 개발자 입장에선 귀찮지만 사용자 입장으로 보면 편리한 기능이다. 이러한 기능을 위해 login 파라미터에 rediretURL을 넘겨준다.물론 /login 컨트롤러에서 로그인 성공시 해당 경로로 이동하는 기능은 추가 개발을 해야함

                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    //여기가 중요 ㅇㅇ 필터를 더는 진행하지 않는다 이후 필터는 물론 서블릿,컨트롤러가 더는 호출되지 않는다ㅣ 앞서 redirect를 사용했기 때문에 rediect가 응답으로 적용되고 요청이 끝!
                    return;
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }



    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
