package com.ykh.backend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongArrayConverter implements AttributeConverter<List<Long>, String> {
    private static final String SPLIT_CHAR = ",";

    // List<Long> 타입의 객체를 String으로 변환하여 데이터베이스에 저장하기 위한 메소드
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null) {
            return null;
        }
        // List의 각 원소를 문자열로 변환 후 쉼표로 연결하여 반환
        return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
    }

    // 데이터베이스에서 읽어온 String 타입의 데이터를 List<Long>으로 변환하는 메소드
    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null; // 또는 비어있는 List<Long> 객체를 반환할 수도 있습니다.
        }

        // 쉼표를 기준으로 문자열을 분리하고, 각 문자열을 Long 타입으로 변환하여 리스트로 반환
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}

