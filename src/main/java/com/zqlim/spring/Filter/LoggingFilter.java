package com.zqlim.spring.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        requestWrapper.getInputStream();

        filterChain.doFilter(requestWrapper,responseWrapper);
        filterChain.doFilter(request,response);


        String requestBody = getString(requestWrapper.getContentAsByteArray(),requestWrapper.getCharacterEncoding());
        String responseBody = getString(responseWrapper.getContentAsByteArray(),responseWrapper.getCharacterEncoding());

        LOGGER.info("Log:\nMETHOD = {}" +
                        "\nRequest URI = {}" +
                        "\nResponse Status Code = {}" +
                        "\nRequest Body = {}" +
                        "\nResponse Body = {}",
                request.getMethod(),request.getRequestURI(),response.getStatus(),requestBody,responseBody);

    }

    private String getString(byte[] contentAsByteArray, String charEncoding){
        try{
            return new String(contentAsByteArray, 0, contentAsByteArray.length, charEncoding);
        } catch(UnsupportedEncodingException uee){
            uee.printStackTrace();
        }
        return "";
    }

}
