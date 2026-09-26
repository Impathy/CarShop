package ru.impathy.repository;

import ru.impathy.domain.entities.orders.InStockCarOrder;
import ru.impathy.domain.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InStockCarOrderRepository {
    InStockCarOrder createByAdmin(User actor, InStockCarOrder order);

    Optional<InStockCarOrder> readByIdByAdmin(User actor, UUID id);

    InStockCarOrder updateByAdmin(User actor, UUID id, InStockCarOrder order);

    void deleteByIdByAdmin(User actor, UUID id);

    boolean validateOrderByAdmin(User actor, InStockCarOrder order);

    InStockCarOrder saveByClient(User actor, InStockCarOrder order);

    InStockCarOrder saveByManager(User actor, InStockCarOrder order);

    InStockCarOrder saveByAdmin(User actor, InStockCarOrder order);

    Optional<InStockCarOrder> findByIdForManager(User actor, UUID id);

    Optional<InStockCarOrder> findByIdForAdmin(User actor, UUID id);

    List<InStockCarOrder> findAllForManager(User actor);

    List<InStockCarOrder> findAllForAdmin(User actor);
}
