package ru.impathy.repository;

import ru.impathy.domain.entities.orders.CustomCarOrder;
import ru.impathy.domain.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomCarOrderRepository {
    CustomCarOrder createByAdmin(User actor, CustomCarOrder order);

    Optional<CustomCarOrder> readByIdByAdmin(User actor, UUID id);

    CustomCarOrder updateByAdmin(User actor, UUID id, CustomCarOrder order);

    void deleteByIdByAdmin(User actor, UUID id);

    boolean validateOrderByAdmin(User actor, CustomCarOrder order);

    CustomCarOrder saveByClient(User actor, CustomCarOrder order);

    CustomCarOrder saveByManager(User actor, CustomCarOrder order);

    CustomCarOrder saveByAdmin(User actor, CustomCarOrder order);

    Optional<CustomCarOrder> findByIdForManager(User actor, UUID id);

    Optional<CustomCarOrder> findByIdForAdmin(User actor, UUID id);

    List<CustomCarOrder> findAllForManager(User actor);

    List<CustomCarOrder> findAllForAdmin(User actor);
}
