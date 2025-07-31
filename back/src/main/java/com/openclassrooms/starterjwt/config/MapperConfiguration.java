package com.openclassrooms.starterjwt.config;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public SessionMapper sessionMapper() {
        return Mappers.getMapper(SessionMapper.class);
    }

    @Bean
    public TeacherMapper teacherMapper() {
        return Mappers.getMapper(TeacherMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}
