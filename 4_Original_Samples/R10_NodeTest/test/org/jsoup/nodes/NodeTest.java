package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class NodeTest {
    @Test
    public void testNodeName() {
        Node node = new TestNode();
        Assert.assertEquals("TestNode", node.nodeName());
    }
    
    @Test
    public void testAttr() {
        Node node = new TestNode();
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        
        Assert.assertEquals("value1", node.attr("key1"));
        Assert.assertEquals("value2", node.attr("key2"));
        Assert.assertEquals("", node.attr("key3"));
    }
    
    @Test
    public void testHasAttr() {
        Node node = new TestNode();
        node.attr("key1", "value1");
        
        Assert.assertTrue(node.hasAttr("key1"));
        Assert.assertFalse(node.hasAttr("key2"));
    }
    
    @Test
    public void testRemoveAttr() {
        Node node = new TestNode();
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        
        node.removeAttr("key1");
        Assert.assertFalse(node.hasAttr("key1"));
        Assert.assertTrue(node.hasAttr("key2"));
    }
    
    @Test
    public void testBaseUri() {
        Node node = new TestNode();
        node.setBaseUri("http://example.com");
        
        Assert.assertEquals("http://example.com", node.baseUri());
    }
    
    @Test
    public void testAbsUrl() {
        Node node = new TestNode();
        node.setBaseUri("http://example.com");
        node.attr("href", "/page.html");
        
        Assert.assertEquals("http://example.com/page.html", node.absUrl("href"));
        Assert.assertEquals("", node.absUrl("invalidAttr"));
    }
    
    @Test
    public void testChildNode() {
        Node node = new TestNode();
        Node childNode1 = new TestNode();
        Node childNode2 = new TestNode();
        
        node.addChildren(childNode1, childNode2);
        
        Assert.assertEquals(childNode1, node.childNode(0));
        Assert.assertEquals(childNode2, node.childNode(1));
    }
    
    @Test
    public void testChildNodes() {
        Node node = new TestNode();
        Node childNode1 = new TestNode();
        Node childNode2 = new TestNode();
        
        node.addChildren(childNode1, childNode2);
        
        Assert.assertEquals(2, node.childNodes().size());
    }
    
    @Test
    public void testParent() {
        Node parentNode = new TestNode();
        Node childNode = new TestNode();
        parentNode.addChildren(childNode);
        
        Assert.assertEquals(parentNode, childNode.parent());
    }
    
    @Test
    public void testRemove() {
        Node parentNode = new TestNode();
        Node childNode = new TestNode();
        parentNode.addChildren(childNode);
        
        childNode.remove();
        Assert.assertEquals(0, parentNode.childNodes().size());
        Assert.assertNull(childNode.parent());
    }
    
    @Test
    public void testReplaceWith() {
        Node parentNode = new TestNode();
        Node childNode1 = new TestNode();
        Node childNode2 = new TestNode();
        parentNode.addChildren(childNode1);
        
        childNode1.replaceWith(childNode2);
        Assert.assertEquals(1, parentNode.childNodes().size());
        Assert.assertNull(childNode1.parent());
        Assert.assertEquals(parentNode, childNode2.parent());
        Assert.assertEquals(childNode2, parentNode.childNode(0));
    }
    
    private static class TestNode extends Node {
        public TestNode() {
            super("http://example.com");
        }
        
        public String nodeName() {
            return "TestNode";
        }

        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        }
    
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
        }
    }
}
