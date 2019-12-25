package com.cmlanche.core.utils;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cmlanche.core.search.node.NodeInfo;


/**
 * {@link AccessibilityNodeInfo}
 */
public class AccessibilityNodeInfoHelper {
	public static Rect getVisibleBoundsInScreen(AccessibilityNodeInfo node,
												int screenW, int screenH, NodeInfo myrect) {
		if (node == null) {
			return null;
		}
		Rect nodeRect = new Rect();
		node.getBoundsInScreen(nodeRect);
		Rect displayRect = new Rect();	
		displayRect.top = 0;
		displayRect.left = 0;
		displayRect.right = screenW;
		displayRect.bottom = screenH;		
		nodeRect.intersect(displayRect);
		myrect.setRect(nodeRect);
		myrect.setVisiableRect(nodeRect);
		return nodeRect;
	}
}
