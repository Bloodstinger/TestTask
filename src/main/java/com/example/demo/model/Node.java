package com.example.demo.model;

import com.example.demo.utils.NodeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@javax.persistence.Entity
@Table(name = "nodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "type")
    @NonNull
    private NodeType type;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name = "param_mapping")
    @Column(name = "params")
    private Map<String, String> params;

    @Column(name = "children")
    @ElementCollection(targetClass = Node.class, fetch = FetchType.EAGER)
    private List<Node> children;

}
