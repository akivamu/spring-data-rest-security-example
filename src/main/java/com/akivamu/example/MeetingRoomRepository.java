package com.akivamu.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface MeetingRoomRepository extends CrudRepository<MeetingRoom, Long> {
}
