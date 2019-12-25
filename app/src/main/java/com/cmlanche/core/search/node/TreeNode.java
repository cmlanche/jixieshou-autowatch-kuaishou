package com.cmlanche.core.search.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmlanche on 2018/8/14.
 * <p>
 * 自定义的控件树结构
 */
public class TreeNode<T> {

    public enum RelationShip {
        PARENT,
        BROTHER,
        CHILD,
        IGNORED,
        REPLACE,
        UNKNOW
    }

    public interface Detector<T> {
        /**
         * 获取当前节点与新节点的关系
         *
         * @param currNode 新添加的节点
         * @param newData  当前节点
         * @return
         */
        RelationShip getRelationship(TreeNode<T> currNode, T newData);
    }

    /**
     * 节点的数据
     */
    private T data;

    /**
     * 父节点
     */
    private TreeNode<T> parent;

    /**
     * 子节点
     */
    private List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
    }

    /**
     * 添加到控件树
     *
     * @param t
     * @return
     */
    public boolean addToTree(T t, Detector<T> detector) {
        if (data == null) {
            data = t;
            return true;
        } else {
            RelationShip relationShip = detector.getRelationship(this, t);
            switch (relationShip) {
                case PARENT:
                    TreeNode<T> newNode = new TreeNode<T>(t);
                    this.parent = newNode;
                    newNode.children = new ArrayList<TreeNode<T>>();
                    newNode.children.add(this);
                    return true;
                case IGNORED:
//                    Logger.e("same, ignored");
                    break;
                case BROTHER:
                    if (this.parent != null) {
                        if (this.parent.children == null) {
                            this.parent.children = new ArrayList<TreeNode<T>>();
                        }

                        // 兄弟之间可能有重复的，需要去重
                        for (TreeNode<T> c : this.parent.children) {
                            RelationShip ship = detector.getRelationship(c, t);
                            switch (ship) {
                                case IGNORED:
                                    return false;
                                case REPLACE:
                                    c.data = t;
                                    return true;
                            }
                        }

                        // 确认为不重复的兄弟节点，增加兄弟节点
                        TreeNode<T> child = new TreeNode<T>(t);
                        child.parent = this.parent;
                        this.parent.children.add(child);
                    }
                    return true;
                case REPLACE:
                    this.data = t;
                    return true;
                case CHILD:
                    if (this.children == null) {
                        this.children = new ArrayList<TreeNode<T>>();
                    }
                    TreeNode<T> child = new TreeNode<T>(t);
                    child.parent = this;
                    this.children.add(child);
                    return true;
                default:
                    if (children != null) {
                        for (TreeNode<T> node : children) {
                            if (node.addToTree(t, detector)) {
                                return true;
                            }
                        }
//                        Logger.e("could not happened, check2!");
                    } else {
                        // 如果走到这里说明子节点
//                        Logger.e("could not happened, check!");
                    }
            }
        }
        return false;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

}