package com.example.demo.service;

import com.example.demo.model.Node;

import java.util.List;

public interface NodeService {

    void createNetwork(Node node);

    List<Node> getNetworks();

    Node getNode(String nodeId);

    List<Node> getNetworkChildren(String parentId);

    void deleteNode(String node);

    void updateNode(Node newNode, String id);

    void updateNetwork(Node node, String id);

    void addChildren(String id, List<Node> children);

    boolean validateNetwork(String node);

    void deleteNetwork(String id);
}
