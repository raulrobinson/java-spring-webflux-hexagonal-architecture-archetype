package com.raulbolivarnavas.ms.usecase;

import com.raulbolivarnavas.ms.ports.in.IParameterStoreGateway;
import com.raulbolivarnavas.ms.ports.out.ParameterStoreGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParameterStoreUseCase implements IParameterStoreGateway {

    private final ParameterStoreGateway gateway;

    @Override
    public Mono<String> getParameter(String name)  {
        return gateway.getParameter(name);
    }
}
