package com.fxdata.fxratesmonitor.httpclient;


import com.fxdata.fxratesmonitor.config.EhsConfiguration;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class EhsWebClientBuilder {

    private final EhsConfiguration ehsConfiguration;

    public EhsWebClientBuilder(EhsConfiguration ehsConfiguration) {
        this.ehsConfiguration = ehsConfiguration;
    }

    public WebClient build(String url){
        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public HttpClient httpClient (){
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ehsConfiguration.getConnectTimeout())
                .responseTimeout(Duration.ofMillis(ehsConfiguration.getResponseTimeout()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(ehsConfiguration.getReadTimeout(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(ehsConfiguration.getWriteTimeout(), TimeUnit.MILLISECONDS)));
    }



}
