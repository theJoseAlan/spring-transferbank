package com.spring.tb.api.assembler;

import com.spring.tb.api.model.ExtratoResponse;
import com.spring.tb.domain.model.Extrato;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ExtratoAssembler {

    private ModelMapper modelMapper;

    public ExtratoResponse toModel(Extrato extrato){
        return modelMapper.map(extrato, ExtratoResponse.class);
    }

    public List<ExtratoResponse> toList(List<Extrato> extratos){
        return extratos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
