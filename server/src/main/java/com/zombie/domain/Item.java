package com.zombie.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by mvurts on 27.04.2018
 */
@Entity
@Table(name = "t_item")
public class Item {

    @Id
    @Column(name = "id")
    @Getter @Setter private Long id;

    @Column(name = "name")
    @Getter @Setter private String name;

    @Column(name = "mesh_file")
    @Getter @Setter private String meshFile;

    @Column(name = "mesh_file_drop")
    @Getter @Setter private String meshFileDrop;

    @Column(name = "material")
    @Getter @Setter private String material;

    @Column(name = "weight")
    @Getter @Setter private Double weight;

    @Transient
    public Item clone() {
        Item item = new Item();
        item.setMeshFile(this.meshFile);
        item.setMeshFileDrop(this.meshFileDrop);
        item.setMaterial(this.material);
        item.setWeight(this.weight);
        return item;
    }

}
