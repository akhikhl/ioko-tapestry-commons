/*
 * Copyright (c) 2009 ioko365 Ltd
 *
 * This file is part of ioko tapestry-commons.
 *
 *     ioko tapestry-commons is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ioko tapestry-commons is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with ioko tapestry-commons.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.ioko.tapestry.cacheControl.pages;

import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.util.TextStreamResponse;

import uk.co.ioko.tapestry.cacheControl.annotations.CacheControl;
import uk.co.ioko.tapestry.cacheControl.annotations.CacheType;

/**
 * 
 */
@CacheControl(cacheType = CacheType.NEVER)
public class CacheControlVideoPlayerInfoTestPage {
	public Object onActivate() {
		JSONObject videoPlayerInfo = new JSONObject();
		videoPlayerInfo.append("Caching Test", "It Works !!!!");
		
		return new TextStreamResponse("application/json", videoPlayerInfo.toString());
	}
}
