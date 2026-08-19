package com.ladislaurenan.flowable_spring_demo.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("weatherDelegate")
public class WeatherDelegate implements JavaDelegate {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        Double latitude = (Double) execution.getVariable("latitude");
        Double longitude = (Double) execution.getVariable("longitude");

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                latitude, longitude
        );

        String resposta = restTemplate.getForObject(url, String.class);

        execution.setVariable("respostaClima", resposta);
    }
}