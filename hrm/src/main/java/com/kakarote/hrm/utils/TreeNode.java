package com.kakarote.hrm.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点，支持Ext、zTree等Web控件
 *
 * @param <T> 树节点的绑定数据类
 */
public class TreeNode<T> {
    /**
     * 树节点id
     * 为了兼容多种情况，使用String类型
     */
    private String id;

    /**
     * 树节点上级id
     */
    private String parentId;

    /**
     * 树节点显示文本
     */
    private String text;

    private String code;

    private String key;

    private Integer level;


    /**
     * 树节点名称，内容和text一样
     * 该字段主要是为了兼容Ext和zTree
     */
    private String name;

    /**
     * 是否为叶子节点
     */
    private Boolean leaf = true;
    private Boolean expanded = false;
    private T nodeData;

    /**
     * 是否为父节点，该字段和leaf重复，主要是为了兼容Ext和zTree
     */
    private Boolean isParent = false;

    /**
     * 子节点，如果没有子节点，则列表长度为0
     */
    private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.name = text;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.text = name;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
        this.isParent = !leaf;
    }

    public Boolean getLeaf() {
        return this.leaf;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
        this.leaf = !isParent;
    }

    public T getNodeData() {
        return nodeData;
    }

    public void setNodeData(T nodeData) {
        this.nodeData = nodeData;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 把树节点列表构造成树，最后返回树的根节点，如果传入的列表有多个根节点，会动态创建一个根节点。
     *
     * @param nodes 树节点列表
     * @return 根节点
     */
    public static <T> TreeNode<T> buildTree(List<TreeNode<T>> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return null;
        }

        if (nodes.size() == 1) {
            TreeNode<T> root;
            root = new TreeNode<T>();
            root.setLeaf(false);
            root.setId("-1");
            root.setName("root");
            root.setParentId("");
            root.getChildren().addAll(nodes);
            return root;
        }

        //用来存放nodes里面的顶级树节点
        //也就是把没有父节点的节点都放到tops里面去
        List<TreeNode<T>> tops = new ArrayList<TreeNode<T>>();

        boolean hasParent = false;
        //第一次遍历，获取一个节点作为子节点
        for (TreeNode<T> child : nodes) {
            hasParent = false;

            //当前节点child的父节点id
            String pid = child.getParentId();

            //如果pid不存在或为空
            //则当前节点为顶级节点
            if (pid == null || pid.equals("")) {
                //把当前节点添加到tops中作为顶级节点
                tops.add(child);
                //跳过当前节点，进入下一轮
                continue;
            }

            //遍历nodes上的所有节点，判断是否有child的父节点
            for (TreeNode<T> parent : nodes) {
                String id = parent.getId();

                //如果parent节点的id等于child节点的pid，则parent节点是child节点的父节点
                if (id != null && id.equals(pid)) {

                    //把child加到parent下
                    parent.getChildren().add(child);
                    parent.setLeaf(false);

                    //child节点有父节点
                    hasParent = true;

                    continue;
                }
            }

            //如果child节点没有父节点，则child是顶级节点
            //把child添加到tops中
            if (!hasParent) {
                tops.add(child);
            }
        }

        TreeNode<T> root;
        if (tops.size() == 1) {
            //如果顶级节点只有一个，该顶级节点是根节点
            root = tops.get(0);
        } else {
            //如果顶级节点有多个，创建一个根节点，把顶级节点放到根节点下
            root = new TreeNode<T>();
            root.setLeaf(false);
            root.setId("-1");
            root.setName("root");
            root.setParentId("");

            root.getChildren().addAll(tops);
        }

        return root;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
