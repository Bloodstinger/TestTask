package com.example.demo.controller;

import com.example.demo.model.Node;
import com.example.demo.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network")
public class NodeController {

    private final NodeService service;

    @Autowired
    public NodeController(NodeService service) {
        this.service = service;
    }

    /**
     * Creates network of any size and hierarchy, the id should be unique and type should be
     *  {@link com.example.demo.utils.NodeType#NETWORK}.
     *
     * @param node
     *        Node with {@link com.example.demo.utils.NodeType#NETWORK} type.
     */
    @PostMapping
    public void createNetwork(@RequestBody Node node) {
        service.createNetwork(node);
    }

    /**
     * Returns all networks.
     *
     * @return
     *      All nodes with type {@link com.example.demo.utils.NodeType#NETWORK}.
     */
    @GetMapping
    public List<Node> getNetworks() {
        return service.getNetworks();
    }

    /**
     * Returns node with id, that was given as path variable.
     * @param id
     *        Node id.
     * @return
     *        Node with given id or null.
     */
    @GetMapping(path = "/{id}")
    public Node getNode(@PathVariable String id) {
        return service.getNode(id);
    }

    /**
     *  Returns list with all children of the node.
     * @param id
     *        Parent node id.
     * @return
     *        List of all children.
     */
    @GetMapping(path = "/{id}/entries")
    public List<Node> getNodeChildren(@PathVariable String id) {
        return service.getNetworkChildren(id);
    }

    /**
     * Removes node and all it's children (if any) from the network.
     * @param id
     *        Node id.
     */
    @DeleteMapping(path = "/{id}")
    public void deleteNetwork(@PathVariable String id) {
        service.deleteNetwork(id);
    }

    /**
     * Updates whole network (does not adds new children)
     * @param node
     *        Parent node with type {@link com.example.demo.utils.NodeType#NETWORK} and all
     *        updated children.
     * @param id
     *        Parent node id.
     */
    @PutMapping(path = "/{id}")
    public void updateNetwork(@RequestBody Node node, @PathVariable String id) {
        service.updateNetwork(node, id);
    }

    /**
     *  Adds children node to the parent.
     * @param id
     *        Parent node id
     * @param children
     *        List of children to add.
     */
    @PutMapping(path = "/{id}/add-node")
    public void addChildren(@PathVariable String id, @RequestBody List<Node> children) {
        service.addChildren(id, children);
    }

    /**
     * Updates single node. Children and id cannot be updated.
     * @param node
     *        Modified node.
     * @param id
     *        Id of the original parent node.
     */
    @PutMapping(path = "/{id}/entry")
    public void updateNode(@RequestBody Node node, @PathVariable String id) {
        service.updateNode(node, id);
    }

    /**
     *  Removes child node from the parent.
     * @param id
     *        Child node id.
     */
    @DeleteMapping(path = "/{id}/entry")
    public void deleteNode(@PathVariable String id) {
        service.deleteNode(id);
    }

    /**
     * Verifies that network is in consistent state (hierarchy is valid, no id's are duplicated).
     * @param id
     *        Id of the network
     * @return
     *        Boolean value - (true - valid, false - non-valid).
     */
    @GetMapping(path = "/{id}/verify")
    public boolean verifyNetwork(@PathVariable String id) {
        return service.validateNetwork(id);
    }
}
