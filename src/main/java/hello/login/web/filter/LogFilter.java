package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


//필타 사용을 위한 필터 인터페이스 구현
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException{
    log.info("log filter init");
    }

    //HTTP 요청이 오면 doFilter가 호출된다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException{

        //ServletRequest request는 HTTP 요청이 아닌 경우 까지 고려해서 만든 인터페이스이다.
        //HTTP 를 사용하면 HttpServletRequest로 다운 캐스팅하자.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}{}]",uuid,requestURI);
            //이 부분이 가장 중요하다 다음 필터가 있으면 필터를 호출하고 필터가 없으면 서블릿을 호출한다.
            //만약 이 로직을 호출하지 않으면 다음 단계로 진행되지 않는다.
            chain.doFilter(request,response);
        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}{}]",uuid,requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
