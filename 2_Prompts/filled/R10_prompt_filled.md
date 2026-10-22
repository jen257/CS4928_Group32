# Master Refactoring Prompt (RQ2)

Paste this prompt to the AI, with `R10`, `7`, `30`, `106`,
`package org.jsoup.nodes;

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
}` and `package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 The base, abstract Node model. Elements, Documents, Comments etc are all Node instances.

 @author Jonathan Hedley, jonathan@hedley.net */
public abstract class Node implements Cloneable {
    Node parentNode;
    List<Node> childNodes;
    Attributes attributes;
    String baseUri;
    int siblingIndex;

    /**
     Create a new Node.
     @param baseUri base URI
     @param attributes attributes (not null, but may be empty)
     */
    protected Node(String baseUri, Attributes attributes) {
        Validate.notNull(baseUri);
        Validate.notNull(attributes);
        
        childNodes = new ArrayList<Node>(4);
        this.baseUri = baseUri.trim();
        this.attributes = attributes;
    }

    protected Node(String baseUri) {
        this(baseUri, new Attributes());
    }

    /**
     * Default constructor. Doesn't setup base uri, children, or attributes; use with caution.
     */
    protected Node() {
        childNodes = Collections.EMPTY_LIST;
        attributes = null;
    }

    /**
     Get the node name of this node. Use for debugging purposes and not logic switching (for that, use instanceof).
     @return node name
     */
    public abstract String nodeName();

    /**
     * Get an attribute's value by its key.
     * <p/>
     * To get an absolute URL from an attribute that may be a relative URL, prefix the key with <code><b>abs</b></code>,
     * which is a shortcut to the {@link #absUrl} method.
     * E.g.: <blockquote><code>String url = a.attr("abs:href");</code></blockquote>
     * @param attributeKey The attribute key.
     * @return The attribute, or empty string if not present (to avoid nulls).
     * @see #attributes()
     * @see #hasAttr(String)
     * @see #absUrl(String)
     */
    public String attr(String attributeKey) {
        Validate.notNull(attributeKey);

        if (hasAttr(attributeKey))
            return attributes.get(attributeKey);
        else if (attributeKey.toLowerCase().startsWith("abs:"))
            return absUrl(attributeKey.substring("abs:".length()));
        else return "";
    }

    /**
     * Get all of the element's attributes.
     * @return attributes (which implements iterable, in same order as presented in original HTML).
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Set an attribute (key=value). If the attribute already exists, it is replaced.
     * @param attributeKey The attribute key.
     * @param attributeValue The attribute value.
     * @return this (for chaining)
     */
    public Node attr(String attributeKey, String attributeValue) {
        attributes.put(attributeKey, attributeValue);
        return this;
    }

    /**
     * Test if this element has an attribute.
     * @param attributeKey The attribute key to check.
     * @return true if the attribute exists, false if not.
     */
    public boolean hasAttr(String attributeKey) {
        Validate.notNull(attributeKey);
        return attributes.hasKey(attributeKey);
    }

    /**
     * Remove an attribute from this element.
     * @param attributeKey The attribute to remove.
     * @return this (for chaining)
     */
    public Node removeAttr(String attributeKey) {
        Validate.notNull(attributeKey);
        attributes.remove(attributeKey);
        return this;
    }

    /**
     Get the base URI of this node.
     @return base URI
     */
    public String baseUri() {
        return baseUri;
    }

    /**
     Update the base URI of this node.
     @param baseUri base URI to set
     */
    public void setBaseUri(String baseUri) {
        Validate.notNull(baseUri);
        this.baseUri = baseUri;
    }

    /**
     * Get an absolute URL from a URL attribute that may be relative (i.e. an <code>&lt;a href></code> or
     * <code>&lt;img src></code>).
     * <p/>
     * E.g.: <code>String absUrl = linkEl.absUrl("href");</code>
     * <p/>
     * If the attribute value is already absolute (i.e. it starts with a protocol, like
     * <code>http://</code> or <code>https://</code> etc), and it successfully parses as a URL, the attribute is
     * returned directly. Otherwise, it is treated as a URL relative to the element's {@link #baseUri}, and made
     * absolute using that.
     * <p/>
     * As an alternate, you can use the {@link #attr} method with the <code>abs:</code> prefix, e.g.:
     * <code>String absUrl = linkEl.attr("abs:href");</code>
     *
     * @param attributeKey The attribute key
     * @return An absolute URL if one could be made, or an empty string (not null) if the attribute was missing or
     * could not be made successfully into a URL.
     * @see #attr
     * @see java.net.URL#URL(java.net.URL, String)
     */
    public String absUrl(String attributeKey) {
        Validate.notEmpty(attributeKey);

        String relUrl = attr(attributeKey);
        if (!hasAttr(attributeKey)) {
            return ""; // nothing to make absolute with
        } else {
            URL base;
            try {
                try {
                    base = new URL(baseUri);
                } catch (MalformedURLException e) {
                    // the base is unsuitable, but the attribute may be abs on its own, so try that
                    URL abs = new URL(relUrl);
                    return abs.toExternalForm();
                }
                // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
                URL abs = new URL(base, relUrl);
                return abs.toExternalForm();
            } catch (MalformedURLException e) {
                return "";
            }
        }
    }

    /**
     Get a child node by index
     @param index index of child node
     @return the child node at this index.
     */
    public Node childNode(int index) {
        return childNodes.get(index);
    }

    /**
     Get this node's children. Presented as an unmodifiable list: new children can not be added, but the child nodes
     themselves can be manipulated.
     @return list of children. If no children, returns an empty list.
     */
    public List<Node> childNodes() {
        return Collections.unmodifiableList(childNodes);
    }
    
    protected Node[] childNodesAsArray() {
        return childNodes.toArray(new Node[childNodes().size()]);
    }

    /**
     Gets this node's parent node.
     @return parent node; or null if no parent.
     */
    public Node parent() {
        return parentNode;
    }
    
    /**
     * Gets the Document associated with this Node. 
     * @return the Document associated with this Node, or null if there is no such Document.
     */
    public Document ownerDocument() {
        if (this instanceof Document)
            return (Document) this;
        else if (parentNode == null)
            return null;
        else
            return parentNode.ownerDocument();
    }
    
    /**
     * Remove (delete) this node from the DOM tree. If this node has children, they are also removed.
     */
    public void remove() {
        Validate.notNull(parentNode);
        parentNode.removeChild(this);
    }
    
    /**
     * Replace this node in the DOM with the supplied node.
     * @param in the node that will will replace the existing node.
     */
    public void replaceWith(Node in) {
        Validate.notNull(in);
        Validate.notNull(parentNode);
        parentNode.replaceChild(this, in);
    }

    protected void setParentNode(Node parentNode) {
        if (this.parentNode != null)
            this.parentNode.removeChild(this);
        this.parentNode = parentNode;
    }

    protected void replaceChild(Node out, Node in) {
        Validate.isTrue(out.parentNode == this);
        Validate.notNull(in);
        if (in.parentNode != null)
            in.parentNode.removeChild(in);
        
        Integer index = out.siblingIndex();
        childNodes.set(index, in);
        in.parentNode = this;
        in.setSiblingIndex(index);
        out.parentNode = null;
    }

    protected void removeChild(Node out) {
        Validate.isTrue(out.parentNode == this);
        int index = out.siblingIndex();
        childNodes.remove(index);
        reindexChildren();
        out.parentNode = null;
    }

    protected void addChildren(Node... children) {
        //most used. short circuit addChildren(int), which hits reindex children and array copy
        for (Node child: children) {
            reparentChild(child);
            childNodes.add(child);
            child.setSiblingIndex(childNodes.size()-1);
        }
    }

    protected void addChildren(int index, Node... children) {
        Validate.noNullElements(children);
        for (int i = children.length - 1; i >= 0; i--) {
            Node in = children[i];
            reparentChild(in);
            childNodes.add(index, in);
        }
        reindexChildren();
    }

    private void reparentChild(Node child) {
        if (child.parentNode != null)
            child.parentNode.removeChild(child);
        child.setParentNode(this);
    }
    
    private void reindexChildren() {
        for (int i = 0; i < childNodes.size(); i++) {
            childNodes.get(i).setSiblingIndex(i);
        }
    }
    
    /**
     Retrieves this node's sibling nodes. Effectively, {@link #childNodes()  node.parent.childNodes()}.
     @return node siblings, including this node
     */
    public List<Node> siblingNodes() {
        return parent().childNodes(); // TODO: should this strip out this node? i.e. not a sibling of self?
    }

    /**
     Get this node's next sibling.
     @return next sibling, or null if this is the last sibling
     */
    public Node nextSibling() {
        if (parentNode == null)
            return null; // root
        
        List<Node> siblings = parentNode.childNodes;
        Integer index = siblingIndex();
        Validate.notNull(index);
        if (siblings.size() > index+1)
            return siblings.get(index+1);
        else
            return null;
    }

    /**
     Get this node's previous sibling.
     @return the previous sibling, or null if this is the first sibling
     */
    public Node previousSibling() {
        List<Node> siblings = parentNode.childNodes;
        Integer index = siblingIndex();
        Validate.notNull(index);
        if (index > 0)
            return siblings.get(index-1);
        else
            return null;
    }

    /**
     * Get the list index of this node in its node sibling list. I.e. if this is the first node
     * sibling, returns 0.
     * @return position in node sibling list
     * @see org.jsoup.nodes.Element#elementSiblingIndex()
     */
    public Integer siblingIndex() {
        return siblingIndex;
    }
    
    protected void setSiblingIndex(int siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

    /**
     Get the outer HTML of this node.
     @return HTML
     */
    public String outerHtml() {
        StringBuilder accum = new StringBuilder(32*1024);
        outerHtml(accum);
        return accum.toString();
    }

    protected void outerHtml(StringBuilder accum) {
        new NodeTraversor(new OuterHtmlVisitor(accum, getOutputSettings())).traverse(this);
    }

    // if this node has no document (or parent), retrieve the default output settings
    private Document.OutputSettings getOutputSettings() {
        return ownerDocument() != null ? ownerDocument().outputSettings() : (new Document("")).outputSettings();
    }

    /**
     Get the outer HTML of this node.
     @param accum accumulator to place HTML into
     */
    abstract void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out);

    abstract void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out);

    public String toString() {
        return outerHtml();
    }

    protected void indent(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append("\n").append(StringUtil.padding(depth * out.indentAmount()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // todo: have nodes hold a child index, compare against that and parent (not children)
        return false;
    }

    @Override
    public int hashCode() {
        int result = parentNode != null ? parentNode.hashCode() : 0;
        // not children, or will block stack as they go back up to parent)
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    /**
     * Create a stand-alone, deep copy of this node, and all of its children. The cloned node will have no siblings or
     * parent node. As a stand-alone object, any changes made to the clone or any of its children will not impact the
     * original node.
     * <p>
     * The cloned node may be adopted into another Document or node structure using {@link Element#appendChild(Node)}.
     * @return stand-alone cloned node
     */
    @Override
    public Node clone() {
        return doClone(null); // splits for orphan
    }

    protected Node doClone(Node parent) {
        Node clone;
        try {
            clone = (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        clone.parentNode = parent; // can be null, to create an orphan split
        clone.siblingIndex = parent == null ? 0 : siblingIndex;
        clone.attributes = attributes != null ? attributes.clone() : null;
        clone.baseUri = baseUri;
        clone.childNodes = new ArrayList<Node>(childNodes.size());
        for (Node child: childNodes)
            clone.childNodes.add(child.doClone(clone)); // clone() creates orphans, doClone() keeps parent

        return clone;
    }

    private static class OuterHtmlVisitor implements NodeVisitor {
        private StringBuilder accum;
        private Document.OutputSettings out;

        OuterHtmlVisitor(StringBuilder accum, Document.OutputSettings out) {
            this.accum = accum;
            this.out = out;
        }

        public void head(Node node, int depth) {
            node.outerHtmlHead(accum, depth, out);
        }

        public void tail(Node node, int depth) {
            if (!node.nodeName().equals("#text")) // saves a void hit.
                node.outerHtmlTail(accum, depth, out);
        }
    }
}` replaced by the sample's actual
values. Keep every line; the "Detector rules" and "Hard constraints" sections
are what stop the model from going in the wrong direction.

