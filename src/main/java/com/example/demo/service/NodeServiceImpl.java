package com.example.demo.service;

import com.example.demo.dao.NodeDao;
import com.example.demo.model.Node;
import com.example.demo.utils.NodeType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class NodeServiceImpl implements NodeService {

    private final NodeDao dao;
    private Map<String, Node> uniqueIds = new HashMap<>();

    @Autowired
    public NodeServiceImpl(NodeDao dao) {
        this.dao = dao;
    }

    @Transactional
    @Override
    public void createNetwork(Node node) {
        generateNetwork(node);
        log.info("Network " + node.getId() + " created.");
    }

    private void generateNetwork(Node node) {
        Optional<Node> testNode = dao.findById(node.getId());
        if (testNode.isPresent()) {
            log.error("Tried to create network with id that already exists : " + node.getId());
        } else {
            List<Node> entries = node.getChildren();
            if (entries != null) {
                for (Node entry : entries) {
                    generateNetwork(entry);
                }
            }
            dao.save(node);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> getNetworks() {
        log.info("All networks were called.");
        return dao.findAllByTypeOrderById(NodeType.NETWORK);
    }

    public Node getNode(String nodeId) {
        Optional<Node> optNode = dao.findById(nodeId);
        if (optNode.isPresent()) {
            return optNode.get();
        } else {
            log.error("Node with id: " + nodeId + " was not found!");
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Node> getNetworkChildren(String parentId) {
        log.info("Children of " + parentId + " were called.");
        return getNode(parentId).getChildren();
    }

    @Override
    @Transactional
    public void deleteNode(String id) {
        Node node = getNode(id);
        if (node == null) {
            log.error("Node with such id does not exist: " + id);
            return;
        }
        List<Node> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Node child : children) {
                deleteNode(child.getId());
            }
        }
        log.info("Node " + id + " was deleted");
        dao.delete(node);
    }

    @Override
    @Transactional
    public void updateNode(Node newNode, String id) {
        Node nodeToSave = getNode(id);
        nodeToSave.setDescription(newNode.getDescription());
        nodeToSave.setName(newNode.getName());
        nodeToSave.setParams(newNode.getParams());
        nodeToSave.setType(newNode.getType());
        dao.save(nodeToSave);
    }

    @Override
    @Transactional
    public void updateNetwork(Node node, String id) {
        log.info("Network " + id + " is updating...");
        updateWholeNetwork(node, id);
    }

    private void updateWholeNetwork(Node node, String id) {
        Node parentNode = getNode(id);
        List<Node> entries = node.getChildren();
        if (entries != null) {
            for (Node entry : entries) {
                updateWholeNetwork(entry, id);
            }
        }
        if (!parentNode.equals(node)) {
            updateNode(node, id);
        }
    }

    @Override
    @Transactional
    public void addChildren(String id, List<Node> children) {
        Node parent = getNode(id);
        if (parent.getType() == NodeType.RESOURCE) {
            log.error("Tried to add children to RESOURCE node: " + id);
        }
        List<Node> entries = parent.getChildren();
        if (entries != null) {
            entries.addAll(children);
            parent.setChildren(entries);
        } else {
            parent.setChildren(children);
        }
        log.info("Children were added to " + parent.getId() + " node.");
        generateNetwork(parent);
    }

    @Override
    @Transactional
    public boolean validateNetwork(String id) {
        Node network = getNode(id);
        log.info(id + " network validating...");
        if (!isIdsUnique(network) || !isHierarchyValid(network)) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void deleteNetwork(String id) {
        Node node = getNode(id);
        if (node.getType().equals(NodeType.NETWORK)) {
            log.info("Node deleted: " + id);
            deleteNode(id);
        } else {
            log.error("Tried to delete non-network node via network link: " + id);
        }
    }

    private boolean isHierarchyValid(Node node) {
        @NonNull NodeType type = node.getType();
        NodeType childType = getNextType(type);
        List<Node> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Node child : children) {
                if (!child.getType().equals(childType)) {
                    log.info("Validation failed. Hierarchy in child node: " + child.getId() + " " +
                            "is not valid");
                    return false;
                }
                isHierarchyValid(child);
            }
        }
        return true;
    }

    private NodeType getNextType(NodeType type) {
        int index = type.ordinal();
        int nextIndex = index + 1;
        NodeType[] types = NodeType.values();
        nextIndex %= types.length;
        return types[nextIndex];
    }

    private boolean isIdsUnique(Node node) {
        String id = node.getId();
        if (uniqueIds.containsKey(id)) {
            log.info("Validation failed. Id: " + id + " is not unique.");
            return false;
        } else {
            uniqueIds.put(id, node);
        }
        List<Node> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Node child : children) {
                isIdsUnique(child);
            }
        }
        return true;
    }

}
