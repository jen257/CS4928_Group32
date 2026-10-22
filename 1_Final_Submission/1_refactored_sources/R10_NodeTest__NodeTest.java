package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class NodeTest {
    @Test
    public void testNode() {
        Node node = new TestNode();
        Assert.assertEquals("Expected nodeName() to return 'TestNode'", "TestNode", node.nodeName());
    
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        
        Assert.assertEquals("Expected attr('key1') to return 'value1'", "value1", node.attr("key1"));
        Assert.assertEquals("Expected attr('key2') to return 'value2'", "value2", node.attr("key2"));
        Assert.assertEquals("Expected attr('key3') for non-existent attribute to return empty string", "", node.attr("key3"));
    
        node = new TestNode();
        node.attr("key1", "value1");
        
        Assert.assertTrue("Expected hasAttr('key1') to return true for existing attribute", node.hasAttr("key1"));
        Assert.assertFalse("Expected hasAttr('key2') to return false for non-existing attribute", node.hasAttr("key2"));
    
        node = new TestNode();
        node.attr("key1", "value1");
        node.attr("key2", "value2");
        
        node.removeAttr("key1");
        Assert.assertFalse("Expected hasAttr('key1') to return false after removeAttr()", node.hasAttr("key1"));
        Assert.assertTrue("Expected hasAttr('key2') to remain true after removing key1", node.hasAttr("key2"));
    
        node = new TestNode();
        node.setBaseUri("http://example.com");
        
        Assert.assertEquals("Expected baseUri() to match configured base URI", "http://example.com", node.baseUri());
    
        node = new TestNode();
        node.setBaseUri("http://example.com");
        node.attr("href", "/page.html");
        
        Assert.assertEquals("Expected absUrl('href') to resolve relative URL correctly", "http://example.com/page.html", node.absUrl("href"));
        Assert.assertEquals("Expected absUrl('invalidAttr') for missing attribute to return empty string", "", node.absUrl("invalidAttr"));
    
        node = new TestNode();
        Node childNode1 = new TestNode();
        Node childNode2 = new TestNode();
        
        node.addChildren(childNode1, childNode2);
        
        Assert.assertEquals("Expected childNode(0) to return childNode1", childNode1, node.childNode(0));
        Assert.assertEquals("Expected childNode(1) to return childNode2", childNode2, node.childNode(1));
    
        node = new TestNode();
        Node childNode3 = new TestNode();
        Node childNode4 = new TestNode();
        
        node.addChildren(childNode3, childNode4);
        
        Assert.assertEquals("Expected childNodes().size() to be 2", 2, node.childNodes().size());
    
        Node parentNode = new TestNode();
        Node childNode = new TestNode();
        parentNode.addChildren(childNode);
        
        Assert.assertEquals("Expected childNode.parent() to return parentNode", parentNode, childNode.parent());
    
        parentNode = new TestNode();
        childNode = new TestNode();
        parentNode.addChildren(childNode);
        
        childNode.remove();
        Assert.assertEquals("Expected parentNode childNodes size to be 0 after child removal", 0, parentNode.childNodes().size());
        Assert.assertNull("Expected removed childNode parent to be null", childNode.parent());
    
        parentNode = new TestNode();
        Node childNode1_rep = new TestNode();
        Node childNode2_rep = new TestNode();
        parentNode.addChildren(childNode1_rep);
        
        childNode1_rep.replaceWith(childNode2_rep);
        Assert.assertEquals("Expected parentNode childNodes size to be 1 after replacement", 1, parentNode.childNodes().size());
        Assert.assertNull("Expected replaced childNode1 parent to be null", childNode1_rep.parent());
        Assert.assertEquals("Expected replacing childNode2 parent to be parentNode", parentNode, childNode2_rep.parent());
        Assert.assertEquals("Expected parentNode childNode(0) to be childNode2", childNode2_rep, parentNode.childNode(0));
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
