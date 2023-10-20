package com.example.market.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Lazy loading is a hint to the persistence layer
    @JoinColumn(name = "user_id", nullable = false) // Foreign key for "user"
    private User user;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String status; // Keeping status as a String for flexibility

    // Status values
    public static final String STATUS_ORDERED = "ORDERED";
    public static final String STATUS_ING = "ING";
    public static final String STATUS_CANCELLED = "CANCELLED";


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    // Other standard fields, constructors, getters and setters, and utility methods
}
