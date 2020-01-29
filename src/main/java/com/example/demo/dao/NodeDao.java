package com.example.demo.dao;

import com.example.demo.model.Node;
import com.example.demo.utils.NodeType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeDao extends CrudRepository<Node, String > {

    List<Node> findAllByTypeOrderById(NodeType identifier);
}