---

You are an expert Java test-refactoring assistant. Refactor the JUnit test
class below to eliminate two test smells, Assertion Roulette (AR) and Lazy
Test (LT), as counted by the TsDetect tool. Preserve all original test
behaviour exactly, and produce code that compiles and passes.

## Detector rules (measured, not textbook)

These are the actual counting rules of TsDetect. Follow them literally.

**Assertion Roulette (AR):** a test method containing 2 or more assertion
statements AND at least one assertion without a message parameter is counted
once. Adding a message to EVERY assertion in a method removes that method from
the count. Do not split methods to fix AR.

**Lazy Test (LT):** for each production method M, if M is invoked from 2 or
more DIFFERENT test methods, TsDetect adds M's TOTAL invocation count (calls
inside the same method count multiple times) to the LT score. If M is invoked
from only one test method it contributes 0.

Consequences you MUST follow:

1. Splitting a test method into several smaller methods INCREASES LT, because
   more test methods then share the same production method. Do not split to fix LT.
2. To reduce LT, MERGE test methods that observe the same production method
   (typically toString(), peek(), get(), size(), etc.) so each production method
   is invoked from exactly one test method.

## Procedure

1. List each production method and count how many test methods call it.
   Identify methods called from 2+ test methods - these are the LT source.
2. For AR: add a clear, descriptive failure message to EVERY assertion.
3. For LT: group and merge test methods by the production method they observe.
   After merging, each production method must be invoked from a single test method.
