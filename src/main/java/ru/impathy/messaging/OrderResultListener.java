package ru.impathy.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.impathy.application.OrderApplicationService;

@Component
@Profile("order & !test")
public class OrderResultListener {
    private final OrderApplicationService orderApplicationService;
    private final ProcessedMessageService processedMessageService;

    public OrderResultListener(OrderApplicationService orderApplicationService,
                               ProcessedMessageService processedMessageService) {
        this.orderApplicationService = orderApplicationService;
        this.processedMessageService = processedMessageService;
    }

    @RabbitListener(queues = "impathy.order.result.queue")
    public void handle(OrderApprovalResultEvent event) {
        String messageId = event.getEventId() == null ? null : event.getEventId().toString();
        if (messageId != null && processedMessageService.alreadyProcessed(messageId)) {
            return;
        }
        if (event.getOrderType() == OrderType.IN_STOCK) {
            orderApplicationService.applyInStockApproval(event.getOrderId(), event.isApproved());
        } else {
            orderApplicationService.applyCustomApproval(event.getOrderId(), event.isApproved());
        }
        if (messageId != null) {
            processedMessageService.markProcessed(messageId, "ORDER_RESULT");
        }
    }
}
