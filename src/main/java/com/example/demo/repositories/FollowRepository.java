package com.example.demo.repositories;

import com.example.demo.entities.Follow;
import com.example.demo.entities.User;

import java.util.List;

public interface FollowRepository extends AbstractRepository<Follow, Integer> {
    List<Follow> findFollowByFollowedByUserAndTargetUser(User followedByUser, User targetUser);

    List<Follow> findAllByTargetUser(User user);

    List<Follow> findAllByFollowedByUser(User user);
}