4. A merged method may contain many assertions; that is acceptable, but every
   assertion must carry a message, otherwise that method trips AR.

## Hard constraints (any violation is a failure)

- Do not delete or change ANY assertion: predicate, expected value, comparison,
  or input literal. The total assertion count after refactoring must equal the original.
- Do not delete or change any test input, constructor argument, or string/char literal.
- Do not change expected-exception behaviour or the setup/act/assert order.
- Do not merge two different scenarios' expectations into one assertion.
- Do not move assertions into helper or private methods to hide them from the detector.
- Keep JUnit 4 @Test annotations and imports; no parameterized tests, no new
  dependencies; keep the package and the class name unchanged.
- Do not modify the production class.

## Output format

1. A table: production method -> test methods calling it before -> after.
2. A list of each new test method and which original methods it absorbed.
3. The COMPLETE refactored Java file, from `package` to the final `}`, with no
   `// ...` or truncation.
4. State the assertion count before and after; they must be equal.

If some LT source cannot be merged without changing semantics, say which and
why. If the semantics are ambiguous, say so; do not invent expected values.

## Files

Sample R10 - baseline AR=7, LT=30, LOC=106.

Test class:

```java
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
```

Production class:

```java
package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 The base, abstract Node model. Elements, Documents, Comments etc are all Node instances.

 @author Jonathan Hedley, jonathan@hedley.net */
public abstract class Node implements Cloneable {
    Node parentNode;
    List<Node> childNodes;
    Attributes attributes;
    String baseUri;
    int siblingIndex;

    /**
     Create a new Node.
     @param baseUri base URI
     @param attributes attributes (not null, but may be empty)
     */
    protected Node(String baseUri, Attributes attributes) {
        Validate.notNull(baseUri);
        Validate.notNull(attributes);
        
        childNodes = new ArrayList<Node>(4);
        this.baseUri = baseUri.trim();
        this.attributes = attributes;
    }

    protected Node(String baseUri) {
        this(baseUri, new Attributes());
    }

    /**
     * Default constructor. Doesn't setup base uri, children, or attributes; use with caution.
     */
    protected Node() {
        childNodes = Collections.EMPTY_LIST;
        attributes = null;
    }

    /**
     Get the node name of this node. Use for debugging purposes and not logic switching (for that, use instanceof).
     @return node name
     */
    public abstract String nodeName();

    /**
     * Get an attribute's value by its key.
     * <p/>
     * To get an absolute URL from an attribute that may be a relative URL, prefix the key with <code><b>abs</b></code>,
     * which is a shortcut to the {@link #absUrl} method.
     * E.g.: <blockquote><code>String url = a.attr("abs:href");</code></blockquote>
     * @param attributeKey The attribute key.
     * @return The attribute, or empty string if not present (to avoid nulls).
     * @see #attributes()
     * @see #hasAttr(String)
     * @see #absUrl(String)
     */
    public String attr(String attributeKey) {
        Validate.notNull(attributeKey);

        if (hasAttr(attributeKey))
            return attributes.get(attributeKey);
        else if (attributeKey.toLowerCase().startsWith("abs:"))
            return absUrl(attributeKey.substring("abs:".length()));
        else return "";
    }

    /**
     * Get all of the element's attributes.
     * @return attributes (which implements iterable, in same order as presented in original HTML).
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Set an attribute (key=value). If the attribute already exists, it is replaced.
     * @param attributeKey The attribute key.
     * @param attributeValue The attribute value.
     * @return this (for chaining)
     */
    public Node attr(String attributeKey, String attributeValue) {
        attributes.put(attributeKey, attributeValue);
        return this;
    }

    /**
     * Test if this element has an attribute.
     * @param attributeKey The attribute key to check.
     * @return true if the attribute exists, false if not.
     */
    public boolean hasAttr(String attributeKey) {
        Validate.notNull(attributeKey);
        return attributes.hasKey(attributeKey);
    }

    /**
     * Remove an attribute from this element.
     * @param attributeKey The attribute to remove.
     * @return this (for chaining)
     */
    public Node removeAttr(String attributeKey) {
        Validate.notNull(attributeKey);
        attributes.remove(attributeKey);
        return this;
    }

    /**
     Get the base URI of this node.
     @return base URI
     */
    public String baseUri() {
        return baseUri;
    }

    /**
     Update the base URI of this node.
     @param baseUri base URI to set
     */
    public void setBaseUri(String baseUri) {
        Validate.notNull(baseUri);
        this.baseUri = baseUri;
    }

    /**
     * Get an absolute URL from a URL attribute that may be relative (i.e. an <code>&lt;a href></code> or
     * <code>&lt;img src></code>).
     * <p/>
     * E.g.: <code>String absUrl = linkEl.absUrl("href");</code>
     * <p/>
     * If the attribute value is already absolute (i.e. it starts with a protocol, like
     * <code>http://</code> or <code>https://</code> etc), and it successfully parses as a URL, the attribute is
     * returned directly. Otherwise, it is treated as a URL relative to the element's {@link #baseUri}, and made
     * absolute using that.
     * <p/>
     * As an alternate, you can use the {@link #attr} method with the <code>abs:</code> prefix, e.g.:
     * <code>String absUrl = linkEl.attr("abs:href");</code>
     *
     * @param attributeKey The attribute key
     * @return An absolute URL if one could be made, or an empty string (not null) if the attribute was missing or
     * could not be made successfully into a URL.
     * @see #attr
     * @see java.net.URL#URL(java.net.URL, String)
     */
    public String absUrl(String attributeKey) {
        Validate.notEmpty(attributeKey);

        String relUrl = attr(attributeKey);
        if (!hasAttr(attributeKey)) {
            return ""; // nothing to make absolute with
        } else {
            URL base;
            try {
                try {
                    base = new URL(baseUri);
                } catch (MalformedURLException e) {
                    // the base is unsuitable, but the attribute may be abs on its own, so try that
                    URL abs = new URL(relUrl);
                    return abs.toExternalForm();
                }
                // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
                URL abs = new URL(base, relUrl);
                return abs.toExternalForm();
            } catch (MalformedURLException e) {
                return "";
            }
        }
    }

    /**
     Get a child node by index
     @param index index of child node
     @return the child node at this index.
     */
    public Node childNode(int index) {
        return childNodes.get(index);
    }

    /**
     Get this node's children. Presented as an unmodifiable list: new children can not be added, but the child nodes
     themselves can be manipulated.
     @return list of children. If no children, returns an empty list.
     */
    public List<Node> childNodes() {
        return Collections.unmodifiableList(childNodes);
    }
    
    protected Node[] childNodesAsArray() {
        return childNodes.toArray(new Node[childNodes().size()]);
    }

    /**
     Gets this node's parent node.
     @return parent node; or null if no parent.
     */
    public Node parent() {
        return parentNode;
    }
    
    /**
     * Gets the Document associated with this Node. 
     * @return the Document associated with this Node, or null if there is no such Document.
     */
    public Document ownerDocument() {
        if (this instanceof Document)
            return (Document) this;
        else if (parentNode == null)
            return null;
        else
            return parentNode.ownerDocument();
    }
    
    /**
     * Remove (delete) this node from the DOM tree. If this node has children, they are also removed.
     */
    public void remove() {
        Validate.notNull(parentNode);
        parentNode.removeChild(this);
    }
    
    /**
     * Replace this node in the DOM with the supplied node.
     * @param in the node that will will replace the existing node.
     */
    public void replaceWith(Node in) {
        Validate.notNull(in);
        Validate.notNull(parentNode);
        parentNode.replaceChild(this, in);
    }

    protected void setParentNode(Node parentNode) {
        if (this.parentNode != null)
            this.parentNode.removeChild(this);
        this.parentNode = parentNode;
    }

    protected void replaceChild(Node out, Node in) {
        Validate.isTrue(out.parentNode == this);
        Validate.notNull(in);
        if (in.parentNode != null)
            in.parentNode.removeChild(in);
        
        Integer index = out.siblingIndex();
        childNodes.set(index, in);
        in.parentNode = this;
        in.setSiblingIndex(index);
        out.parentNode = null;
    }

    protected void removeChild(Node out) {
        Validate.isTrue(out.parentNode == this);
        int index = out.siblingIndex();
        childNodes.remove(index);
        reindexChildren();
        out.parentNode = null;
    }

    protected void addChildren(Node... children) {
        //most used. short circuit addChildren(int), which hits reindex children and array copy
        for (Node child: children) {
            reparentChild(child);
            childNodes.add(child);
            child.setSiblingIndex(childNodes.size()-1);
        }
    }

    protected void addChildren(int index, Node... children) {
        Validate.noNullElements(children);
        for (int i = children.length - 1; i >= 0; i--) {
            Node in = children[i];
            reparentChild(in);
            childNodes.add(index, in);
        }
        reindexChildren();
    }

    private void reparentChild(Node child) {
        if (child.parentNode != null)
            child.parentNode.removeChild(child);
        child.setParentNode(this);
    }
    
    private void reindexChildren() {
        for (int i = 0; i < childNodes.size(); i++) {
            childNodes.get(i).setSiblingIndex(i);
        }
    }
    
    /**
     Retrieves this node's sibling nodes. Effectively, {@link #childNodes()  node.parent.childNodes()}.
     @return node siblings, including this node
     */
    public List<Node> siblingNodes() {
        return parent().childNodes(); // TODO: should this strip out this node? i.e. not a sibling of self?
    }

    /**
     Get this node's next sibling.
     @return next sibling, or null if this is the last sibling
     */
    public Node nextSibling() {
        if (parentNode == null)
            return null; // root
        
        List<Node> siblings = parentNode.childNodes;
        Integer index = siblingIndex();
        Validate.notNull(index);
        if (siblings.size() > index+1)
            return siblings.get(index+1);
        else
            return null;
    }

    /**
     Get this node's previous sibling.
     @return the previous sibling, or null if this is the first sibling
     */
    public Node previousSibling() {
        List<Node> siblings = parentNode.childNodes;
        Integer index = siblingIndex();
        Validate.notNull(index);
        if (index > 0)
            return siblings.get(index-1);
        else
            return null;
    }

    /**
     * Get the list index of this node in its node sibling list. I.e. if this is the first node
     * sibling, returns 0.
     * @return position in node sibling list
     * @see org.jsoup.nodes.Element#elementSiblingIndex()
     */
    public Integer siblingIndex() {
        return siblingIndex;
    }
    
    protected void setSiblingIndex(int siblingIndex) {
        this.siblingIndex = siblingIndex;
    }

    /**
     Get the outer HTML of this node.
     @return HTML
     */
    public String outerHtml() {
        StringBuilder accum = new StringBuilder(32*1024);
        outerHtml(accum);
        return accum.toString();
    }

    protected void outerHtml(StringBuilder accum) {
        new NodeTraversor(new OuterHtmlVisitor(accum, getOutputSettings())).traverse(this);
    }

    // if this node has no document (or parent), retrieve the default output settings
    private Document.OutputSettings getOutputSettings() {
        return ownerDocument() != null ? ownerDocument().outputSettings() : (new Document("")).outputSettings();
    }

    /**
     Get the outer HTML of this node.
     @param accum accumulator to place HTML into
     */
    abstract void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out);

    abstract void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out);

    public String toString() {
        return outerHtml();
    }

    protected void indent(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append("\n").append(StringUtil.padding(depth * out.indentAmount()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // todo: have nodes hold a child index, compare against that and parent (not children)
        return false;
    }

    @Override
    public int hashCode() {
        int result = parentNode != null ? parentNode.hashCode() : 0;
        // not children, or will block stack as they go back up to parent)
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    /**
     * Create a stand-alone, deep copy of this node, and all of its children. The cloned node will have no siblings or
     * parent node. As a stand-alone object, any changes made to the clone or any of its children will not impact the
     * original node.
     * <p>
     * The cloned node may be adopted into another Document or node structure using {@link Element#appendChild(Node)}.
     * @return stand-alone cloned node
     */
    @Override
    public Node clone() {
        return doClone(null); // splits for orphan
    }

    protected Node doClone(Node parent) {
        Node clone;
        try {
            clone = (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        clone.parentNode = parent; // can be null, to create an orphan split
        clone.siblingIndex = parent == null ? 0 : siblingIndex;
        clone.attributes = attributes != null ? attributes.clone() : null;
        clone.baseUri = baseUri;
        clone.childNodes = new ArrayList<Node>(childNodes.size());
        for (Node child: childNodes)
            clone.childNodes.add(child.doClone(clone)); // clone() creates orphans, doClone() keeps parent

        return clone;
    }

    private static class OuterHtmlVisitor implements NodeVisitor {
        private StringBuilder accum;
        private Document.OutputSettings out;

        OuterHtmlVisitor(StringBuilder accum, Document.OutputSettings out) {
            this.accum = accum;
            this.out = out;
        }

        public void head(Node node, int depth) {
            node.outerHtmlHead(accum, depth, out);
        }

        public void tail(Node node, int depth) {
            if (!node.nodeName().equals("#text")) // saves a void hit.
                node.outerHtmlTail(accum, depth, out);
        }
    }
}
```

