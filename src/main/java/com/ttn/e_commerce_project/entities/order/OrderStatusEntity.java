package com.ttn.e_commerce_project.entities.order;

import com.ttn.e_commerce_project.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Order_Status")
public class OrderStatusEntity {

    @Id
    @OneToOne
    @JoinColumn(name = "Order_Product_id")
    OrderProduct orderProduct;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status")
    OrderStatus toStatus;

    @Column(name = "transition_notes_comments")
    String transitionNotesComments;

    @Column(name = "transition_date")
    LocalDateTime transitionDate;
}


