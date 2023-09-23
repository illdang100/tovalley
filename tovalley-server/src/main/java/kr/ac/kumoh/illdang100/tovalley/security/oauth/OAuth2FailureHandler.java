package kr.ac.kumoh.illdang100.tovalley.security.oauth;

import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomOAuth2AuthenticationException;
import kr.ac.kumoh.illdang100.tovalley.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String DEFAULT_URL = "http://13.125.136.237/";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 Authentication Failure:", exception);
        String errorMessage;
        if (exception instanceof CustomOAuth2AuthenticationException) {
            errorMessage = exception.getMessage();
        } else {
            log.debug(exception.getMessage());
            log.debug(exception.getClass().toString());
            errorMessage = "인증 실패";
        }

        resultRedirectStrategy(request, response);
        CustomResponseUtil.fail(response, errorMessage, HttpStatus.BAD_REQUEST);
    }

    private void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginUrl = DEFAULT_URL + "/login";

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, loginUrl);
    }
}
