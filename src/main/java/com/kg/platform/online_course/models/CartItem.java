package com.kg.platform.online_course.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
@Builder

@NamedEntityGraph(
        name = "cart-item-entity-graph-with-cart",
        attributeNodes = {
                @NamedAttributeNode(value = "cart",subgraph = "cart-subgraph")
        },
        subgraphs = {

                @NamedSubgraph(
                        name = "cart-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("user")
                        }
                )

        }
)
public class CartItem {
    private static final String SEQ_NAME = "cart_item_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1, initialValue = 4)
    private Long id;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Cart cart;
    public Double evalTotalPrice() {
        return getCourse().getPrice() * quantity;
    }

}
