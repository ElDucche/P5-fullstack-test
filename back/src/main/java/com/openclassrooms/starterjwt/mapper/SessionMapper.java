package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionMapper extends EntityMapper<SessionDto, Session> {

    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "users", ignore = true)
    Session toEntity(SessionDto sessionDto);

    @Mapping(source = "teacher.id", target = "teacher_id")
    @Mapping(target = "users", ignore = true) 
    SessionDto toDto(Session session);
}
