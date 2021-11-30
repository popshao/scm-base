package com.gangling.scm.base.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Autowired
 * ApplicationEventPublisher publisher;
 * // 发送事件
 * public void sendEvent() {
 *    publisher.publishEvent(new EventDTO(this, new EventData()));
 * }
 * // 监听事件
 * @EventListener
 * public void listenEvent(EventDTO eventDTO) {
 *     System.out.println(Thread.currentThread().getName() + " " + eventDTO.getEventData());
 * }
 */
public abstract class BaseEvent<T> extends ApplicationEvent {

    /**
     * 该类型事件携带的信息
     */
    private T eventData;

    /**
     *
     * @param source 最初触发该事件的对象
     * @param eventData 该类型事件携带的信息
     */
    public BaseEvent(Object source, T eventData) {
        super(source);
        this.eventData = eventData;
    }

    public T getEventData() {
        return eventData;
    }
}
