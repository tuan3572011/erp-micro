package com.erp.rest.comment;

import com.erp.rest.base.IEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder(toBuilder = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Comment implements IEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Version
    private Long version;

}
