package com.example.internintelligence_movieapidevelopment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponseDto {
    private String name;
    private List<MovieOverviewDto> movies;//page
}