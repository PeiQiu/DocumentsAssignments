package com.starstar.filters;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebFilter
public class CsrfTokenFilter extends OncePerRequestFilter {

    protected static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
    protected static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";
    protected static final String RESPONSE_PARAM_NAME = "X-CSRF-PARAM";
    protected static final String RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
        String tokenString = token.getToken();
        System.out.println("REQUEST -------------------------");
        System.out.println("token string : " + tokenString);
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            System.out.println(header + ":" + request.getHeader(header));
        }
        System.out.println();

        Enumeration<String> atts = request.getAttributeNames();
        while (atts.hasMoreElements()) {
            String att = atts.nextElement();
            System.out.println(att + ":" + request.getAttribute(att));
        }

//        if (token == null) {
//            token = new CsrfToken() {
//                private String token = UUID.randomUUID().toString();
//                @Override
//                public String getHeaderName() {
//                    return RESPONSE_HEADER_NAME;
//                }
//
//                @Override
//                public String getParameterName() {
//                    return RESPONSE_PARAM_NAME;
//                }
//
//                @Override
//                public String getToken() {
//                    return token;
//                }
//            };
//        }

        if (token != null) {
            response.setHeader(RESPONSE_HEADER_NAME, token.getHeaderName());
            response.setHeader(RESPONSE_PARAM_NAME, token.getParameterName());
            response.setHeader(RESPONSE_TOKEN_NAME, token.getToken());
        }

        System.out.println("RESPONSE -------------------------");
        for (String header : response.getHeaderNames()) {
            System.out.println(header + ":" + response.getHeader(header));
        }

        filterChain.doFilter(request, response);
    }
}