/*
 * Copyright (c) 2009 ioko365 Ltd
 *
 * This file is part of ioko tapestry-commons.
 *
 *     Foobar is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with ioko tapestry-commons.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.ioko.tapestry.caching.pages;

import java.util.List;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.dom.Raw;
import org.apache.tapestry5.test.PageTester;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA. User: ben Date: Mar 25, 2009 Time: 4:52:47 PM
 */
public class CachingTestPageTest {

	@Test
	public void testCaching() throws InterruptedException {

		PageTester pageTester = new PageTester("uk.co.ioko.tapestry.caching", "Test", "src/test/webapp");
		Document document = pageTester.renderPage("CachingTestPage");
		Assert.assertNotNull(document);

		Element caching = document.getElementById("cached");

		String cachedDate = ((Element) caching.getChildren().get(1)).getChildMarkup();
		Element live = document.getElementById("live");
		String liveDate = ((Element) live.getChildren().get(1)).getChildMarkup();
		Element cachedKey = document.getElementById("cachedWithKey");
		String cachedWithKeyDate = ((Element) cachedKey.getChildren().get(1)).getChildMarkup();
		Element script = getScriptElement(document.getRootElement().getChildren());
		Assert
				.assertTrue(script.getChildMarkup()
						.indexOf("Tapestry.init({\n" +
                                "  \"zone\" : [\n" +
                                "    {\n" +
                                "      \"element\" : \"helloZone1\"\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"element\" : \"helloZone2\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "});") > 0);

		Assert.assertEquals(liveDate, cachedDate);
		Assert.assertEquals(liveDate, cachedWithKeyDate);

		Thread.sleep(1000);

		// Render the page again
		Document document2 = pageTester.renderPage("CachingTestPage");

		// We can't just get the element by ID as cached items come through as raw
		Raw rawCached = (Raw) ((Element) document2.getRootElement().getChildren().get(1)).getChildren().get(2);
		String raw = rawCached.toString();
		int indexOfDd = raw.lastIndexOf("<dd>");
		int indexOfCloseDd = raw.lastIndexOf("</dd>");
		String cachedDate2 = raw.substring(indexOfDd + 4, indexOfCloseDd);

		Raw rawCachedKey = (Raw) ((Element) document2.getRootElement().getChildren().get(1)).getChildren().get(3);
		String rawKey = rawCachedKey.toString();
		indexOfDd = rawKey.lastIndexOf("<dd>");
		indexOfCloseDd = rawKey.lastIndexOf("</dd>");
		String cachedWithKeyDate2 = rawKey.substring(indexOfDd + 4, indexOfCloseDd);

		Element live2 = document2.getElementById("live");
		String liveDate2 = ((Element) live2.getChildren().get(1)).getChildMarkup();

		// Time should have passed
		Assert.assertTrue(!liveDate2.equals(liveDate));
		// But the cached date should not have changed
		Assert.assertEquals(cachedDate, cachedDate2);

		Assert.assertEquals(cachedWithKeyDate, cachedWithKeyDate2);

		// zones should have been initialised as expected despite being inside the cached parts of the page - this tests
		// ClientBehaviorSupportPlayer/Recorder
		script = getScriptElement(document2.getRootElement().getChildren());
		Assert
				.assertTrue(script.getChildMarkup()
						.indexOf("Tapestry.init({\n" +
                                "  \"zone\" : [\n" +
                                "    {\n" +
                                "      \"element\" : \"helloZone1\"\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"element\" : \"helloZone2\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "});") > 0);
	}

	@Test
	public void testOverideCaching() throws InterruptedException {
		PageTester pageTester = new PageTester("uk.co.ioko.tapestry.caching", "TestOverideConfig", "src/test/webapp");
		Document document = pageTester.renderPage("CachingTestPage");
		Assert.assertNotNull(document);
	}

	private Element getScriptElement(List<Node> nodes) {
		for (Node node : nodes) {
			if (node instanceof Element) {
				Element element = (Element) node;
				if (element.getName().equals("script") && (element.getAttribute("src") == null)) {
					return element;
				} else {
					Element scriptElement = getScriptElement(element.getChildren());
					if (scriptElement != null) {
						return scriptElement;
					}
				}
			}
		}
		return null;
	}
}
