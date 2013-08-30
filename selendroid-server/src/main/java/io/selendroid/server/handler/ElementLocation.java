/*
 * Copyright 2012-2013 eBay Software Foundation and selendroid committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.selendroid.server.handler;

import io.selendroid.android.internal.Point;
import io.selendroid.server.RequestHandler;
import io.selendroid.server.Response;

import org.json.JSONException;
import org.json.JSONObject;
import io.selendroid.exceptions.NoSuchElementException;
import io.selendroid.exceptions.StaleElementReferenceException;
import io.selendroid.server.SelendroidResponse;
import io.selendroid.server.model.AndroidElement;
import io.selendroid.util.SelendroidLogger;
import org.webbitserver.HttpRequest;

public class ElementLocation extends RequestHandler {

  public ElementLocation(HttpRequest request, String mappedUri) {
    super(request, mappedUri);
  }

  @Override
  public Response handle() throws JSONException {
    SelendroidLogger.log("Get element location command");
    String id = getElementId();
    AndroidElement element = getElementFromCache(id);
    if (element == null) {
      return new SelendroidResponse(getSessionId(), 10, new NoSuchElementException("The element with id '"
          + id + "' was not found."));
    }
    Point point = element.getLocation();
    JSONObject result = new JSONObject();
    result.put("x", point.x);
    result.put("y", point.y);
    try {
      return new SelendroidResponse(getSessionId(), result);
    } catch (StaleElementReferenceException se) {
      return new SelendroidResponse(getSessionId(), 10, se);
    }
  }
}
