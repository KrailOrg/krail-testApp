/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */

package uk.q3c.krail.testbench.page.object;

import com.google.common.primitives.Ints;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.TreeElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import uk.q3c.krail.core.vaadin.ID;
import uk.q3c.krail.core.view.component.DefaultUserNavigationTree;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by david on 04/10/14.
 */
public class NavTreePageObject extends PageObject {
    private String id = ID.getIdc(Optional.empty(), DefaultUserNavigationTree.class);

    public NavTreePageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public String itemCaption(int... index) {
        return webElement(false, index).getText();
    }

    /**
     * @param expand if true, set the id so that it will expand when clicked, otherwise select the node will just select when
     *               clicked
     * @param index  an integer array describing the path to the required node
     * @return
     */
    private WebElement webElement(boolean expand, int... index) {
        ElementPath elementPath = new ElementPath(parentCase.getAppContext());
        elementPath.id(id)
                .index(index);
        if (expand) {
            elementPath.expand();
        }
        return parentCase.getDriver()
                .findElement(By.vaadin(elementPath.get()));
    }

    /**
     * Clears the selection
     */
    public void clear() {
        treeElement().clear();
    }

    private TreeElement treeElement() {
        return element(TreeElement.class, id);
    }

    public void expandCollapse(int... index) {
        webElement(true, index).click();
    }

    /**
     * Just the first part of the path, no other segments
     *
     * @param path0
     */
    public void expandCollapse(String path0) {
        List<Integer> index = treeItemIndex(path0, Optional.empty(), DefaultUserNavigationTree.class);
        int[] indexArray = Ints.toArray(index);
        webElement(true, indexArray[0]).click();
    }

    /**
     * Returns the WebElement described by the url "path"
     *
     * @param path
     * @return
     */
    protected List<Integer> treeItemIndex(String path, Optional<?> qualifier, Class<?>... componentClasses) {
        checkNotNull(path);
        String[] segments = path.split("/");
        Queue<String> queue = new ArrayDeque<>();
        queue.addAll(Arrays.asList(segments));
        WebElement parentElement = navTree();

        TreeNodeInfo nodeInfo = null;
        List<Integer> indexes = new ArrayList<>();
        while (queue.size() > 0) {
            parentElement.click();

            nodeInfo = getChildElement(parentElement, queue.poll());
            indexes.add(nodeInfo.index);
            parentElement = nodeInfo.nodeElement;
        }

        return indexes;
    }

    private TreeElement navTree() {
        TreeElement element = element(TreeElement.class, Optional.empty(), DefaultUserNavigationTree.class);
        return element;
    }

    private TreeNodeInfo getChildElement(WebElement parentElement, String segment) {
        TreeNodeInfo nodeInfo = new TreeNodeInfo();
        List<WebElement> nodeElements = parentElement.findElements(By.className("v-tree-node"));
        List<WebElement> nodeChildrenElements = parentElement.findElements(By.className("v-tree-node-children"));
        List<WebElement> nodeCaptionElements = parentElement.findElements(By.className("v-tree-node-caption"));
        int index = -1;
        for (int i = 0; i < nodeElements.size(); i++) {
            WebElement element = nodeElements.get(i);
            if (element.getText()
                    .equals(segment)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            throw new TreePathException("Segment " + segment + " not found");
        }
        nodeInfo.index = index;
        nodeInfo.nodeElement = nodeElements.get(index);
        nodeInfo.nodeCaptionElement = nodeCaptionElements.get(index);
        nodeInfo.nodeChildrenElement = nodeChildrenElements.get(index);
        return nodeInfo;
    }

    public void select(String path) {
        List<Integer> index = treeItemIndex(path, Optional.empty(), DefaultUserNavigationTree.class);
        int[] indexArray = Ints.toArray(index);
        select(indexArray);
    }

    public void select(int... index) {
        webElement(false, index).click();
    }

    public String currentSelection() {
        TreeElement tree = navTree();
        String html = tree.getHTML();
        Document doc = Jsoup.parse(html);
        Elements treeNodes = doc.getElementsByClass("v-tree-node");
        for (Element node : treeNodes) {
            if (node.getElementsByAttributeValueMatching("aria-selected", "true").size() == 1) {
                Element span = node.getElementsByTag("span").get(0);
                return span.text();
            }
        }
        return null;


    }


    public static class TreeNodeInfo {
        WebElement nodeElement;
        WebElement nodeCaptionElement;
        WebElement nodeChildrenElement;
        int index;
    }

}
